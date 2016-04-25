(ns dm-helper.gui.reference
  (:gen-class)
  (:require [dm-helper.files.books :as book]
            [seesaw.core :as sc]
            [seesaw.border :as sb]
            [seesaw.event :as se]
            [seesaw.bind :as sbind]
            [seesaw.mig :as smig]
            [clojure.string :as s]))

(def items-ref (ref []))

(defn display-monster-in-panel [selected items panel])
(defn display-spell-in-panel [selected items panel])
(defn display-race-in-panel [selected items panel])
(defn display-class-in-panel [selected items panel])
(defn display-feat-in-panel [selected items panel])
(defn display-background-in-panel [selected items panel])
(defn display-item-in-panel [selected items panel])

(defn bold-label [t]
  (sc/label :text t
            :font "ARIAL-BOLD-14"))

(defn title-label [t]
  (sc/label :text t
            :font "ARIAL-BOLD-30"))


(defn actions-to-panel [t]
  (println "actions-to-panel: " t)
  (loop [bits (if (vector? t) t [t])
         result []]
    (let [thing (first bits)
          name (:name thing)
          text (:text thing)
          p (sc/border-panel :west (bold-label name) :center (sc/label text))]
      (if (> (count (rest bits)) 0)
        (recur (rest bits) (conj result p (sc/separator)))
        (sc/vertical-panel :items (conj result p))))))

(defn display-item-in-panel
  [selected-type selected items panel]

  (let [thing (first (filter (fn [x] (= selected (:name x))) items))
        current (first (first (sc/config panel :items)))
        name (sc/label :text selected :font "ARIAL-BOLD-40")
        str (:str thing)
        dex (:dex thing)
        con (:con thing)
        int (:int thing)
        wis (:wis thing)
        cha (:cha thing)
        pp  (:passive thing)
        hp  (:hp thing)
        t   (:type thing)
        ac  (:ac thing)
        align (:alignment thing)
        size (:size thing)
        speed (:speed thing)
        cr (:cr thing)

        attrs (smig/mig-panel :items [
                                      ["STR"] ["DEX"] ["CON"] ["INT"] ["WIS"] ["CHA"]
                                      ["Passive Perception", "width 150:150:200, align center"]
                                      ["HP" "width 100:100:150, wrap, align center"]
                                      [str] [dex] [con] [int] [wis] [cha]
                                      [pp "align center"]
                                      [hp "align center"]
                                      ])

        stats (smig/mig-panel
               :items [
                       [(bold-label "Type: ")] [t "align left, width 200:250:300"]
                       [(bold-label "AC: ")] [ac "align left, width 200:250:300"]
                       [(bold-label "Alignment: ")] [align "align left, width 200:250:300, wrap"]
                       [(bold-label "Size: ")] [size "align left, width 200:250:300"]
                       [(bold-label "Speed: ")] [speed "align left, width 200:250:300"]
                       [(bold-label "CR: ")] [cr "align left, width 200:250:300"]
                       ])

        p (sc/scrollable (smig/mig-panel :items [
                                                 [name "span"]
                                                 [stats "height ::80"]
                                                 ["  ", "width ::20"]
                                                 [attrs "height ::80, wrap"]
                                                 [(title-label "Actions") "wrap, span"]
                                                 [(actions-to-panel (:action thing)) "wrap, span"]
                                                 [(title-label "Traits") "wrap, span"]
                                                 [(actions-to-panel (:trait thing)) "wrap, span"]
                                                 ]))
        ]

    ;; (println "actions: " (:action thing) "\n#####################\n\n"
    ;;          (actions-to-panel (:action thing)) "\n\n####################\n")
    ;; (println "what: " selected)
    ;;(println "panel? " (actions-to-panel (:action thing)))
    ;; (println "traits: \n" (:action thing) "\n" (type (:action thing)) "\n-------------\n\n")

    (sc/replace! panel current p)))

(defn find-matching-items [current info term]
  (sort
   (map :name
        (filterv
         (fn [i]
           (not (nil? (re-find (re-pattern term) (:name i)))))
         info))))

(defn filter-items
  [info selector search]
  (let [n (sc/text search)
        sl (sc/text selector)
        slower (s/lower-case sl)
        l (info (keyword slower))]
    (dosync
     (alter items-ref find-matching-items l n))))

(defn reference-panel
  [info-ref]
  (let [sections (book/sections-from-data @info-ref)
        selector (sc/combobox :model sections)
        items-search (sc/text :id :items-search-input :columns 10)
        items-list (sc/listbox)
        scrollable-items (sc/scrollable items-list)
        selector-and-items (sc/border-panel :north selector :center scrollable-items)
        items-left (sc/border-panel :north items-search :center selector-and-items)
        items-right (sc/border-panel :class :container :center (sc/label "This is a label?"))]

    (sbind/subscribe info-ref (fn [e] (filter-items @info-ref selector items-search)))

    (-> items-list (.setFixedCellWidth 200))

    (sbind/bind items-ref (sbind/property items-list :model))

    (filter-items @info-ref selector items-search )

    (se/listen selector :item
               (fn [e]
                 (sc/text! items-search "")
                 (filter-items @info-ref e items-search)))
    (se/listen items-search :document
               (fn [e] (filter-items @info-ref selector e)))

    (se/listen items-list :mouse-clicked (fn [e]
                                           (let [selected-type (keyword (s/lower-case (sc/text selector)))]
                                             (display-item-in-panel
                                              selected-type
                                              (sc/selection e)
                                              (selected-type @info-ref)
                                              items-right))))

    (sc/border-panel :west items-left :center items-right)))
