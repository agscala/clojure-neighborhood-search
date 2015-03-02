(ns neighborhood-search.core
  (:gen-class)
  (:use [geo [poly :as poly]]
        [clj-yaml.core :as yaml]
        [clojure.string :as str]))

(def alger-heights [
       -85.648052564782 42.9273362102149
       -85.6478882590963 42.9251726547117
       -85.6476264197602 42.9213638858421
       -85.6475059484637 42.9193322486685
       -85.6473969367528 42.9176735292532
       -85.6472773313717 42.915863478862
       -85.647173069728 42.9142533317003
       -85.6471147300296 42.9132383473197
       -85.6470869802934 42.9127994026719
       -85.6410338812922 42.912746252097
       -85.6362877432632 42.912704026709
       -85.6305258585951 42.9126495789206
       -85.6280654134082 42.912645572882
       -85.6281641435672 42.9129101639149
       -85.6283939961886 42.913379815837
       -85.6286770835257 42.9138966488788
       -85.6289654150077 42.9143041662849
       -85.6294642924574 42.9149662936917
       -85.6304850637386 42.9163190171978
       -85.6318362356398 42.9181240592178
       -85.633108429244 42.9198976279501
       -85.6332865929073 42.9217156850473
       -85.633460729244 42.9235838816006
       -85.6334983318764 42.9239686722763
       -85.6310128233994 42.9239468583333
       -85.6312477862934 42.927123399508
       -85.6321772800006 42.9271313996717
       -85.6354081928527 42.9271652125069
       -85.638217039954 42.9271947012253
       -85.6384635823553 42.9271982246275
       -85.6406151999787 42.9272285168723
       -85.6439846135493 42.9272774427542
       -85.648052564782 42.9273362102149
])

(def unformatted-neighborhood-data (yaml/parse-string (slurp "neighborhood_data.txt")))

(def formatted-neighborhood-data
	(map (fn [[name points]]
			[name (map #(Float/parseFloat %) (str/split points #"[\, ]"))]
			) unformatted-neighborhood-data))

(defn find-neighborhood [lat lng]
		(def result-neighborhoods
			(filter (fn [[name points]] (poly/region-contains? lat lng points)) formatted-neighborhood-data))

		(map (fn [[name points]] name) result-neighborhoods))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (println (poly/region-contains? -85.646726 42.913097 alger-heights))

  (def tests-filename (first args))

  (with-open [rdr (clojure.java.io/reader tests-filename)]
      (doseq [line (line-seq rdr)]
          (def unformatted-test (yaml/parse-string line))
              (def formatted-test
                  (map (fn [[testname point]]
                      [testname (map #(Float/parseFloat %) (str/split point #"[\, ]"))]
                  ) unformatted-test))

              (def test-result (apply (fn [[testname [lat lng]]] [testname (find-neighborhood lat lng)]) formatted-test))

              (printf "%s: %s\n" (name (first test-result)) (str/join ", " (map #(name %) (last test-result))))))
)
