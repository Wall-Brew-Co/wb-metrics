# wb-metrics

A Wall Brew infrastructure library to monitor function performance.
wb-metrics measures the time it takes functions to execute, uses `clojure.tools.logging` to emit debug statements.

## Dependency

Add the library to your `:dependencies`:

```clojure
[com.wallbrew/wb-metrics "2.0.0"]
```

## Usage

To get basic runtime information:

```clojure
(instrument-fn #'my-function)
```

Call `instrument.core/instrument-fn` with the named symbol of the target function.
Optionally, you may pass another function to format the log entry.
You can choose from the provided formatting functions or write your own.

```clojure
(require 'wb-metrics.core)

(defn my-function
  [x y & z]
  (apply + x y z))

;; default logging
(instrument-fn #'my-function)
(my-function 1 2)
;; logs "user/my-function elapsed 123 ms"

;; log timing w/args + return value
(instrument-fn #'my-function format-elapsed-args-ret)
(my-function 1 2 3 4 5)
;; logs "(user/my-function 1 2 3 4 5) elapsed 123 ms => 15"

;; log timing w/custom ret & args formatter
(instrument-fn #'my-function
               (format-fn (fn [ret & args]
                            (format "ret=%d; args product=%s" ret (apply * args)))))
;; logs "user/my-function elapsed 123 ms; ret=15; args product=120"
```
