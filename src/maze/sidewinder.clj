(ns maze.sidewinder  (:refer-clojure)
    (:require [maze.grid :as grid]
              [maze.cell :as cell]))

(defn on [grid]
  #_{:clj-kondo/ignore [:unresolved-symbol]}
  (grid/each-row [row grid]
                 (let [run (atom [])]
                   (doseq [cell row]
                     (dosync (swap! run conj cell))
                     (let [at-eastern (nil? (:east @cell))
                           at-northern (nil? (:north @cell))
                           should-close-out (or at-eastern
                                                (and (not at-northern)
                                                     (= 0 (rand-int 2))))]
                       ;(println (:row @cell) (:column @cell))
                       (if should-close-out
                         (when-not at-northern
                           (let [member (rand-nth @run)]
                             (cell/link member (:north @member) true))
                           (reset! run []))
                         ;else
                         (cell/link cell (:east @cell) true)))))))

(comment
  (rand-nth ['a])

  (def a (grid/initialize 4 4))
  (on a)
  (println (grid/to-s a)))