(ns taskdesk.core
     (:require [taskdesk.dal.db :as db]
               [clojure.java.jdbc :as jdbc]))

(defn -main
  "I don't do a whole lot."
  [& args]
  (
    println (jdbc/query db/db-map ["SELECT * FROM users"])))
