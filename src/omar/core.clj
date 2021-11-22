(ns omar.core
  (:gen-class)
  (:require
            [cheshire.core :refer :all]
            [omar.defines ]))


(defn get-file-list [directory]
  (-> directory clojure.java.io/file file-seq))

(defn file-match [file-name substring]
  (if (zero? (compare (-> substring reverse vec)
              (->> file-name reverse (take (count substring)) vec))) true false))

(defn merge-includes [inc-list]
  (str " -I" (clojure.string/join " -I" inc-list)))
(defn merge-defines [def-list]
  (str " -D" (clojure.string/join " -D" def-list)))
(defn is-directory-have-include? [dir-name]
  (not (empty? (filter #(file-match % ".h") (.list (clojure.java.io/file dir-name))))))

(defn get-dir-list [project]
  (filter #(.isDirectory %) (get-file-list project)))

(defn get-include-list [dir-list]
  (map str (filter #(is-directory-have-include? %) dir-list)))

(defn final-get-include-list[project]
  (concat
   (-> project  get-dir-list get-include-list)
   omar.defines/common-include))

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

(defn my-json [directory command file]
     {:directory directory,
      :command command
      :file file})


(defn create-global-struct [project]
  {:compiler omar.defines/cppcompiler
   :includes (final-get-include-list project)
   :defines omar.defines/data-plane-defines
   :flags omar.defines/cpp-flags
   :suffix omar.defines/suffix
   })


(defn  json-file-logger [bigfilename comp-list]
   (loop [json-list (my-main comp-list)]
     (if (empty? json-list) nil
         (do
           (generate-stream
            (first json-list)
            (clojure.java.io/writer bigfilename :append true))
             (spit bigfilename ",\n" :append true)
             (recur (rest json-list))))))

(defn my-main [project ]
  (loop [
         file-list (filter #(file-match % ".c" ) (get-file-list (second project)))
         json-list '()
         global-struct (create-global-struct project)
         ]
    (if (empty? file-list) (filter #(not (nil? %)) json-list)
        (recur (rest file-list)
               (cons
                (my-json project
                         (create-command-str global-struct (first file-list))
                         (first file-list))
                json-list)
               global-struct))))
