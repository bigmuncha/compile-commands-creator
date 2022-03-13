(ns omar.core
  (:gen-class)
  (:require [clojure.data.json ]
            [cheshire.core :refer :all]
            [omar.defines ]))

(defn read-path [ ]
  (str(read)))

(defn get-file-list [directory]
  (-> directory clojure.java.io/file file-seq))

(defn file-match [file-name substring]
  (if (zero? (compare (-> substring reverse vec)
              (->> file-name reverse (take (count substring)) vec))) true false))


(defn create-command [command filename]
  (let [obj-file (str filename ".o")]
    (-> (vector command obj-file filename)
        flatten
        vec)))

(defn my-json [directory command file]
     {:arguments command
      :directory directory
      :file file})

(defn helper [filename file-extension comp-list]
  (if (file-match filename file-extension)
    (my-json (:directory comp-list) (create-command (:arguments comp-list) filename)
             filename)
    nil))

(defn my-main [comp-list]
  (loop [ file-list (get-file-list (:directory comp-list))
         json-list '() ]
    (if (empty? file-list) (filter #(not (nil? %)) json-list)
        (recur (rest file-list)
               (cons (helper (str (first file-list)) ".cpp" comp-list)
                     json-list)))))

 (defn  json-file-logger [bigfilename comp-list]
   (loop [json-list (my-main comp-list)]
     (if (empty? json-list) nil
         (do (generate-stream (first json-list)
                          (clojure.java.io/writer bigfilename :append true)
                          {:pretty true} )
             (spit bigfilename ",\n" :append true)
             (recur (rest json-list))
             )
         )))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (json-file-logger "/home/omar/igor.json" omar.defines/data-plane))




(defn merge-includes [inc-list]
  (str " -I" (clojure.string/join " -I" inc-list)))

(defn is-directory-have-include? [dir-name]
  (not (empty? (filter #(file-match % ".h") (.list (clojure.java.io/file dir-name))))))
