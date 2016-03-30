(ns dm-helper.core
  (:gen-class)
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

(defn get-name
  "WHEEE"
  [thing]
  (-> thing :content first :content first))

;; (defn get-stat
;;   "Fetch stat"
;;   [thing stat]

;;   (let [attr (filter (fn [x] (= (:tag x) stat)) (:content thing))]
;;     (-> attr first :content first))
;;   ;; (println "Got thing: " thing)
;;   ;; (println "Got stat: " stat)
;;   ;; 1
;;   )

;; (defn stats
;;   "Show basic stats"
;;   [thing]
;;   (let [name (get-name thing)
;;         strength (get-stat thing :str)
;;         dex (get-stat thing :dex)
;;         con (get-stat thing :con)
;;         intel (get-stat thing :int)
;;         wis (get-stat thing :wis)
;;         cha (get-stat thing :cha)
;;         cr (get-stat thing :cr)]
;;     (printf "Name: %s (%s) STR: %s DEX: %s CON: %s INT: %s WIS: %s CHA: %s\n" name cr strength dex con intel wis cha)
;;     ))

(defn stat-to-map
  "Turns {:tag :name, :content [Ancient Gold Dragon]} into {:name 'Ancient Gold Dragon'}"
  [stat]
  {(stat :tag) (first (stat :content))})

(def f "test.xml")
(def d (:content (first (zip-str (slurp (data-file f))))))
(def bits (group-by
           :tag
           (:content
            (second d)
            )
           ))

(defn monster
  [m]
  (group-by :tag (:content m)))

(defn nth-monster
  [i]
  (monster (nth d i)))

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
      {name (into [] (map fix-tag tags))}
      )))

(defn filter-creature
  "Find a creature in the list"
  [monsters looking-for]

  (println "Trying to find: " looking-for)

  (let [pattern (re-pattern looking-for)]

    (into []
          (filter
           #(not
             (nil?
              (do
                (println "Does '" (:name %) "' match? => " (not (nil? (re-find pattern (:name % )))))
                (re-find pattern (:name %)))))
           monsters))
    )
  )

(defn -main
  "LEEEARNING"
  [& args]
  (println "Starting...")

  (let [arg-file (first args)
        file (if (nil? arg-file) "test.xml" arg-file)
        data (:content (first (zip-str (slurp (data-file file)))))
        monsters (map #(map needs-better-name (monster %)) data)]

    ;; (println (group-by :tag (:content (last data))))
    ;; (println (transform-stats (:content (last data))))

    (def fixed-monsters
      (loop [list data
             result []]
        (let [d (first list)
              r (rest list)
              m (into {} (map needs-better-name (monster d)))]
          (conj result m)
          (if (> (count r) 1)
            (recur r result)
            result)
          )))

    (println "Fixed monsters: " fixed-monsters)

    (loop [list fixed-monsters]
      (let [m (first list)
            r (rest list)]
        (println "Monster: " (:name m))
        (if (> (count r) 1)
          (recur r))
        )
      )

    ;; (println "Filtered for 'Ancient':\n" (filter-creature fixed-monsters "Ancient"))

    ;; (loop [list (filter-creature fixed-monsters "Ancient")]
    ;;   (let [m (first list)
    ;;         r (rest list)]

    ;;     (println "Thing: " (:name m))

    ;;     (if (> (count r) 1)
    ;;       (recur r)))
    ;;   )

    ;; (println (first monsters))

    ;;(dorun (map stats data))
    ))
