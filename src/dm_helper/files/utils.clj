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
  ;; (re-matches (re-pattern "\\.xml$") file)
  (not (nil? (re-find (re-pattern "\\.xml$") file))))

(defn not-russian? [file]
  (nil? (re-find (re-pattern "Russian") file)))

(defn not-ua? [file]
  (nil? (re-find (re-pattern "Unearthed Arcana") file)))

(defn all-xml-from-dir [dirpath]
  (doall (filter (fn [n]
                   ;; (println "file: " (.getName n) ", is xml: " (is-xml? (.getName n)))
                   (and
                    (not-russian? (.getPath n))
                    (is-xml? (.getName n))
                    (not-ua? (.getPath n))))
                 (file-seq (io/file dirpath)))))

(defn files-by-filter [base-path folder f]
  (let [path (io/file base-path folder)
        files (file-seq path)]
    (filter f files)))

(defn files-by-folder [base-path folder]
  (files-by-filter base-path folder #(is-xml? (.getPath %))))

(defn files-from-dir
  [base-path dir]
  (files-by-folder base-path dir))

(defn specific-file-from-dir [base-path folder name]
  (let [pattern (re-pattern name)
        method #(re-find pattern (.getPath %))]
    (files-by-filter base-path folder method)))

(defn group-by-tags
  [t]
  (group-by :tag (:content t)))

(defn stat-to-map
  "Turns {:tag :name, :content [Ancient Gold Dragon]} into {:name 'Ancient Gold Dragon'}"
  ;; TODO: need to fix this:
  ;; Stat to map:  {:tag :modifier, :attrs {:category ability score}, :content [charisma +1]}
  ;; new map:  {:modifier charisma +1}
  [stat]
  ;; (println "Stat to map: " stat)
  (let [n (stat :tag)
        content (first (stat :content))]
    ;; (println "new map: "  {n content} "\n\n")
    {n content}))

(defn fix-tag
  [action]
  (let [content (:content action)
        n (first (:content (first content)))
        text (first (:content (second content)))]
    (if (and (nil? n) (nil? text))
      (if (nil? content)
        "\n"
        content)
      {:name n :text text})))

(defn needs-better-name
  ""
  [thing]
  (let [name (first thing)
        tags (second thing)]
    (if (= (count tags) 1)
      (stat-to-map (first tags))
      {name (into [] (map fix-tag tags))})))

(defn also-needs-name
  ""
  [things]

  (loop [list things
         result []]
    (let [d (into {} (first list))
          r (rest list)]
      (if (> (count r) 0)
        (recur r (conj result d))
        (conj result (into {} (first list)))))))

(defn load-from-file
  ""
  [file]
  (also-needs-name
   (map #(map needs-better-name (group-by-tags %))
        (:content (first (zip-str (slurp (data-file file))))))))

(defn load-all-things
  [files]
  (loop [files files
         results []]
    (let [f (first files)
          r (rest files)]
      (if (> (count r) 0)
        (recur r (into results (load-from-file (.getPath f))))
        (into results (load-from-file (.getPath f)))))))

(defn not-re-find-name?
  ""
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
