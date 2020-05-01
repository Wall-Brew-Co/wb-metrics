(defproject com.wallbrew/wb-metrics "1.0.0"
  :description "A library for collecting runtime performance metrics"
  :url "https://github.com/Wall-Brew-Co/wb-metrics"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[com.fzakaria/slf4j-timbre "0.3.19"]
                 [com.taoensso/timbre "4.10.0"]
                 [metrics-clojure "2.10.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.logging "1.1.0"]
                 [robert/hooke "1.3.0"]])
