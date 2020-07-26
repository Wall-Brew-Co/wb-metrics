(ns wb-metrics.correlation-ids
  "Namespace for the inclusion of session/request IDs"
  (:import (java.util UUID)))

(def ^:dynamic *ids* nil)

(defn get-correlation-ids [] *ids*)

(defn with-correlation-ids
  [f ids]
  (binding [*ids* ids]
    (f)))

(defn generate-correlation-ids
  []
  {:x-session-id (str (UUID/randomUUID))
   :x-request-id 1})

(defn add-correlation-ids
  "Add session ID and request ID to headers of the request/response."
  [request]
  (when (map? request)
    (update request :headers assoc "x-session-id" (str (:x-session-id *ids*))
                                   "x-request-id" (str (:x-request-id *ids*)))))

(defn get-header-correlation-ids
  "Fetch the session ID and request ID of the headers of the request/response"
  [request]
  (let [headers (:headers request)
        sid     (not-empty (headers "x-session-id"))
        rid     (not-empty (headers "x-request-id"))]
    (when sid
      {:x-session-id sid
       :x-request-id (or rid 1)})))

(defn correlation-ids-middleware
  "clj-http compatible middleware that will add dd session ID and request ID to headers of the request/response."
  [client]
  (fn [request] (client (add-correlation-ids request))))
