(ns dm-helper.core
  (:gen-class)
  ;; [org.apache.commons.io.FilenameUtils :as FilenameUtils]
  (:import [org.apache.commons.io.FilenameUtils])
  (:require [dm-helper.files.monster :as file-monster]
            [dm-helper.files.spell :as file-spell]
            [dm-helper.files.utils :as futil]
            [seesaw.core :as sc]))

(defn -main
  "LEEEARNING"
  [& args]

  (let [monsters (file-monster/load-all-monsters (first args))
        m (file-monster/load-monsters-from-file (first monsters))]

    ;; (loop [files monsters]
    ;;   (let [])
    ;;   )

    (println "monsters: " (first monsters))

    (println "what: " (count m))
    )

  ;; (let [files (futil/monster-files (first args))]
  ;;   ;;(println "extensions?" (map .getExtension files) "\n\n")
  ;;   (println "files: " (map #(FilenameUtils/getExtension %) (map  #(-> (.getPath %)) files)))
  ;;   )


  ;; (let [arg-file (first args)
  ;;       spells (file-spell/load-spells-from-file arg-file)]
  ;;   (printf "first spell" (first spells))
  ;;   )

  ;; (sc/native!)

  ;; (println "Starting...")

  ;; (sc/invoke-later
  ;;  (-> (sc/frame :title "Hello"
  ;;                :content "Hello Seesaw!"
  ;;                :on-close :exit)
  ;;      sc/pack!
  ;;      sc/show!))

  ;; (time
  ;;  (let [arg-file (first args)
  ;;        monsters (file-monster/load-monsters-from-file arg-file)]

  ;;    (loop [list (file-monster/filter-creature monsters "Dragon")]
  ;;      (let [m (first list)
  ;;            r (rest list)]
  ;;        (println "Creature: " (:name m))
  ;;        (if (> (count r) 0)
  ;;          (recur r))
  ;;        )
  ;;      )
  ;;    )))

  )

(def f "test.xml")
(def d (:content (first (futil/zip-str (slurp (futil/data-file f))))))
(def bits (group-by :tag (:content (second d))))
