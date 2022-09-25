(ns maze.grid
  (:refer-clojure)
  (:require [maze.cell :as cell]))

(defrecord Grid [rows columns grid])
(declare prepare-grid)
(declare configure-cells)

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn initialize [rows columns]
  (let [result (->Grid rows columns (prepare-grid rows columns))]
    (configure-cells result)))

(defn- prepare-grid [rows columns]
  (for [row (range rows)]
    (for [column (range columns)]
      (atom (cell/initialize row column)))))

(defmacro each-row [[row self] & body]
  `(doseq [~row (:grid ~self)]
     ~@body))

(defmacro each-cell [[cell self] & body]
  `(doseq [row# (:grid ~self)]
     (doseq [~cell row#]
       ~@body)))

(defn get-cell [self row column]
  (cond
    (or (< row 0) (>=  row (:rows self))) nil
    (or (< column 0) (>= column (:columns self))) nil
    :else (-> (:grid self) (nth row) (nth column))))

(defn print-cell [self row column]
  (let [cell @(get-cell self row column)]
    (println (str (:row cell) " " (:column cell)))
    (when-let [cell-ref (:north cell)]
      (println (str "north: " (:row @cell-ref) " " (:column @cell-ref))))
    (when-let [cell-ref (:south cell)]
      (println (str "south: " (:row @cell-ref) " " (:column @cell-ref))))
    (when-let [cell-ref (:east cell)]
      (println (str "east: " (:row @cell-ref) " " (:column @cell-ref))))
    (when-let [cell-ref (:west cell)]
      (println (str "west: " (:row @cell-ref) " " (:column @cell-ref))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn random-cell [self]
  (let [row (rand-int (:rows self))
        column (rand-int (:columns self))]
    (get-cell self row column)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn size [self]
  (* (:rows self) (:columns self)))

(defn to-s [self]
  (let [builder (StringBuilder.)]
    (.append builder (format "+%s\n" (apply str (repeat (:columns self) "---+"))))

    #_{:clj-kondo/ignore [:unresolved-symbol]}
    (each-row [row self]
              (let [top (StringBuilder. "|")
                    bottom (StringBuilder. "+")]
                (doseq [cell row]
                  (.append top "   ")
                  (.append top
                           (if (cell/linked? @cell (:east @cell)) " " "|"))
                  (.append bottom
                           (if (cell/linked? @cell (:south @cell)) "   " "---"))
                  (.append bottom "+"))
                (.append builder top)
                (.append builder "\n")
                (.append builder bottom)
                (.append builder "\n")))
    (.toString builder)))

(comment
  (macroexpand-1 '(each-cell [cell self] (println cell)))

  (initialize 4 4)
  (def a (initialize 4 4))
  a
  (configure-cells a)
  (print-cell a 0 0)
  (print-cell a 1 1)
  (print-cell a 3 3)
  (println (to-s a))

  (get-cell a 0 0)
  (get-cell a 3 3)

  (:rows a)
  (< 0 1))


(defn- configure-cells [self]
  #_{:clj-kondo/ignore [:unresolved-symbol]}
  (each-cell [cell self]
             (let [row (:row @cell) column (:column @cell)]
              ;;  (println (str row column))
               (dosync
                (swap! cell assoc :north (get-cell self (dec row) column)
                       :south (get-cell self (inc row) column)
                       :west (get-cell self row (dec column))
                       :east (get-cell self row (inc column))))))
  self)
