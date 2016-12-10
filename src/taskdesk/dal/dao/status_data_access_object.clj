(ns taskdesk.dal.dao.status-data-access-object
  (:use [taskdesk.dsl.execs]
        [taskdesk.dsl.struct-func]
        [taskdesk.dsl.renders])
  (:require [taskdesk.dal.models.status-model :as status-model]
            [taskdesk.dal.protocols.common-db-protocol :as common-protocol]
            [taskdesk.dal.db :as db]
            [clojure.java.jdbc :as jdbc]))

(deftype status-data-access-object []

  common-protocol/common-db-protocol

  (get-all-items
    [this]
    (into [] (fetch (select (from :stats)))))

  (add-item
    [this options]
    (println options))
  )
