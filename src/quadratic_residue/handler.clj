(ns quadratic-residue.handler
  (:use [compojure.core]
        [hiccup.middleware :only [wrap-base-url]]
        [ring.middleware.params :only [wrap-params]] 
        [ring.util.io :only [piped-input-stream]]
        [ring.util.response :only [response content-type status]]
        [quadratic-residue.svg]
        [quadratic-residue.core])
  (:require [compojure.handler :as handler]))

(defn sq [x] (* x x))

(defn graph [n f]
  (let [n (Integer/parseInt n)
        d (digraph n f)]
    (->
      (piped-input-stream #(->svg % d))
      (response)
      (content-type "image/svg+xml"))))

(defroutes app-routes
  (GET "/x2/:n"   [n] (graph n sq))
  (GET "/x2+1/:n" [n] (graph n (comp inc sq)))
  (GET "/x3/:n"   [n] (graph n (fn [x] (* x x x))))
  )

(def app 
  (-> 
    (handler/site app-routes)
    (wrap-base-url)
    (wrap-params)))
