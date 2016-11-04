(ns dm-helper.gui.project
  (:gen-class)
  (:require [seesaw.core :as sc]
            [seesaw.bind :as sbind]
            [dm-helper.files.books :as books]
            [dm-helper.files.utils :as util]
            [dm-helper.files.projects :as proj]))

(def projects-ref (ref {:projects []}))

(defn load-project [e])
(defn new-project [e])
(defn delete-project [e])

(defn build-project-chooser [projects-ref]
  (let [proj-list (sc/listbox)
        scrollable (sc/scrollable proj-list)
        load-proj (sc/button :text "Load Selected" :listen [:action load-project])
        new-proj (sc/button :text "New Campaign" :listen [:action new-project])
        delete-proj (sc/button :text "Delete Selected" :listen [:action delete-project])
        button-list (sc/vertical-panel :items [load-proj new-proj delete-proj])
        ]

    (-> proj-list (.setFixedCellWidth 200))

    (sc/horizontal-panel
     :items [scrollable button-list])
    )
  )

(defn show-project-chooser []
  (proj/load-projects projects-ref)
  (sc/show!
   ;;(sc/pack!)
   (sc/frame
    :title "D&D - DM Helper - Load Campaign"
    :on-close :hide
    :width 500
    :height 200
    :content (build-project-chooser projects-ref))))
