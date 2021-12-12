(ns wb-metrics.logging
  "Namespace for logging configuration"
  (:require [clojure.string :as cs]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.logstash :as logstash]
            [taoensso.timbre.tools.logging :as log]
            [timbre-ns-pattern-level]
            [wb-metrics.config :as config]
            [wb-metrics.correlation-ids :as corr-ids]
            [wb-metrics.versioneer :as versioneer])
  (:import (java.io StringWriter)))


(defn decorate-logs
  [data {:keys [artifact-name] :as opts}]
  (let [version-info (when artifact-name (versioneer/get-artifact-info opts))]
    (-> data
        (merge version-info)
        (merge (corr-ids/get-correlation-ids)))))


(defn local-format
  [opts data]
  (let [{:keys [no-stacktrace?]}                           opts
        {:keys [level ?err msg_ ?ns-str ?file timestamp_]} data
        time                                               (force timestamp_)
        log-level                                          (cs/upper-case (name level))
        log-source                                         (or ?ns-str ?file "?")
        message                                            (force msg_)]
    (str time " " log-level " [" log-source "] - " message
         (when-not no-stacktrace?
           (when-let [err ?err]
             (str "\n" (timbre/stacktrace err opts)))))))


(defn logstash-format
  [data]
  (let [writer (StringWriter.)]
    (logstash/data->json-stream data writer {:pr-stacktrace #(print (timbre/stacktrace % {:stacktrace-fonts nil}))})
    (.toString writer)))


(defn log-level-middleware
  [root-level {:keys [ns-levels]}]
  (let [root-logging-level (or root-level :info)]
    (timbre-ns-pattern-level/middleware
      (merge {"org.eclipse.jetty.*" :info ; These namespaces walk over and handle all inbound HTTP requests
              "org.apache.http.*"   :info ; The Request builder logs a :debug level warning containing request headers, possibly including API Keys
              "com.zaxxer.hikari.*" :info ; The db pool logs ~3 messages each time it checks to see if resizing is needed
              :all                  root-logging-level}
             ns-levels))))


(defn configure!
  ([] (configure! {}))

  ([{:keys [root-level]
     :as   opts}]
   (let [formatter    (if config/local? (partial local-format opts) logstash-format)
         formatter-fn #(formatter (update % :context decorate-logs opts))]
     (timbre/merge-config! {:middleware [(log-level-middleware root-level opts)]
                            :async?     true
                            :output-fn  formatter-fn})
     (log/use-timbre))))
