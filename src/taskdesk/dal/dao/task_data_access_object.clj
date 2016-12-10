(ns taskdesk.dal.dao.task-data-access-object
  (:use [taskdesk.dsl.execs]
        [taskdesk.dsl.struct-func]
        [taskdesk.dsl.renders])
  (:require [taskdesk.dal.protocols.common-db-protocol :as common-protocol]
            [taskdesk.dal.protocols.task-db-protocol :as task-protocol]
            [taskdesk.dal.models.task-model :as task-model]
            [clojure.java.jdbc :as jdbc]
            [taskdesk.dal.db :as db]))

(deftype task-data-access-object []

  common-protocol/common-db-protocol

  (get-all-items
    [this]
    (into [] (fetch (select (fields {:id :t.id
                                     :author :u.name
                                     :date :date
                                     :title :title
                                     :description :description
                                     :milestone :milestone
                                     :assignee :us.name
                                     :status :t.status
                                     :group :t.group})
                            (from :t :tasks)
                            (join :u :users (== :u.id :t.author))
                            (join :us :users (== :us.id :t.assignee))
                            (join :tg :taskgroups (== :t.group :tg.id)))
                    #(task-model/->task-record
                                  (:id %1)
                                  (:author %1)
                                  (:date %1)
                                  (:title %1)
                                  (:description %1)
                                  (:milestone %1)
                                  (:assignee %1)
                                  (:status %1)
                                  (:group %1)))))

  (add-item
    [this options]
    (jdbc/execute! db/db-map
                   ["INSERT INTO tasks (date,
                                        title,
                                        description,
                                        milestone,
                                        tasks.status,
                                        author,
                                        assignee,
                                        tasks.group)
                   values (?,
                           ?,
                           ?,
                           ?,
                           ?,
                           (SELECT id FROM users WHERE name=?),
                           (SELECT id FROM users WHERE name=?),
                           (SELECT id FROM taskgroups WHERE name=?))"
                    (:date options)
                    (:title options)
                    (:description options)
                    (:milestone options)
                    (:status options)
                    (:author options)
                    (:assignee options)
                    (:group options)]))

  task-protocol/task-db-protocol

  (get-by-id
    [this id]
    (first (fetch (select (fields {:id :t.id
                                   :author :u.name
                                   :date :date
                                   :title :title
                                   :description :description
                                   :milestone :milestone
                                   :assignee :us.name
                                   :status :t.status
                                   :group :tg.name})
                          (from :t :tasks)
                          (join :u :users (== :u.id :t.author))
                          (join :us :users (== :us.id :t.assignee))
                          (join :tg :taskgroups (== :tg.id :t.group))
                          (where (== :t.id id)))
                  #(task-model/->task-record
                                (:id %1)
                                (:author %1)
                                (:date %1)
                                (:title %1)
                                (:description %1)
                                (:milestone %1)
                                (:assignee %1)
                                (:status %1)
                                (:group %1)))))

  (edit-task
    [this task-opts]
    (if (= (:status task-opts) "Closed")
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
                    t.group=tg.id,
                    u.karma=(u.karma + 1)
                WHERE t.id=?"
                      (:assignee task-opts)
                      (:group task-opts)
                      (:title task-opts)
                      (:description task-opts)
                      (:milestone task-opts)
                      (:status task-opts)
                      (Integer. (re-find #"[0-9]*" (:id task-opts)))])
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
                      (:assignee task-opts)
                      (:group task-opts)
                      (:title task-opts)
                      (:description task-opts)
                      (:milestone task-opts)
                      (:status task-opts)
                      (Integer. (re-find #"[0-9]*" (:id task-opts)))])))

  (delete-item
    [this id]
    (jdbc/delete! db/db-map
                  :tasks
                  ["id=?" id]))

  )
