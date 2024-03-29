(defproject com.wallbrew/wb-metrics "3.0.0"
  :description "A library for collecting runtime performance metrics"
  :url "https://github.com/Wall-Brew-Co/wb-metrics"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[cheshire "5.8.1"]
                 [com.fzakaria/slf4j-timbre "0.3.21"]
                 [com.taoensso/timbre "5.1.2"]
                 [compojure "1.6.1"]
                 [metrics-clojure "2.10.0"]
                 [org.clojure/clojure "1.10.3"]
                 [timbre-ns-pattern-level "0.1.2"]
                 [trptcolin/versioneer "0.2.0"]])
