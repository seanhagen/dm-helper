(ns dm-helper.files.monster
  (:gen-class)
  (:require
   [dm-helper.files.utils :as futil]
   ))

(defn stat-to-map
  "Turns {:tag :name, :content [Ancient Gold Dragon]} into {:name 'Ancient Gold Dragon'}"
  [stat]
  {(stat :tag) (first (stat :content))})

(defn monster
  [m]
  (group-by :tag (:content m)))

(defn fix-tag
  [action]
  (let [content (:content action)
        name (first (:content (first content)))
        text (first (:content (second content)))]
    {:name name :text text}))

(defn needs-better-name
  ""
  [thing]
  (let [name (first thing)
        tags (second thing)]
    (if (= (count tags) 1)
      (stat-to-map (first tags))
      {name (into [] (map fix-tag tags))})))

(defn not-re-find-name?
  ""
  [thing pattern]
  (not
   (nil?
    (re-find pattern (:name thing)))))

(defn filter-creature
  "Find a creature in the list"
  [monsters looking-for]
  (let [pattern (re-pattern looking-for)]
    (into []
          (filter
           #(not-re-find-name? % pattern)
           monsters))))

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

(defn load-monsters-from-file
  ""
  [file]
  (also-needs-name
   (map #(map needs-better-name (monster %))
        (:content (first (futil/zip-str (slurp (futil/data-file file))))))))

(defn load-all-monsters
  [base-path]

  ;; (map #(-> % (.getPath)) (futil/monster-files base-path))

  (loop [files (futil/monster-files base-path)
         results []]

    (let [f (first files)
          r (rest files)]
      (if (> (count r) 0)
        (recur r (conj results (.getPath f)))
        (conj results (.getPath f))
        )

      )

    )

  ;; (loop [files (futil/monster-files base-path)
  ;;        results []]
  ;;   (let [f (first files)
  ;;         r (rest files)]
  ;;     (if (> (count r) 0)
  ;;       (recur r (into results (load-monsters-from-file f)))
  ;;       (conj results (load-monsters-from-file f))
  ;;       )
  ;;     )
  ;;   )

  ;; ;; old!
  ;; (let [monsters []]
  ;;   (map #(into monsters (load-monsters-from-file %)) (futil/monster-files base-path)))


  )
