(ns wb-metrics.metrics
  "Namespace for function instrumentation: logging, timing, metrics."
  (:require [clojure.string :as cs]
            [clojure.tools.logging :as log]
            [metrics.meters :refer [meter mark!]]
            [metrics.timers :refer [timer]]
            [robert.hooke :refer [add-hook]])
  (:import (java.util.concurrent TimeUnit)))

(defn mark-meter
  "Sends a meter metric for a given metric name.
   Optionally takes a number argument to indicate multiple marks."
  ([metric-name]
   (mark! (meter metric-name)))
  
  ([metric-name n]
   (mark! (meter metric-name) n)))

(defn send-elapsed
  "Sends a timer metric for a given metric name and elapsed milliseconds."
  [metric-name millis]
  (.update (timer metric-name) millis TimeUnit/MILLISECONDS))

(defn- log-function-timing
  "Logs the execution time of a given function.
   Takes a format function, a name path vector, target function, and target function arguments."
  [ns' format-fn name-path f & args]
  (let [start (System/nanoTime)
        metric-path #(apply vector % (rest name-path))
        f-ret (try
                (let [ret (apply f args)]
                  (mark-meter (metric-path "success"))
                  ret)
                (catch Throwable t
                  (mark-meter (metric-path "failure"))
                  (throw t)))
        millis (int (/ (- (System/nanoTime) start) 1000000))
        fname (format "%s/%s"
                      (cs/join "." (butlast name-path))
                      (last name-path))]
    (log/log ns' :debug nil (apply format-fn fname millis f-ret args))
    (send-elapsed (metric-path "timing") millis)
    f-ret))

(defn format-elapsed
  "Returns a formatted string of function name and milliseconds."
  [fname millis _ & _]
  (format "%s elapsed %d ms" fname millis))

(defn format-elapsed-args
  "Returns a formatted string of function name & args and elapsed milliseconds."
  [fname millis _ & args]
  (format "(%s %s) elapsed %d ms"
          fname (cs/join " " (map pr-str args)) millis))

(defn format-elapsed-args-ret
  "Returns a formatted string of function name, args, return value, and elapsed milliseconds."
  [fname millis ret & args]
  (format "(%s %s) elapsed %d ms => %s"
          fname (cs/join " " (map pr-str args)) millis (pr-str ret)))

(defn format-fn
  "Takes a function accepting return value and arguments.
   Returns a function accepting function name, milliseconds, return value, and arguments.
   Returned function will return concatenated string output of format-elapsed and f."
  [f]
  (fn [fname millis ret & args]
    (format "%s; %s"
            (apply format-elapsed fname millis ret args)
            (apply f ret args))))

(defn instrument-fn
  "Adds a hook to time and log target-var function execution."
  ([target-var]
   (instrument-fn target-var format-elapsed))
  
  ([target-var format-fn]
   (let [{tn :name tns :ns} (meta target-var)
         name-path (conj (cs/split (str tns) #"\.") (str tn))]
     (add-hook
      target-var
      ::instrument
      (fn [f & args]
        (apply log-function-timing tns format-fn name-path f args))))))
