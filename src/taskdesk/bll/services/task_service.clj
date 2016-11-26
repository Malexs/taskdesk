(ns taskdesk.bll.services.task-service
  (:require [taskdesk.bll.protocols.task-service-protocol :as task-protocol]
            [taskdesk.bll.protocols.common-service-protocol :as common-protocol]
            [taskdesk.validation.task-validation :refer :all]
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
    (let [opts (:params options)
          opts (assoc opts :date (.format (SimpleDateFormat. "yyyy-MM-dd HH:mm:ss")(Date.)))
          opts (assoc opts :author "admin")]
      (if (is-correct-date? (:milestone opts))
        (.add-item task-dao opts))))

  task-protocol/task-service-protocol

  (edit-task
    [this task-opts]
    (def opts (:params task-opts))
    (if (is-correct-date? (:milestone opts))
      (.edit-task task-dao opts)
      (println "Incorrect date")
      )
  )

  (get-by-id
    [this id]
    (.get-by-id task-dao id))

  (delete-item
    [this id]
    (.delete-item task-dao id)))
