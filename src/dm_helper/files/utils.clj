(ns dm-helper.files.utils
  (:gen-class)
  (:import [org.apache.commons.io FileUtils])
  (:use [clojure.pprint])
  (:require [clojure.java.io :as io]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [inflections.core :as infl]
            [clojure.string :as string]))

(defn zip-str [s]
  (zip/xml-zip
   (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))

(defn data-file [file]
  (let [res (io/resource file)]
    (if (nil? res)
      (io/file file)
      (io/file res))))

(defn is-xml? [file]
  (not (nil? (re-find (re-pattern "\\.xml$") file))))

(defn not-russian? [file]
  (nil? (re-find (re-pattern "Russian") file)))

(defn all-xml-from-dir [dirpath]
  (doall (filter
          (fn [n]
            (and (not-russian? (.getPath n)) (is-xml? (.getName n))))
          (file-seq (io/file dirpath)))))

(defn fix-info-keys [info]
  (into {}
        (map
         (fn [e]
           (let [key (first e)
                 stuff (second e)]
             {(keyword (infl/plural (name key))) stuff}))
         info)))

(defn not-re-find-name?
  [thing pattern]
  (not
   (nil?
    (re-find pattern (:name thing)))))

(defn filter-by-name
  "Find a creature in the list"
  [things looking-for]
  (let [pattern (re-pattern looking-for)]
    (into []
          (filter
           #(not-re-find-name? % pattern)
           things))))
