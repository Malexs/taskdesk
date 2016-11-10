(ns taskdesk.dal.db
  (:require [clojure.java.jdbc :as j]))

(def db-map {:subprotocol "mysql"
            :subname "//localhost:3306/taskdesk"
            :user "root"
            :password "root"})