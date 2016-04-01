(ns dm-helper.files.books
  (:gen-class)
  (:require
   [dm-helper.files.utils :as util]))

(defn load-all-spells
  ""
  [base-path]
  (util/load-all-things (util/files-from-dir base-path "Bestiary")))

(defn load-all-monsters
  [base-path]
  (util/load-all-things (util/files-from-dir base-path "Spells")))

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

;; (defn argh
;;   [content]

;;   (map
;;    #(let [parts (group-by :tag (:content %))]
;;       {:name (:content (first (:name parts)))
;;        :text ""
;;        :optional false
;;        :modifier {}}
;;       )
;;    content)
;;   )

(defn parse-slots [arg]
  {:slots (first (:content arg))}
  )

(defn parse-feature [arg]
  (let [f (into {} (map util/needs-better-name (group-by :tag (:content arg))))]
    ;; need to include optional thingy here
    (into f {:text (into [] (map first (:text f)))}))
  )

(defn fixing [arg]
  (if (= (count (:content arg)) 1)
    (parse-slots arg)
    (parse-feature arg)
    )
  )

(defn group-autolevel [arg]
  (group-by
   #(get-in % [:attrs :level])
   (:autolevel (group-by :tag (:content arg)))))

(defn argh [d]
  (println "argh: " (first (:content (first (get (group-autolevel d) "2")))))
  (println "fixed: " (fixing (first (:content (first (get (group-autolevel d) "2"))))))

  (println "argh: " (first (:content (last (get (group-autolevel d) "2")))))
  (println "fixed: " (fixing (first (:content (last (get (group-autolevel d) "2"))))))

  (println "\n\n--------------------\n\n")

  (println

   (map fixing
        (map #(first (:content %))
             (get (group-autolevel d) "2")))

   )


  (map
   #(first (:content %))

   (get (group-autolevel d) "1")

   )
  )

(defn fix-autolevels
  [als]

  ;; (println "autolevels: " als)

  ;; (let [parts (group-by #(get-in % [:attrs :level]) als)]
  ;;   (last (:content (first (get parts 2))))
  ;;   )

  als
  )

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
            results (conj results (into class (fix-autolevels autolevel)))]

        (if (> (count classes) 0)
          (recur (rest classes) results)
          {:spells spells
           :classes results}
          )
        )
      )

    (:class parts)
    ))
