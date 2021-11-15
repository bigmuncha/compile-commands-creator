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
  (let [obj-file (->> filename reverse (drop 3 ) (cons \o )
                      reverse clojure.string/join)]
    (str command " " obj-file " " filename)))

(defn my-json [directory command file]
     {:directory directory,
    :command command
    :file file})

(defn helper [filename file-extension comp-list]
  (if (file-match filename file-extension)
    (my-json (second comp-list) (create-command (first comp-list) filename)
             filename)
    nil))

(defn my-main [comp-list]
  (loop [ file-list (get-file-list (second comp-list))
         json-list '() ]
    (if (empty? file-list) (filter #(not (nil? %)) json-list)
        (recur (rest file-list)
               (cons (helper (str (first file-list)) ".cpp" comp-list`)
                     json-list)))))

 (defn  json-file-logger [bigfilename comp-list]
   (loop [json-list (my-main comp-list)]
     (if (empty? json-list) nil
         (do (generate-stream (first json-list) (clojure.java.io/writer bigfilename :append true))
             (spit bigfilename ",\n" :append true)
             (recur (rest json-list))))))
    
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (json-file-logger "/home/omar/igor.json" omar.defines/radius-commands))

(-main)
