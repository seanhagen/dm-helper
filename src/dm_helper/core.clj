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

  (time
   (let [path (first args)
         monsters (books/load-all-monsters path)
         spells (books/load-all-spells path)
         ;; arcana (books/load-all-unearthed path)
         races (books/load-all-races path)
         classes-and-spells (books/load-all-classes path)
         spells (into spells (:spells classes-and-spells))
         classes (:classes classes-and-spells)
         feats (books/load-all-feats path)
         backgrounds (books/load-all-backgrounds path)
         ]

     (println "monsters: " (first monsters) "\n\n")
     (println "spells: " (first spells) "\n\n")
     ;; (println (first arcana) "\n\n")
     (println "races: " (first races) "\n\n")
     (println "classes: " (first classes) "\n\n")
     (println "feats: " (first feats) "\n\n")
     (println "backgrounds: " (first backgrounds) "\n\n")
     )))
