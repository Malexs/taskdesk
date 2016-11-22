(ns taskdesk.bll.services.group-service
  (:require [taskdesk.bll.protocols.common-service-protocol :as common-protocol]))

(deftype group-service [group-dao]

  common-protocol/common-service-protocol

  (get-all-items
    [this]
    (def response (.get-all-items group-dao))
    (println "\n----------------GROUP ITEMS-------------\n" response)
    response)

  (add-item
    [this options]
    (.add-item group-dao options))

  )
