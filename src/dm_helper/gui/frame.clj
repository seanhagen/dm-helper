(ns dm-helper.gui.frame
  (:gen-class)
  (:require [seesaw.core :as sc]
            [seesaw.event :as se]
            [seesaw.bind :as sbind]))


(defn menu-handler [event]
  (println "Menu event: " event))


;; (def path-ref (ref ""))
;; (def path-show (sc/text :editable? false :columns 20))
;; (def launch-file-selector (fn [e]
;;                             (seec/choose-file :selection-mode :dirs-only
;;                                               :success-fn (fn [f,e]
;;                                                             (let [path (fn [old] (.getPath e))]
;;                                                               (dosync
;;                                                                (alter path-ref path)))))))

;; (def file-dialog (sc/button
;;                   :text "Select Directory"
;;                   :listen [:action launch-file-selector]))

;; (sbind/bind path-ref (sbind/property path-show :text))

;; (def argh
;;   (sc/dialog :content (sc/horizontal-panel :items ["Path to D&D App Files: " path-show file-dialog])
;;              :modal? true
;;              :success-fn (fn [opane]
;;                            (main-app @path-ref))))
;; (-> argh sc/pack! sc/show!)

(defn load-from-files [e]

  ;;load all data from xml files within path: [info (books/load-all-from-dir path)]
  )


(def import-xml   (sc/menu-item :text "Import XML" :listen [:action menu-handler]))
(def quit-program (sc/menu-item :text "Quit"       :listen [:action menu-handler]))

(def main-menu (sc/menu :text "File" :items [import-xml quit-program]))

(def mbar (sc/menubar :items [main-menu]))

(defn build-main-frame
  []
  (sc/frame :title "D&D - DM Helper"
            :on-close :exit
            :menubar mbar))
