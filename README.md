# wb-metrics

A Wall Brew infrastructure library to monitor function performance utilizing [timbre.](https://github.com/ptaoussanis/timbre)
This library has utility functions to work with [clj-http](https://github.com/dakrone/clj-http) and [ring](https://github.com/ring-clojure/ring) to provide cross-application session and request IDs.

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
