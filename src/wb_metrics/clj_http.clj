(ns wb-metrics.clj-http
  "Namespace for clj-http middleware"
  (:require [wb-metrics.correlation-ids :as corr-ids]))

(declare with-correlation-ids)

(try
  (require 'clj-http.client)
  (eval
   '(defn with-correlation-ids [f]
      (clj-http.client/with-additional-middleware [corr-ids/correlation-ids-middleware] (f))))
  (catch Exception _
    (defn with-correlation-ids [f] (f))))
