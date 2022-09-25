(ns maze.cell
  (:refer-clojure))

(defrecord Cell [row column north south east west links])

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn initialize [row column]
  (->Cell row column nil nil nil nil (ref {})))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
; Note : Link and Unlink, self and cell use atom
(defn link
  ([self-atom cell-atom]
   (link self-atom cell-atom true))
  ([self-atom cell-atom bidi]
   (when bidi
     (link cell-atom self-atom false))
   (dosync
    (alter (:links @self-atom) assoc cell-atom true))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn unlink
  ([self-atom cell-atom]
   (unlink self-atom cell-atom true))
  ([self-atom cell-atom bidi]
   (when bidi
     (unlink cell-atom self-atom false))
   (dosync
    (alter (:links @self-atom) dissoc cell-atom true))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn links [self]
  (keys @(:links self)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn linked? [self cell]
  (find @(:links self) cell))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn neighbors [self]
  (let [result (transient [])]
    (when (:north self)
      (conj! result (:north self)))
    (when (:south self)
      (conj! result (:south self)))
    (when (:east self)
      (conj! result (:east self)))
    (when (:west self)
      (conj! result (:west self)))
    (persistent! result)))