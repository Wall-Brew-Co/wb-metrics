(defproject com.wallbrew/wb-metrics "2.0.0"
  :description "A library for collecting runtime performance metrics"
  :url "https://github.com/Wall-Brew-Co/wb-metrics"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[cheshire "5.8.1"]
                 [com.fzakaria/slf4j-timbre "0.3.19"]
                 [com.taoensso/timbre "4.10.0"]
                 [compojure "1.6.1"]
                 [metrics-clojure "2.10.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.logging "1.1.0"]
                 [robert/hooke "1.3.0"]
                 [timbre-ns-pattern-level "0.1.2"]
                 [trptcolin/versioneer "0.2.0"]])
