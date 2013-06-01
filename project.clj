(defproject quadratic-residue "0.0.1"
  :url "http://quadratic-residue.destructuring-bind.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lacij "0.8.1"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.3"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler quadratic-residue.handler/app}
  :min-lein-version "2.1.3"
  :warn-on-reflection true
  :description "A web-based quadratic residue explorer")
