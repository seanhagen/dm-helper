(ns dm-helper.gui.frame
  (:gen-class)
  (:require [dm-helper.files.books :as books]
            [dm-helper.files.utils :as util]
            [seesaw.core :as sc]
            [seesaw.event :as se]
            [seesaw.chooser :as seec]
            [seesaw.bind :as sbind]))

(defn menu-handler [event]
  (println "Menu event: " event))

(def path-ref (ref ""))
(def path-show (sc/text :editable? false :columns 20))
(sbind/bind path-ref (sbind/property path-show :text))

(def launch-file-selector
  (fn [e]
    (seec/choose-file :selection-mode :dirs-only
                      :success-fn (fn [f,e]
                                    (let [path (fn [old] (.getPath e))]
                                      (dosync
                                       (alter path-ref path)))))))

(def file-dialog (sc/button
                  :text "Select Directory"
                  :listen [:action launch-file-selector]))

(defn show-load-files-dialog [e items-ref]
  (sc/show!
   (sc/pack!
    (sc/dialog :content (sc/horizontal-panel
                         :items ["Path to D&D App Files: " path-show file-dialog])
               :modal? true
               :success-fn (fn [e]
                             (let [info (books/parse-from-directory @path-ref)
                                   fixed (util/fix-info-keys info)]
                               (books/save-info-to-file fixed)
                               (dosync
                                (ref-set items-ref fixed))))))))

(defn build-main-frame
  [items-ref]

  (def import-xml   (sc/menu-item :text "Import XML"
                                  :listen [:action (fn [e] (show-load-files-dialog e items-ref))]))
  (def quit-program (sc/menu-item :text "Quit"       :listen [:action menu-handler]))
  (def main-menu (sc/menu :text "File" :items [import-xml quit-program]))
  (def mbar (sc/menubar :items [main-menu]))

  (sc/frame :title "D&D - DM Helper"
            :on-close :exit
            :menubar mbar))
