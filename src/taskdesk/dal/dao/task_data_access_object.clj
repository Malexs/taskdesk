(ns taskdesk.dal.dao.task-data-access-object
  (:require [taskdesk.dal.protocols.common-db-protocol :as common-protocol]
            [taskdesk.dal.protocols.task-db-protocol :as task-protocol]
            [taskdesk.dal.models.task-model :as task-model]
            [clojure.java.jdbc :as jdbc]
            [taskdesk.dal.db :as db]))

(deftype task-data-access-object []

  common-protocol/common-db-protocol

  (get-all-items
    [this]
    (into [] (jdbc/query db/db-map
                         ["SELECT t.id,
                                  u.name AS author,
                                  date,
                                  title,
                                  description,
                                  milestone,
                                  us.name AS assignee,
                                  t.status,
                                  t.group
                           FROM tasks as t
                           JOIN users as u
                              ON u.id = t.author
                           JOIN users as us
                              ON us.id = t.assignee
                           JOIN taskgroups as tg
                              ON tg.id = t.group"]
                         :row-fn #(task-model/->task-record
                                   (:id %1)
                                   (:author %1)
                                   (:date %1)
                                   (:title %1)
                                   (:description %1)
                                   (:milestone %1)
                                   (:assignee %1)
                                   (:status %1)
                                   (:group %1)))))

  task-protocol/task-db-protocol

  (get-by-id
    [this id]
    (first (jdbc/query db/db-map
                       ["SELECT t.id,
                                  u.name AS author,
                                  date,
                                  title,
                                  description,
                                  milestone,
                                  us.name AS assignee,
                                  t.status,
                                  tg.name AS \"group\"
                           FROM tasks as t
                           JOIN users as u
                              ON u.id = t.author
                           JOIN users as us
                              ON us.id = t.assignee
                           JOIN taskgroups as tg
                              ON tg.id = t.group
                           WHERE t.id=?" id]
                       :row-fn #(task-model/->task-record
                                 (:id %1)
                                 (:author %1)
                                 (:date %1)
                                 (:title %1)
                                 (:description %1)
                                 (:milestone %1)
                                 (:assignee %1)
                                 (:status %1)
                                 (:group %1)))))

  (add-task
    [this task-opts]
    (jdbc/insert! db/db-map
                  :tasks
                  {:author      (:author task-opts)
                   :date        (:date task-opts)
                   :title       (:title task-opts)
                   :description (:description task-opts)
                   :milestone   (:milestone task-opts)
                   :assignee    (:assignee task-opts)
                   :status      (:status task-opts)
                   :group       (:group task-opts)}))

  (edit-task
    [this task-opts]
    (def opts (:params task-opts))
    (jdbc/execute! db/db-map
                ["UPDATE tasks as t
                JOIN users as u
                  ON u.name=?
                JOIN taskgroups as tg
                  ON tg.name=?
                SET title=?,
                    description=?,
                    milestone=?,
                    t.status=?,
                    assignee=u.id,
                    t.group=tg.id
                WHERE t.id=?"
                 (:assignee opts)
                 (:group opts)
                 (:title opts)
                 (:description opts)
                 (:milestone opts)
                 (:status opts)
                 (Integer. (re-find #"[0-9]*" (:id opts)))]))

  )
