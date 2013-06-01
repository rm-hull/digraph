(ns quadratic-residue.handler
  (:use [compojure.core]
        [clojure.pprint]
        [hiccup.middleware :only [wrap-base-url]]
        [ring.middleware.params :only [wrap-params]] 
        [ring.util.io :only [piped-input-stream]]
        [ring.util.response :only [response content-type status]]
        [seman.svg]
        [seman.core])
  (:require [compojure.handler :as handler])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream OutputStreamWriter]))

(defn- create-pipe [f pipe-size]
  (with-open [out-stream (ByteArrayOutputStream. pipe-size)]
    (f out-stream)
    (ByteArrayInputStream. (.toByteArray out-stream))))


(defn svg [sentence]
  (if (seq sentence)
    (let [result (analyze sentence)
          nodes  (node-finder result)
          edges  (edge-finder result)
          labels (leaf-finder result)] 
      (->
        (piped-input-stream #(->svg nodes edges labels %))
        (response)
        (content-type "image/svg+xml")))
    (-> 
      (response "No sentence supplied") 
      (status 400)))) 

(defroutes app-routes
  (GET "/x2/:n" [n] (digraph n (fn [x] (* x x))))
  (GET "/x2+1/:n" [n] (digraph n (fn [x] (inc (* x x)))))
  (GET "/x3/:n" [n] (digraph n (fn [x] (* x x x))))
  )

(def app 
  (-> 
    (handler/site app-routes)
    (wrap-base-url)
    (wrap-params)))
