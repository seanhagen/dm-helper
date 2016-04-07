(ns dm-helper.core
  (:gen-class)
  (:use [seesaw.font])
  (:import [org.apache.commons.io.FilenameUtils])
  (:require [dm-helper.files.books :as books]
            [dm-helper.files.utils :as util]
            [dm-helper.gui.items :as gui-ref]
            [dm-helper.gui.frame :as gui-frame]
            [seesaw.core :as sc]
            [seesaw.border :as sb]
            [seesaw.event :as se]
            [seesaw.chooser :as seec]
            [seesaw.bind :as sbind]))

(defn main-app []
  (let [info (books/load-saved-info)
        ;; monsters (books/load-all-monsters path)
        ;; spells (books/load-all-spells path)
        ;; ;; arcana (books/load-all-unearthed path)
        ;; races (books/load-all-races path)
        ;; classes-and-spells (books/load-all-classes path)
        ;; spells (into spells (:spells classes-and-spells))
        ;; classes (:classes classes-and-spells)
        ;; feats (books/load-all-feats path)
        ;; backgrounds (books/load-all-backgrounds path)
        ;; info {:monsters monsters
        ;;       :spells spells
        ;;       :races races
        ;;       :classes classes
        ;;       :feats feats
        ;;       :backgrounds backgrounds}
        ]

    ;; (println "monsters: " (first monsters) "\n\n")
    ;; (println "spells: " (first spells) "\n\n")
    ;; (println (first arcana) "\n\n")
    ;; (println "races: " (first races) "\n\n")
    ;; (println "classes: " classes "\n\n")
    ;; (println "feats: " (first feats) "\n\n")
    ;; (println "backgrounds: " (first backgrounds) "\n\n")

    ;; (spit "info.clj" info)

    (let [tab1 {:title "Reference" :tip "Reference from all the books" :content (gui-ref/reference-panel info)}
          tabs (sc/tabbed-panel :placement :top :overflow :scroll :tabs [tab1])
          main-display (sc/border-panel :center tabs :preferred-size [1500 :by 900])
          main-frame (gui-frame/build-main-frame)]
      (sc/config! main-frame :content main-display)
      (-> main-frame sc/pack! sc/show!))
    ))

(defn -main
  "LEEEARNING"
  [& args]
  ;; (books/load-saved-info)

  ;; (println (System/getProperty "java.runtime.version"))

  ;; (def data (books/load-all-from-dir "/home/sean/Dropbox/D&D App Files"))
  ;; (spit "info.clj" data)
  ;; (println "keys: " (keys data))

  (println "User home directory is: " books/app-dir)

  (sc/native!)
  (main-app)

  )
