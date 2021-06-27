(ns wb-metrics.versioneer
  "Namespace for artifact information gathering"
  (:require [trptcolin.versioneer.core :as versioneer]))


(defn get-artifact-info*
  "Retrieve the version information from project.clj
   For Wall Brew projects, simpy include the artifact name: e.g. common-beer-format
   If the artifact belongs to another group, that can be passed as well: e.g. trptcolin/versioneer"
  [{:keys [group-name artifact-name]}]
  (let [artifact-group   (or group-name "com.wallbrew")
        artifact-version (versioneer/get-version artifact-group artifact-name)
        artifact-hash    (versioneer/get-revision artifact-group artifact-name)]
    (hash-map :app-name artifact-name :app-version artifact-version :app-hash artifact-hash)))


(def get-artifact-info
  (memoize get-artifact-info*))
