(ns maze.main-test
  (:require [clojure.test :refer [deftest testing is]]
            [maze :as main]))

(deftest test-one
  (testing "Test One"
    (is (= "one" (main/one)))))