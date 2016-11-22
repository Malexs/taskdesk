(ns taskdesk.bll.services.task-service
  (:require [taskdesk.bll.protocols.task-service-protocol :as task-protocol]
            [taskdesk.bll.protocols.common-service-protocol :as common-protocol]
            [taskdesk.dal.dao.task-data-access-object :as task-dao])
  (import java.util.Date)
  (import java.text.SimpleDateFormat))

(deftype task-service [task-dao]

  common-protocol/common-service-protocol

  (get-all-items
    [this]
    (def response (.get-all-items task-dao))
    (println "\n----------------TASK ITEMS-------------\n" response)
    response)

  (add-item
    [this options]
    (def opts (:params options))
    (def opts (assoc opts :date (.format (SimpleDateFormat. "yyyy-MM-dd HH:mm:ss")(Date.))))
    (def opts (assoc opts :author "admin"))
    (def opts (assoc opts :milestone (.format (SimpleDateFormat. "yyyy-MM-dd HH:mm:ss")(:milestone opts))))
    (println "\n-----------------------TASK ADD---------------------\n"
             opts)
    (.add-item task-dao opts))

  task-protocol/task-service-protocol

  (edit-task
    [this task-opts]
    (def opts (:params task-opts))
    (when (= (:milestone opts) "")
      (def opts (assoc opts :milestone nil)))

    (.edit-task task-dao opts))

  (get-by-id
    [this id]
    (.get-by-id task-dao id))

  )
