(ns dm-helper.core
  (:gen-class)
  (:use [seesaw.font]
        [clojure.pprint])
  (:import [org.apache.commons.io.FilenameUtils])
  (:require [dm-helper.files.books :as books]
            [dm-helper.gui.frame :as gui-frame]
            [dm-helper.gui.reference :as gui-ref]
            [seesaw.core :as sc]
            [clojure.java.io :as io]))

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
  ;; (println "User home directory is: " books/app-dir)

  (sc/native!)
  (main-app)

  ;; (let [xml (books/xml-from-file (io/resource "test.xml"))
  ;;       parts (group-by :tag (:content (first xml)))]
  ;;   (println "\n#################\n\t\tSPELL\n#################\n")
  ;;   (pprint (parse-all (:spell parts))))


  ;; (let [things (books/parse-from-directory "/home/sean/Dropbox/D&D App Files")]
  ;;   (doall
  ;;    (map
  ;;     (fn [e]
  ;;       (let [part (first e)
  ;;             items (second e)]
  ;;         (println part " - " (count items))

  ;;         ;; (if (= part :race)
  ;;         ;;   (doall (map (fn [e] (pprint (:name e))) items)))

  ;;         )) things)))

  ;; (pprint (keys @info-ref))
  )
