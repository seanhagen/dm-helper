(ns dm-helper.files.books
  (:gen-class)
  (:require
   [dm-helper.files.utils :as util]))

(defn load-all-spells
  ""
  [base-path]
  (util/load-all-things (util/files-from-dir base-path "Spells")))

(defn load-all-monsters
  [base-path]
  (util/load-all-things (util/files-from-dir base-path "Bestiary")))

(defn load-all-items
  [base-path]
  (util/load-all-things (util/files-from-dir base-path "Items")))

(defn load-all-unearthed
  [base-path]
  (util/load-all-things (util/files-from-dir base-path "Unearthed Arcana")))

(defn load-all-from-character-files
  [base-path t]
  (util/load-all-things (util/specific-file-from-dir base-path "Character Files" t))
  )

(defn load-all-feats
  [base-path]
  (load-all-from-character-files base-path "Feats"))

(defn load-all-races
  [base-path]
  (load-all-from-character-files base-path "Races"))

(defn load-all-backgrounds
  [base-path]
  (load-all-from-character-files base-path "Backgrounds"))

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

(defn load-all-classes
  [base-path]
  (let [files  (util/specific-file-from-dir base-path "Character Files" "Class")
        xml    (util/zip-str
                (slurp (util/data-file (.getPath (first files)))))
        parts  (group-by :tag (:content (first xml)))
        spells (into [] (map #(into {} %) (map #(map util/needs-better-name (util/group-by-tags %)) (:spell parts))))]
    (loop [classes (:class parts)
           results []]
      (let [class (group-by :tag (:content (first classes)))
            autolevel (:autolevel class)
            class (into {} (map util/needs-better-name (dissoc class :autolevel)))
            al (fix-autolevels autolevel)
            t (into class {:levels al})
            results (conj results t)]

        (if (> (count classes) 0)
          (recur (rest classes) results)
          {
           :spells spells
           :classes (drop-last results)
           }
          )
        )
      )

    ))
