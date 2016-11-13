(ns taskdesk.dal.models.user-db-model
  (:require [taskdesk.dal.protocols.user-db-protocol :as user-protocol]
            [taskdesk.dal.records.user-record :as user-record]
            [clojure.java.jdbc :as jdbc]))

(deftype user-db-model [db-map]

  user-protocol/user-db-protocol

  (sign-in [this login password]
    (def row (first (jdbc/query db-map
                                ["SELECT id, login, password, name, email, role, karma
                              FROM users
                              WHERE login=? AND password=?" login password])))

    (if (= row nil)
      nil
      (user-record/->user-record
        (:id row)
        (:login row)
        (:password row)
        (:name row)
        (:email row)
        (:role row)
        (:karma row)))
    )

  (sign-up [this user-info]
    (jdbc/insert! db-map
                  :users
                  {:login     (:login user-info)
                   :password  (:password user-info)
                   :name      (:name user-info)
                   :email     (:email user-info)})
  )

)

