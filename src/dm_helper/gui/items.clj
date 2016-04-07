(ns dm-helper.gui.items
  (:gen-class)
  (:require [dm-helper.files.books :as book]
            [seesaw.core :as sc]
            [seesaw.border :as sb]
            [seesaw.event :as se]
            [seesaw.bind :as sbind]
            [clojure.string :as s]))


(defn display-item-in-panel
  [selected items panel]
  (let [current (first (first (sc/config panel :items)))
        new (sc/label selected)]
    (sc/replace! panel current new)))

(def items-ref (ref []))

(defn find-matching-items [current info term]
  (sort
   (map :name
        (filterv
         (fn [i]
           (not
            (nil?
             (do
               ;; (println "term: " term)
               ;; (println "i: " i "\n\n------------------\n\n")
               (re-find
                (re-pattern term)
                (:name i))))))
         info))))

(defn filter-items
  [info selector search]
  (let [n (sc/text search)
        sl (sc/text selector)
        slower (s/lower-case sl)
        l (info (keyword slower))]

    ;; (println "selected: " sl )
    ;; (println "lower: " slower)
    ;; (println "keyword: " (keyword slower) (type (keyword slower)))
    ;; (println "info keys: " (keys info) (map type (keys info)))
    ;; (println "what: " (info (keyword slower)))

    (dosync
     (alter items-ref find-matching-items l n))))

(defn reference-panel
  [info]
  (let [;selector (sc/combobox :model '("Spells" "Monsters" "Races" "Classes" "Feats" "Backgrounds"))
        sections (book/sections-from-data info)
        selector (sc/combobox :model sections)
        items-search (sc/text :id :items-search-input :columns 10)
        items-list (sc/listbox)
        scrollable-items (sc/scrollable items-list)
        selector-and-items (sc/border-panel :north selector :center scrollable-items)
        items-left (sc/border-panel :north items-search :center selector-and-items)
        items-right (sc/border-panel :class :container :center (sc/label "This is a label?"))]

    ;; (println "got sections: " sections)

    (-> items-list (.setFixedCellWidth 200))

    (sbind/bind items-ref (sbind/property items-list :model))

    (filter-items info selector items-search )

    (se/listen selector :item
               (fn [e]
                 (sc/text! items-search "")
                 (filter-items info e items-search)))
    (se/listen items-search :document (fn [e] (filter-items info selector e)))
    (se/listen items-list :mouse-clicked (fn [e]
                                           (display-item-in-panel
                                            (sc/selection e)
                                            (info (keyword (s/lower-case (sc/text selector))))
                                            items-right)))
    (sc/border-panel :west items-left :center items-right)))
