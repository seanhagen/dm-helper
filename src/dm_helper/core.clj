(ns dm-helper.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.xml :as xml]
            [clojure.zip :as zip]))

(defn zip-str [s]
  (zip/xml-zip
   (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))

(def data-file
  (io/file
   (io/resource
    "test.xml")))

;; (defn -main
;;   "Learning!"
;;   [& args]
;;   (println
;;    (first
;;     (:content
;;      (first
;;       (:content
;;        (first
;;         (:content
;;          (first
;;           (zip-str (slurp data-file)))))))))))


(defn get-name
  "WHEEE"
  [thing]
  (println "get name: ")
  (println (:content thing)))

(defn -main
  "LEEEARNING"
  [& args]
  (let [data (:content (first (zip-str (slurp data-file))))]
    (println "Got data: " data)
    (map get-name data)))
