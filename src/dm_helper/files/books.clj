(ns dm-helper.files.books
  (:gen-class)
  (:require [clojure.java.io :as io]
            [dm-helper.files.utils :as util]
            [inflections.core :as infl]
            [clojure.string :as s]))

(defn parse-slots [arg]
  {:slots (first (:content arg))})

(defn fix-feature-text
  [feature text]
  (if (= (type text) java.lang.String)
    (into feature {:text [text]})
    (into feature {:text (into [] (map first (:text feature)))})))

(defn parse-feature [arg]
  (let [f (into {} (map util/needs-better-name (group-by :tag (:content arg))))
        t (:text f)]
    (if (not (nil? (:attrs arg)))
      (into (fix-feature-text f t) {:optional true})
      (fix-feature-text f t))))

(defn fixing [arg]
  (if (= (count (:content arg)) 1)
    (parse-slots arg)
    (parse-feature arg)))

(defn group-autolevel [arg]
  (group-by #(get-in % [:attrs :level]) arg))

(defn autolevel-into-map [bit]
  (let [index (first bit)
        autolevel (:content (first (last bit)))
        fixed (map fixing autolevel)]
    {index (into [] fixed)}))

(defn fix-autolevels
  [als]
  (let [grouped (group-autolevel als)]
    (into [] (map autolevel-into-map grouped))))

(defn parse-classes
  [info]
  (loop [classes info
         results []]
    (let [class (group-by :tag (:content (first classes)))
          autolevel (:autolevel class)
          class (into {} (map util/needs-better-name (dissoc class :autolevel)))
          al (fix-autolevels autolevel)
          t (into class {:levels al})
          results (conj results t)]
      (if (> (count classes) 0)
        (recur (rest classes) results)
        (drop-last results)))))

(defn parse-other
  [info]
  (into [] (map #(into {} %) (map #(map util/needs-better-name (util/group-by-tags %)) info))))

(defn xml-from-file [file]
  (util/zip-str
   (slurp (util/data-file (.getPath file)))))

(defn proper-parser-for [key parts]
  (if (= key :classes)
    (parse-classes parts)
    (parse-other parts)))

(defn parse-parts [result part]
  (let [key (infl/plural (str (first part)))
        key (keyword (s/join (rest key)))
        bits (last part)]
    (into result
          (if (contains? result key)
            {key (into [] (distinct (into (result key) (proper-parser-for key bits))))}
            {key (into [] (distinct (proper-parser-for key bits)))}))))

(defn load-all-from-dir
  [base-path]
  (let [files (util/all-xml-from-dir base-path)]

    (loop [files files
           result {}]
      ;;(println "files count: " (count files))
      (if (> (count files) 0)
        (let [xml (xml-from-file (first files))
              parts (group-by :tag (:content (first xml)))
              result (into result (map #(parse-parts result %) parts))]
          (recur (rest files) result)
          ;; (if (> (count files) 0)
          ;;   (recur (rest files) result)
          ;;   result)
          )
        result
        )
      )
    )
  )

(def app-dir (str (System/getProperty "user.home") "/.dm-helper"))

(defn fix-pair [pair]
  (let [key (first pair)
        data (last pair)
        fixed (keyword (s/join (rest key)))]
    {fixed data}))

(defn load-saved-info [info-ref]
  (let [saved (str app-dir "/info.clj")]
    (if (.exists (io/as-file saved))
      (let [info (read-string (slurp saved))]
        (dosync
         (alter info-ref into info))))))

(defn save-info-to-file [info]
  (let [saved (str app-dir "/info.clj")]
    (.mkdir (java.io.File. app-dir))
    (spit saved info)))

(defn sections-from-data [info]
  (map (fn [e] (s/capitalize (name e))) (keys info)))
