(ns wb-metrics.metrics
  "Namespace for function instrumentation: logging, timing, metrics."
  (:require [metrics.meters :as meter]
            [metrics.timers :as timer])
  (:import (java.util.concurrent TimeUnit)))


(defn ^:deprecated mark-meter
  "Sends a meter metric for a given metric name."
  [metric-name]
  (meter/mark! (meter/meter metric-name)))


(defn ^:deprecated send-elapsed
  "Sends a timer metric for a given metric name and elapsed milliseconds."
  [metric-name millis]
  (.update (timer/timer metric-name) millis TimeUnit/MILLISECONDS))
