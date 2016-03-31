(ns dm-helper.files.spell
  (:gen-class)
  (:require
   [dm-helper.files.utils :as futil]))

(defn load-spells-from-file
  ""
  [file]

  (:content (first (futil/zip-str (slurp (futil/data-file file)))))

  ;; (also-needs-name
  ;;  (map #(map needs-better-name (monster %))
  ;;       )
  ;;  )
  )
