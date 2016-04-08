(ns dm-helper.core
  (:gen-class)
  (:use [seesaw.font]
        [clojure.pprint])
  (:import [org.apache.commons.io.FilenameUtils])
  (:require [dm-helper.files.books :as books]
            [dm-helper.files.utils :as util]
            [dm-helper.gui.frame :as gui-frame]
            [dm-helper.gui.items :as gui-ref]
            [seesaw.core :as sc]
            [seesaw.border :as sb]
            [seesaw.event :as se]
            [seesaw.chooser :as seec]
            [seesaw.bind :as sbind]
            [clojure.string :as string]
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

(defn get-proper-content [thing]
  (let [c "what"])
  )

(defn test-parser [thing]
  (let [name (first thing)
        tags (map :content (second thing))]
    (if (-> tags first first string?)
      {name (-> tags first first)}
      {name (into []
                  (map
                   (fn [e]
                     (into {}
                           (map
                            (fn [f]
                              (let [n (first f)
                                    t (second f)]
                                {n (first (map #(-> % :content first) t))}
                                )

                              )
                            (group-by :tag e)
                            ))


                     )
                   tags))}
      )
    )
  )

(defn parser [thing]

  (println "PARSER: " thing)
  (let [name (first thing)
        tags (second thing)

        content (map :content tags)
        types (distinct (map type (flatten content)))
        is-string (first (distinct (map string? (flatten content))))
        ]

    (println "checking: " name)
    (println "tags: " (count content))

    (if is-string
      (do
        (print "content: ")
        (pprint (string/join (map
                              #(string/join (if (nil? %) "\n" %))
                              (flatten content)))))

      (do
        (print "content 2: ")

        ;; (pprint content)

        (pprint (flatten (map
                          (fn [e]
                            (map (fn [f]
                                   ;;(println "f: " f)
                                   (into f {:attrs (into (if (nil? (:attrs f)) {} (:attrs f)) (get-in e [:attrs]))}))
                                 (:content e)
                                 )

                            )

                          tags)))

        )
      )

    (println "-------------\n\n")
    )
  )

(defn -main
  "LEEEARNING"
  [& args]
  ;; (books/load-saved-info)
  ;; (println (System/getProperty "java.runtime.version"))
  ;; (def data (books/load-all-from-dir "/home/sean/Dropbox/D&D App Files"))
  ;; (spit "info.clj" data)
  ;; (println "keys: " (keys data))
  ;; (println "User home directory is: " books/app-dir)

  ;; (sc/native!)
  ;; (main-app)

  (let [xml (books/xml-from-file (io/resource "test.xml"))
        parts (group-by :tag (:content (first xml)))]

    ;; (pprint
    ;;  (map
    ;;   (fn [e]
    ;;     (let [test (:content e)]
    ;;       (into {} (map test-parser (group-by :tag test)))
    ;;       )
    ;;     )
    ;;   (:monster parts)))

    (println "################# \n\t\tRACES\n#################\n")

    (doall (map
            parser
            (group-by :tag (:content (first (:race parts))))
            ))

    (println "\n################# \n\t\tSPELLS\n#################\n")

    (doall (map
            parser
            (group-by :tag (:content (first (:spell parts))))
            ))

    (println "\n################# \n\t\tCLASSES\n#################\n")

    (doall (map
            parser
            (group-by :tag (:content (first (:class parts))))
            ))

    (println "\n################# \n\t\tMONSTERS\n#################\n")

    (doall (map
            parser
            (group-by :tag (:content (first (:monster parts))))
            ))

    )

  )
