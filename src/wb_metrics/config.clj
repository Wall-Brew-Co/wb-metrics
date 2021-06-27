(ns wb-metrics.config)


(def local?
  (not (System/getenv "HEROKU_ENV")))
