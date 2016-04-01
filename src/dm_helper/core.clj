(ns dm-helper.core
  (:gen-class)
  ;; [org.apache.commons.io.FilenameUtils :as FilenameUtils]
  (:import [org.apache.commons.io.FilenameUtils])
  (:require [dm-helper.files.books :as books]
            [dm-helper.files.utils :as futil]
            [seesaw.core :as sc]))

(defn -main
  "LEEEARNING"
  [& args]

  ;; (let [monsters (file-monster/load-all-monsters (first args))]
  ;;   (loop [monsters (file-monster/filter-creature monsters "Dragon")]
  ;;     (println (:name (first monsters)))
  ;;     (if (> (count monsters) 0)
  ;;       (recur (rest monsters)))))

  (let [path (first args)
        ;; monsters (books/load-all-monsters path)
        ;; spells (books/load-all-spells path)
        ;; arcana (books/load-all-unearthed path)
        ;; races (books/load-all-races path)
        classes (books/load-all-classes path)
        ;; feats (books/load-all-feats path)
        ;; backgrounds (books/load-all-backgrounds path)
        ]
    ;; (println (first monsters) "\n\n")
    ;; (println (first spells) "\n\n")
    ;; (println (first feats) "\n\n")
    ;; (println (first races) "\n\n")
    ;;(println (first classes) "\n\n")
    ;; (let [barbarian (first classes)]
    ;;   ;; (println "content?: " (:content barbarian))
    ;;   ;; (println "argh: " (first barbarian))
    ;;   )
    ;; (println (first feats) "\n\n")
    ;; (println (first backgrounds) "\n\n")

    (println classes)

    ))
