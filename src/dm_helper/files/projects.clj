(ns dm-helper.files.projects
  (:gen-class)
  (:import [org.apache.commons.io.FilenameUtils])
  (:require [clojure.java.io :as io]
            [dm-helper.files.books :as books]
            [dm-helper.files.utils :as util]))

(defn load-projects [proj-ref]
  (util/load-saved-ref proj-ref "projects.clj"))

(defn save-proj-to-file [info]
  (util/save-ref-to-file info "projects.clj"))
