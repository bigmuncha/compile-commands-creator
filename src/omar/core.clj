(ns omar.core
  (:gen-class)
  (:require [clojure.data.json ]
            [cheshire.core :refer :all]
            [omar.defines ]))

(generate-stream {:command "bar/ogpr" :baz 5} (clojure.java.io/writer "/tmp/foo" :append true))
(defn read-path [ ]
  (str(read)))

(defn get-file-list [directory]
  (-> directory clojure.java.io/file file-seq))

(defn file-match [file-name substring]
  (if (clojure.string/includes? file-name substring) true false))

(defn create-command [command filename]
  (let [obj-file (->> filename reverse (drop 3 ) (cons \o )
                      reverse clojure.string/join)]
    (str command " " obj-file " " filename)))

(defn my-json [directory command file]
     {:directory directory,
    :command command
    :file file})

(defn helper [filename]
  (if (file-match filename ".cpp")
    (my-json omar.defines/dir (create-command omar.defines/command filename)
             filename)
    nil))

(defn my-main []
  (loop [ file-list (get-file-list omar.defines/dir)
         json-list '() ]
    (if (empty? file-list) (filter #(not (nil? %)) json-list)
        (recur (rest file-list)
               (cons (helper (str (first file-list)))
                     json-list)))))

 (defn  json-file-logger [bigfilename]
   (loop [json-list (my-main)]
     (if (empty? json-list) nil
         (do (generate-stream (first json-list) (clojure.java.io/writer bigfilename :append true))
             (spit bigfilename ",\n" :append true)
             (recur (rest json-list))))))
    
;; (defn json-print [json-list]
;;   (loop [json-list json-list printer []]
;;     (if
;;         (empty? json-list) printer
;;         (let [temp  (if (nil? (first json-list))
;;                       "" (first json-list)))]
;;           (recur (rest json-list ) (cons temp printer)))))
 
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (json-file-logger "/home/omar/omar.json"))

(-main)
