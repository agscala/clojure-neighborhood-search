(ns neighborhood-search.core
    (:gen-class)
    (:use [geo [poly :as poly]]
        [clj-yaml.core :as yaml]
        [clojure.java.io :as io])
    (:require [clojure.string :as str]))

(def unformatted-neighborhood-data (yaml/parse-string (slurp "neighborhood_data.txt")))

(def formatted-neighborhood-data
    (map (fn [[name points]]
            [name (map #(Float/parseFloat %) (str/split points #"[\, ]"))]
            ) unformatted-neighborhood-data))

(defn internal-find-neighborhood [lat lng neighborhood-data]
    "Internal implementation used for testing different data purposes"
    (def result-neighborhoods
        (filter (fn [[name points]] (poly/region-contains? lat lng points)) neighborhood-data))

    (map (fn [[neighborhood-name points]] (name neighborhood-name)) result-neighborhoods))

(defn find-neighborhood
    "internal-find-neighborhood convenience wrapper for using default data"
    [lat lng] (internal-find-neighborhood lat lng formatted-neighborhood-data))

(defn -main
    "Check lat/lngs from an input file to find which neighborhoods they exist in."
    [& args]

    (if (or
            (= 0 (count args))
            (not (.exists (io/file (first args)))))
        (do
            (println "ERROR: Input file (argument 1) does not exist.")
            (System/exit 1)))

    (def tests-filename (first args))

    (with-open [rdr (clojure.java.io/reader tests-filename)]
        (doseq [line (line-seq rdr)]
            (def unformatted-test (yaml/parse-string line))
            (def formatted-test
                (map (fn [[testname point]]
                    [testname (map #(Float/parseFloat %) (str/split point #"[\, ]"))]
                ) unformatted-test))

            (def test-result (apply (fn [[testname [lat lng]]] [testname (find-neighborhood lat lng)]) formatted-test))

            (printf "%s: %s\n" (name (first test-result)) (str/join ", " (last test-result)))))
)

