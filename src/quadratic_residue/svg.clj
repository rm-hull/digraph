(ns quadratic-residue.svg
  (:use [lacij.layouts.layout]
        [lacij.edit.graph]
        [lacij.view.graphview]
        [lacij.view.core]
        [analemma.xml]))

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

(defn fixed-point? [[a b]] (= a b))

(defn ->svg [stream digraph & {:keys [w h] :or {w 800 h 600}}]
  (let [nodes (map first digraph)
        edges (remove fixed-point? digraph)]
    (->
      (graph :width w :height h)
      (add-default-node-attrs :width 25 :height 25 :shape :circle)
      (add-nodes nodes)
      (add-edges edges)
      (layout :radial :radius 90)
      (build)
      (export stream :indent "yes"))))

; lacij cant handle loops very well, and radial layout is not so good...
