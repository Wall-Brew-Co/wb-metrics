# wb-metrics

A Wall Brew infrastructure library to monitor function performance.
wb-metrics measures the time it takes functions to execute, uses `clojure.tools.logging` to emit debug statements.

## Dependency

Add the library to your `:dependencies`:

```clojure
[com.wallbrew/wb-metrics "2.0.0"]
```

## Usage

Configure `wb-metrics` to act as your primary logger somewhere in your application startup:

```clojure
(:require [wb-metrics.logging :as metrics])

(metrics/configure!)
```
