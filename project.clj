(defproject neighborhood-search "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [factual/geo "1.0.0"]
                 [org.clojars.mpenet/clj-yaml "0.3.4"]]
  :main ^:skip-aot neighborhood-search.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
