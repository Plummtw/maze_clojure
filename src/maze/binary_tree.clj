(ns maze.binary-tree
  (:refer-clojure)
  (:require [maze.grid :as grid]
            [maze.cell :as cell]))

(defn on [grid]
  #_{:clj-kondo/ignore [:unresolved-symbol]}
  (grid/each-cell [cell grid]
                  (let [neighbors (cond-> []
                                    (:north @cell) (conj (:north @cell))
                                    (:east @cell) (conj (:east @cell)))
                        index (rand-int (count neighbors))]
                    ;(println (:north cell))
                    ;(println (count neighbors))
                    (when (> (count neighbors) 0)
                      (cell/link cell (nth neighbors index) true)))))

(comment
  (def a (grid/initialize 4 4))
  (on a)
  (println (grid/to-s a))
  )