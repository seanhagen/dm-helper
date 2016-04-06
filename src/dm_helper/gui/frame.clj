(ns dm-helper.gui.frame
  (:gen-class)
  (:require [seesaw.core :as sc]
            [seesaw.event :as se]
            [seesaw.bind :as sbind]))


(def main-menu (sc/menu :items [
                                (sc/menu-item :text "Import XML")
                                (sc/menu-item :text "Quit")
                                ]))

(def mbar (sc/menubar :items [main-menu]))

(defn build-main-frame
  []
  (sc/frame :title "D&D - DM Helper"
            :on-close :exit
            :menubar mbar))
