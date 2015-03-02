(ns neighborhood-search.core
  (:gen-class)
  (:use [geo [poly :as poly]]
        [clj-yaml.core :as yaml]
        [clojure.string :as str]))

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
