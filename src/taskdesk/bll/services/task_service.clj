(ns taskdesk.bll.services.task-service
  (:require [taskdesk.bll.protocols.task-service-protocol :as task-protocol]
            [taskdesk.bll.protocols.common-service-protocol :as common-protocol]
            [taskdesk.dal.dao.task-data-access-object :as task-dao]))

(deftype task-service [task-dao]

  common-protocol/common-service-protocol

  (get-all-items
    [this]
    (def response (.get-all-items task-dao))
    (println "----------------TASK ITEMS-------------\n" response)
    response)

  task-protocol/task-service-protocol

  (add-task
    [this task-opts]
    (.add-task task-dao task-opts))

  (edit-task
    [this task-opts]
    (.edit-task task-dao task-opts))

  (get-by-id
    [this id]
    (.get-by-id task-dao id))

  )
