(ns quadratic-residue.core
  (:use [lacij.layouts.layout]
        [lacij.edit.graph]
        [lacij.view.graphview]
        [lacij.view.core]
        [analemma.xml]))

(defn follow [lookup-table]
  (fn [n]
    (loop [k n
           edges {}] 
      (let [next-k (lookup-table k)] 
        (if (edges next-k)
          edges
          (recur next-k (assoc edges k next-k)))))))

(defn digraph [n f]
  (let [lookup-table (vec (map #(mod (f %) n) (range n)))]
    (apply merge (map (follow lookup-table) (range n))))) 
 
(defn fixed-point? [[a b]] (= a b))
; ---------------------------------------------------

(defn node-id [x]
  (keyword (str "node-" x)))

(defn edge-id [x y]
  (keyword (str "edge-" x "-" y)))

(defn add-nodes [g nodes]
  (reduce 
    (fn [g x] (add-node g (node-id x) (str x) :style {:fill "lightblue"})) 
    g nodes))

(defn add-edges [g edges]
  (reduce 
    (fn [g [from to]] (add-edge g (edge-id from to) (node-id from) (node-id to))) 
    g edges))

(defn ->svg [stream n f & {:keys [w h] :or {w 800 h 600}}]
  (let [d (digraph n f)
        nodes (map first d)
        edges (remove fixed-point? d)]
    (->
      (graph :width w :height h)
      (add-default-node-attrs :width 25 :height 25 :shape :circle)
      (add-nodes nodes)
      (add-edges edges)
      (layout :radial :radius 90)
      (build)
      (export stream :indent "yes"))))

; lacij cant handle loops very well, and radial layout is not so good...

(def sq (fn [x] (* x x)))

(->svg "resources/digraph-125.svg" 125 sq)
