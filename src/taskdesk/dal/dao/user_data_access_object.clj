(ns taskdesk.dal.dao.user-data-access-object
  (:use [taskdesk.dsl.execs]
        [taskdesk.dsl.struct-func]
        [taskdesk.dsl.renders])
  (:require [taskdesk.dal.protocols.user-db-protocol :as user-protocol]
            [taskdesk.dal.protocols.common-db-protocol :as common-protocol]
            [taskdesk.dal.models.user-model :as user-record]
            [clojure.java.jdbc :as jdbc]))

(deftype user-data-access-object [db-map]

  common-protocol/common-db-protocol

  (get-all-items
    [this]
    (into [] (fetch (select (fields [:id :name])
                            (from :users)))))

  (add-item
    [this options]
    (jdbc/insert! db-map
                  :users
                  {:login     (:login options)
                   :password  (:password options)
                   :name      (:name options)
                   :email     (:email options)}))

  user-protocol/user-db-protocol

  (sign-in
    [this login password]
    (first (fetch (select (from :users)
                          (where (and (== :login login) (== :password password))))
                  #(user-record/->user-record (:id %1)
                                              (:login %1)
                                              (:name %1)
                                              (:email %1)
                                              (:role %1)
                                              (:karma %1)
                                              (if (or (= (:role %1) 0) (> (:karma %1) 999))
                                                1
                                                0)))))


  ;COUNT(*) as count
  (get-user-by-login
    [this login]
    (first (fetch (select (fields [:id :login :name :email :role :karma])
                          (from :users)
                          (where (== :login login)))
                  #(user-record/->user-record (:id %1)
                                              (:login %1)
                                              (:name %1)
                                              (:email %1)
                                              (:role %1)
                                              (:karma %1)
                                              nil))))
)
