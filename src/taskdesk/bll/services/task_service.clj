(ns taskdesk.bll.services.task-service
  (:require [taskdesk.bll.protocols.task-service-protocol :as task-protocol]
            [taskdesk.bll.protocols.common-service-protocol :as common-protocol]
            [taskdesk.bll.services.log-service :as ls]
            [taskdesk.bll.invoice-center :as ic]
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
    [this options session]
    (let [options (assoc options :date (.format (SimpleDateFormat. "yyyy-MM-dd HH:mm:ss")(Date.)))
          options (assoc options :author (:name session))]
      (ls/log-task-edit (:author options) (:title options) false)
      (ic/add-invoice (:assignee options) (str "User ("
                                                 (:name session)
                                                 ") assigned task: ("
                                                 (:title options)
                                                 ") for you."))
      (if (is-correct-date? (:milestone options))
        (do
          (.add-item task-dao options))
        (let [options (assoc options :milestone nil)]
          (do
            (.add-item task-dao options))))))

  task-protocol/task-service-protocol

  (edit-task
    [this task-opts session]
    (if (is-correct-date? (:milestone task-opts))
      (do
        (if (not (= (:assignee task-opts) (:name session)))
          (ic/add-invoice (:assignee task-opts) (str "User ("
                                                     (:name session)
                                                     ") changed your/assigned you to/disassigned you from task: ("
                                                     (:title task-opts)
                                                     ")")))
        (ls/log-task-edit (:name session) (:title task-opts) true)
        (.edit-task task-dao task-opts))
      (println "Incorrect date")
      )
  )

  (get-by-id
    [this id]
    (.get-by-id task-dao id))

  (delete-item
    [this id session]
    (do
      (ls/log-task-delete (:name session) id)
      (.delete-item task-dao id))))