(ns dm-helper.core
  (:gen-class)
  (:use [seesaw.font])
  (:import [org.apache.commons.io.FilenameUtils])
  (:require [dm-helper.files.books :as books]
            [dm-helper.files.utils :as futil]
            [dm-helper.gui.items :as gui-ref]
            [dm-helper.gui.frame :as gui-frame]
            [seesaw.core :as sc]
            [seesaw.border :as sb]
            [seesaw.event :as se]
            [seesaw.chooser :as seec]
            [seesaw.bind :as sbind]))

(defn display [in content]
  (sc/config! in :content content)
  content)

(defn main-app [path]
  (let [monsters (books/load-all-monsters path)
        spells (books/load-all-spells path)
        ;; arcana (books/load-all-unearthed path)
        races (books/load-all-races path)
        classes-and-spells (books/load-all-classes path)
        spells (into spells (:spells classes-and-spells))
        classes (:classes classes-and-spells)
        feats (books/load-all-feats path)
        backgrounds (books/load-all-backgrounds path)
        info {:monsters monsters
              :spells spells
              :races races
              :classes classes
              :feats feats
              :backgrounds backgrounds}]

    ;; (println "monsters: " (first monsters) "\n\n")
    ;; (println "spells: " (first spells) "\n\n")
    ;; (println (first arcana) "\n\n")
    ;; (println "races: " (first races) "\n\n")
    ;; (println "classes: " classes "\n\n")
    ;; (println "feats: " (first feats) "\n\n")
    ;; (println "backgrounds: " (first backgrounds) "\n\n")

    (let [tab1 {:title "Reference" :tip "Reference from all the books" :content (gui-ref/items-panel info)}
          tabs (sc/tabbed-panel :placement :top :overflow :scroll :tabs [tab1])
          main-display (sc/border-panel :center tabs :preferred-size [1500 :by 900])
          main-frame (gui-frame/build-main-frame)]
      (display main-frame main-display)
      (-> main-frame sc/pack! sc/show!))

    ))

(defn -main
  "LEEEARNING"
  [& args]

  (sc/native!)

  (def path-ref (ref ""))
  (def path-show (sc/text :editable? false :columns 20))
  (def launch-file-selector (fn [e]
                              (seec/choose-file :selection-mode :dirs-only
                                                :success-fn (fn [f,e]
                                                              (let [path (fn [old] (.getPath e))]
                                                                (dosync
                                                                 (alter path-ref path)))))))

  (def file-dialog (sc/button
                    :text "Select Directory"
                    :listen [:action launch-file-selector]))

  (sbind/bind path-ref (sbind/property path-show :text))

  (def argh
    (sc/dialog :content (sc/horizontal-panel :items ["Path to D&D App Files: " path-show file-dialog])
               :modal? true
               :success-fn (fn [opane]
                             (main-app @path-ref))))
  (-> argh sc/pack! sc/show!)


  )
