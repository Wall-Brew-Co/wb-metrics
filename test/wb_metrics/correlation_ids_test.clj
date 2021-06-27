(ns wb-metrics.correlation-ids-test
  (:require [wb-metrics.correlation-ids :as sut]
            [clojure.test :as t]))


(t/deftest get-from-headers-test
  (t/testing "Headers can be retreived as keywords or strings"
    (t/is (= "value" (sut/get-from-headers {"key" "value"} "key")))
    (t/is (= "value" (sut/get-from-headers {:key "value"} "key")))
    (t/is (nil? (sut/get-from-headers {:key2 "value2"} "key1"))))
  (t/testing "If the value is the empty string, discard it"
    (t/is (nil? (sut/get-from-headers {"key" ""} "key")))
    (t/is (nil? (sut/get-from-headers {:key ""} "key")))))
