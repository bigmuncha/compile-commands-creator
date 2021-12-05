(ns core.main
  (:require [babashka.fs :as fs]))

(defn my-json [directory command file]
     {:directory directory,
      :command command
      :file file})

(defn file-match [filename]
  (if (re-matches #"\w+\.c(pp)*\b" filename) true
      false))


(defn create-command [command filename subs]
  (let [obj-file (clojure.string/replace filename  #".c[p]*" ".o")]
    (str command " " obj-file " " filename)))

(defn get-include-list [proj]
  (set (map (comp str fs/parent) (fs/glob proj "**.h"))))

(defn get-file-list [proj]
  (filter  #(nil? (re-matches #"\S+CMakeFiles\S+" %)) (set (map str (concat (fs/glob proj "**.cpp") (fs/glob proj "**.c"))))))

(defn -main [& _args]
  (print (final-get-include-list "/home/omar/ecotelecom/configd")))
(-main)
