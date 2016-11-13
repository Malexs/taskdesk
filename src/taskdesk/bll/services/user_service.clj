(ns taskdesk.bll.services.user-service
  (:require [taskdesk.bll.protocols.user-service-protocol :as user-protocol]
            [taskdesk.dal.models.user-db-model :as user-model]
            ))

(defrecord user-service [user-model]

  user-protocol/user-service-protocol

  (sign-in [this login password]
    (.sign-in user-model login password))

  (sign-up [this user-info]
    (.sign-up user-model user-info))

  )

