(ns taskdesk.dsl.execs
  (:use [taskdesk.dsl.emit]
        [taskdesk.dsl.renders]
        [taskdesk.dsl.struct-func])
  (:require [taskdesk.dal.db :as db]
            [clojure.java.jdbc :as jdbc]))

(defn- to-sql-params
  [relation]
  (let [{s :sql p :args} (as-sql relation)]
    (vec (cons s p))))

(defn fetch
  ([relation] (jdbc/query db/db-map (to-sql-params relation)))
  ([relation func] (jdbc/query db/db-map
                               (to-sql-params relation)
                               :row-fn func)))
