(defproject dm-helper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.apache.commons/commons-io "1.3.2"]
                 [seesaw "1.4.5"]]
  :main ^:skip-aot dm-helper.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[org.clojure/tools.nrepl "0.2.7"]]
                   :plugins [[cider/cider-nrepl "0.11.0-SNAPSHOT"]]}})
