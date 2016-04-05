(ns dm-helper.gui.spells
  (:gen-class)
  (:require [seesaw.core :as sc]
            [seesaw.border :as sb]
            [seesaw.event :as se]
            [seesaw.bind :as sbind]))


(defn display-spell-in-panel
  [selected spells panel]
  (let [current (first (first (sc/config panel :items)))
        new (sc/label selected)]
    (sc/replace! panel current new)))

(def spells-ref (ref []))

(defn find-matching-spells [current spells term]
  (sort
   (map :name
        (filterv
         (fn [i]
           (not (nil? (re-find (re-pattern term) (:name i)))))
         spells))))

(defn filter-spells
  [spells search]

  (let [n (sc/text search)]
    (dosync
     (alter spells-ref find-matching-spells spells n))))

(defn spells-panel
  [spells]
  (let [spells-search (sc/text :id :spells-search-input :columns 10)
        spells-list (sc/listbox)
        scrollable-spells (sc/scrollable spells-list)
        spells-left (sc/border-panel :north spells-search :center scrollable-spells)
        spells-right (sc/border-panel :class :container :center (sc/label "This is a label?"))]

    (-> spells-list (.setFixedCellWidth 200))

    (sbind/bind spells-ref (sbind/property spells-list :model))

    (filter-spells spells spells-search)

    (se/listen spells-search :document (fn [e] (filter-spells spells e)))
    (se/listen spells-list :mouse-clicked (fn [e] (display-spell-in-panel (sc/selection e) spells spells-right)))
    (sc/border-panel :west spells-left :center spells-right)
    ))
