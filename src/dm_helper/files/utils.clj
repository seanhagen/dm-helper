(ns dm-helper.files.utils
  (:gen-class)
  (:import [org.apache.commons.io FileUtils])
  (:require [clojure.java.io :as io]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
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

(defn files-by-folder [base-path folder]
  (let [path (io/file base-path folder)
        files (file-seq path)]
    (filter #(is-xml? (.getPath %)) files)))

(defn monster-files
  ""
  [base-path]
  (files-by-folder base-path "Bestiary"))

(defn spell-files
  ""
  [base-path]
  (files-by-folder base-path "Spells"))
