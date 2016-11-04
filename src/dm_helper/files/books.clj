(ns dm-helper.files.books
  (:gen-class)
  (:require [clojure.java.io :as io]
            [dm-helper.files.utils :as util]
            [clojure.string :as string]))

(defn xml-from-file [file]
  (util/zip-str
   (slurp (util/data-file (.getPath file)))))

(defn load-saved-info [info-ref]
  (util/load-saved-ref info-ref "info.clj"))

(defn save-info-to-file [info]
  (util/save-ref-to-file info "info.clj"))

(defn sections-from-data [info]
  (map (fn [e] (string/capitalize (name e))) (keys info)))

(defn new-name-needed [e]
  (map (fn [f]
         (into f {:attrs (into (if (nil? (:attrs f)) {} (:attrs f)) (get-in e [:attrs]))}))
       (:content e)))

(defn parser [thing]
  (let [name (first thing)
        tags (second thing)
        content (map :content tags)
        types (distinct (map type (flatten content)))
        is-string (first (distinct (map string? (flatten content))))]
    (if is-string
      {name (string/join (map #(string/join (if (nil? %) "\n" %)) (flatten content)))}
      {name (into [] (map #(into {} (map
                                     (fn [h] (let [name (:tag h)
                                                   attr (:attrs h)
                                                   content (first (:content h))]
                                               {name content :attrs attr}))) %)
                          (map new-name-needed tags)))})))

(defn parse-all [things]
  (distinct (into []
                  (doall (map
                          (fn [e] (into {} (map parser (dissoc (group-by :tag e) nil))))
                          (map (fn [e] (-> e :content )) things))))))

(defn remove-duplicates [results]
  (into {}
        (map
         (fn [e] (let [name (first e)
                       bits (distinct (second e))]
                   [name bits]))
         results)))

(defn parse-file [result file]
  (println (str "Parsing file '" (.getPath file) "' for data"))
  (remove-duplicates
   (try
     (let [xml (xml-from-file file)
           parts (group-by :tag (:content (first xml)))]
       (merge-with concat
                   result
                   (into {}
                         (map
                          (fn [e]
                            (let [key (first e)
                                  parts (second e)]
                              {key (parse-all parts)}))
                          parts))))
     (catch Exception e
       (do
         (println "Exception parsing xml file:" file "\n\t" (.getMessage e))
         result)))))

(defn parse-from-directory
  [path]
  (let [files (util/all-xml-from-dir path)]
    (loop [files files
           result {}]
      (if (> (count files) 0)
        (recur (rest files) (parse-file result (first files)))
        result))))
