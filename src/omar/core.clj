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
               (cons (helper (str (first file-list)) ".c" comp-list)
                     json-list)))))

 (defn  json-file-logger [bigfilename comp-list]
   (loop [json-list (my-main comp-list)]
     (if (empty? json-list) nil
         (do (generate-stream (first json-list) (clojure.java.io/writer bigfilename :append true))
             (spit bigfilename ",\n" :append true)
             (recur (rest json-list))))))

(defn create-compile-command-json [root-dir commands-map]
  )
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  
  (create-compile-command-json
   (str (first args)) global
   ))
  ;(json-file-logger "/home/omar/igor.json" omar.defines/ecobus-commands))

;;(-main)


(defn merge-includes [inc-list]
  (str " -I" (clojure.string/join " -I" inc-list)))
(defn merge-defines [def-list]
  (str " -D" (clojure.string/join " -D" def-list)))

(defn is-directory-have-include? [dir-name]
  (not (empty? (filter #(file-match % ".h") (.list (clojure.java.io/file dir-name))))))

(defn create-command-str [global-struct filename]
  (clojure.string/join " "
                       (list
                        (:compiler global-struct)
                        (merge-includes (:includes global-struct))
                        (merge-defines (:defines global-struct))
                        (:flags global-struct)
                        (:suffix global-struct )
                        (str filename ".o" )
                        filename
                        )))
(defn parse-project-root [project]
  (get-file-list project(second)))
