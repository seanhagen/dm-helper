(ns dm-helper.core
  (:gen-class)
  (:use [seesaw.font]
        [clojure.pprint])
  (:import [org.apache.commons.io.FilenameUtils])
  (:require [dm-helper.files.books :as books]
            [dm-helper.files.utils :as util]
            [dm-helper.gui.items :as gui-ref]
            [dm-helper.gui.frame :as gui-frame]
            [seesaw.core :as sc]
            [seesaw.border :as sb]
            [seesaw.event :as se]
            [seesaw.chooser :as seec]
            [seesaw.bind :as sbind]

            [clojure.java.io :as io]
            ))

(def info-ref (ref {:monsters [] :spells [] :races [] :classes [] :feats [] :backgrounds [] :items []}))

(defn main-app []
  (books/load-saved-info info-ref)
  (let [tab1 {:title "Reference" :tip "Reference from all the books" :content (gui-ref/reference-panel info-ref)}
        tabs (sc/tabbed-panel :placement :top :overflow :scroll :tabs [tab1])
        main-display (sc/border-panel :center tabs :preferred-size [1500 :by 900])
        main-frame (gui-frame/build-main-frame info-ref)]
    (sc/config! main-frame :content main-display)
    (-> main-frame sc/pack! sc/show!)))

(defn -main
  "LEEEARNING"
  [& args]
  ;; (books/load-saved-info)

  ;; (println (System/getProperty "java.runtime.version"))

  ;; (def data (books/load-all-from-dir "/home/sean/Dropbox/D&D App Files"))
  ;; (spit "info.clj" data)
  ;; (println "keys: " (keys data))

  ;; (println "User home directory is: " books/app-dir)

  (sc/native!)
  (main-app)


  ;; (let [xml (books/xml-from-file (io/resource "test.xml"))
  ;;       parts (group-by :tag (:content (first xml)))
  ;;       result (into {} (map #(books/parse-parts {} %) parts))]
  ;;   (pprint result))

  )
