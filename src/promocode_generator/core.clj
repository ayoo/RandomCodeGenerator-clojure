(ns promocode-generator.core
  (:gen-class)
  (:require [clojure.java.io :as io]))

(def letters "BCDFGHJKLMNPQRSTVWXYZ23456789")

(defn generate-code
  "Generate a promocode by randomly concatnating letters"
  [digit]
  (apply str (take digit (repeatedly #(rand-nth letters)))))

(defn generate
  "Iterate to generate promocodes"
  [{:keys [digit iteration]}]
  (loop [container #{}]
    (if (< (count container) iteration)
      (recur (conj container (generate-code digit)))
      container)))

(defn write-to-file
  "Write the generated codes to csv file"
  [codes]
    (println (str "Creating a csv file with " (count codes) " codes"))
    (with-open [writer (io/writer "codes.csv")]
      (doseq [code codes]
        (.write writer (str code "\n"))))
  )

(defn -main [& args]
  (write-to-file (generate {:digit (read-string (first args))
                            :iteration (read-string (last args))}))
  (.exists (io/file "codes.csv")))