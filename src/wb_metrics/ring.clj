(ns wb-metrics.ring
  "Namespace for ring server utilities"
  (:require [clojure.string :as cs]
            [compojure.core :as compojure]
            [taoensso.timbre :as logger]
            [wb-metrics.clj-http :as clj-http]
            [wb-metrics.correlation-ids :as corr-ids]
            [wb-metrics.metrics :as metrics]
            [wb-metrics.versioneer :as versioneer]))

(defn request->metric
  "Creates a metric name from the request.
  e.g.  GET /v1/recipe/:id and a request of /v1/loan/1234
  is turned into [\"requests\" \"get\" \"v1\" \"recipe\" \"_id\"]"
  [request]
  (let [[method route] (:compojure/route request)]
    (when-not (= "/*" route)
      (vec (concat ["requests" (name method)]
                   (some-> route
                           (cs/replace #":|-" "_")
                           (cs/split #"/")))))))

(defn static-asset?
  [{:keys [uri]}]
  (some #(cs/ends-with? uri %) [".css" ".js" ".html"]))

(defn wrap-metrics
  [handler]
  (fn [request]
    (let [start  (System/currentTimeMillis)
          metric (when-not (static-asset? request)
                   (request->metric request))]
      (when metric
        (metrics/mark-meter metric))
      (try
        (let [resp (handler request)]
          (when metric
            (metrics/mark-meter (conj metric (str (:status resp)))))
          resp)
        (finally
          (when metric
            (let [duration (- (System/currentTimeMillis) start)]
              (metrics/send-elapsed (cons "timing" metric) duration))))))))

(defn log-error
  [e request]
  (logger/with-context (select-keys request [:request-method :uri :query-string])
    (logger/errorf e "Server error! '%s' handling %s %s"
                   (.getMessage e)
                   (-> request :request-method name cs/upper-case)
                   (:uri request))))

(defn wrap-logging*
  "Supported option:
    * excluded-routes - sequence of strings representing routes that shouldn't be logged"
  [handler & [{:keys [excluded-routes]}]]
  (let [excluded-routes (set excluded-routes)]
    (fn [request]
      (let [[method route] (:compojure/route request)
            uri            (:uri request)
            excluded?      (or (contains? excluded-routes route)
                               (static-asset? request))
            ip             (:remote-addr request)
            fwd            (not-empty (get-in request [:headers "x-forwarded-for"]))]
        (when-not excluded?
          (logger/infof "Handling request: %s %s from: %s" method uri (if fwd (str ip "/" fwd) ip)))
        (try
          (handler request)
          (catch Throwable t
            (log-error t request)
            (throw t)))))))

(defn wrap-log-correlation-ids
  [handler]
  (fn [request]
    (let [corr-ids (or (corr-ids/get-header-correlation-ids request)
                       (corr-ids/generate-correlation-ids))]
      (corr-ids/with-correlation-ids
        (fn [] (clj-http/with-correlation-ids #(corr-ids/add-correlation-ids (handler request))))
        corr-ids))))

(defn wrap-instrument-server
  "Supported option:
    * excluded-routes - sequences of strings representing compojure routes that should be excluded from logging"
  [handler & [opts]]
  (-> handler
      (compojure/wrap-routes #(wrap-logging* % opts))
      (compojure/wrap-routes wrap-metrics)
      wrap-log-correlation-ids))
