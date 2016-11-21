(ns taskdesk.bll.services.user-service
  (:require [taskdesk.bll.protocols.user-service-protocol :as user-protocol]
            [taskdesk.bll.protocols.common-service-protocol :as common-protocol]
            [taskdesk.dal.dao.user-data-access-object :as user-model]
            ))

(deftype user-service [user-model]

  common-protocol/common-service-protocol

  (get-all-items
    [this]
    (.get-all-items user-model))

  user-protocol/user-service-protocol

  (sign-in [this login password]
    (.sign-in user-model login password))

  (sign-up [this user-info]
    (.sign-up user-model user-info))

  (get-user-by-login [this login]
    (def user (.get-user-by-login user-model login))
    (println user)
    user)

  )

