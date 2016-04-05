(ns dm-helper.core
  (:gen-class)
  (:use [seesaw.font])
  (:import [org.apache.commons.io.FilenameUtils])
  (:require [dm-helper.files.books :as books]
            [dm-helper.files.utils :as futil]
            [dm-helper.gui.spells :as gui-spells]
            [seesaw.core :as sc]
            [seesaw.border :as sb]
            [seesaw.event :as se]
            [seesaw.chooser :as seec]
            [seesaw.bind :as sbind]))

(def f (sc/frame :title "D&D 5E - DM Helper"
                 ;; :width 800
                 ;; :height 700
                 ;; :visible? true
                 :on-close :exit))

(defn display [in content]
  (sc/config! in :content content)
  content)


(defn -main
  "LEEEARNING"
  [& args]

  (sc/native!)

  (def path-ref (ref ""))
  (def path-show (sc/text :editable? false))
  (def file-dialog (sc/button
                    :text "Select Directory"
                    ;; :listen [:action (fn []
                    ;;                    (seec/choose-file :selection-mode :dirs-only
                    ;;                                      :success-fn (fn [f] (.getPath f))))
                    ;;          ]
                    :listen [:action #(sc/alert "NEXT")]
                    ))

  (sbind/bind path-ref (sbind/property path-show :text))

  (sc/dialog :content
             (sc/flow-panel :items ["Path to D&D App Files: " path-show file-dialog])
             )

  ;;(println "chose file: " (.getPath (seec/choose-file :selection-mode :dirs-only)))

  ;; (let [path (first args)
  ;;       monsters (books/load-all-monsters path)
  ;;       spells (books/load-all-spells path)
  ;;       ;; arcana (books/load-all-unearthed path)
  ;;       races (books/load-all-races path)
  ;;       classes-and-spells (books/load-all-classes path)
  ;;       spells (into spells (:spells classes-and-spells))
  ;;       classes (:classes classes-and-spells)
  ;;       feats (books/load-all-feats path)
  ;;       backgrounds (books/load-all-backgrounds path)
  ;;       ]

  ;;   ;; (println "monsters: " (first monsters) "\n\n")
  ;;   ;; (println "spells: " (first spells) "\n\n")
  ;;   ;; (println (first arcana) "\n\n")
  ;;   ;; (println "races: " (first races) "\n\n")
  ;;   ;; (println "classes: " (first classes) "\n\n")
  ;;   ;; (println "feats: " (first feats) "\n\n")
  ;;   ;; (println "backgrounds: " (first backgrounds) "\n\n")

  ;;   (let [tab1 {:title "Spells" :tip "All the spells" :content (gui-spells/spells-panel spells)}
  ;;         tab2 {:title "Feats" :tip "Buncha feats" :content "FEAAAATS"}
  ;;         tab3 {:title "Backgrounds" :tip "So many backgrounds" :content "ALLLLL THE BACKGROUNDS"}
  ;;         tab4 {:title "Monsters" :tip "So nasty" :content "RAR!"}
  ;;         tab5 {:title "Races" :tip "So racy" :content "All dem races"}
  ;;         tabs (sc/tabbed-panel :placement :top :overflow :scroll :tabs [tab1 tab2 tab3 tab4 tab5])
  ;;         main-display (sc/border-panel :center tabs :preferred-size [1500 :by 900])]

  ;;     (display f main-display)
  ;;     (-> f sc/pack! sc/show!)))

  )
