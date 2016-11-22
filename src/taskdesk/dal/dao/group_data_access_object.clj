(ns taskdesk.dal.dao.group-data-access-object
  (:require [taskdesk.dal.protocols.common-db-protocol :as common-protocol]
            [taskdesk.dal.models.group-model :as group-model]
            [taskdesk.dal.db :as db]
            [clojure.java.jdbc :as jdbc]))

(deftype group-data-access-object []

  common-protocol/common-db-protocol

  (get-all-items
    [this]
    (into [] (jdbc/query db/db-map
                         ["SELECT *
                           FROM taskgroups"]
                         :row-fn #(group-model/->group-record
                                   (:id %1)
                                   (:name %1)))))

  (add-item
    [this options]
    (println options))

  )
