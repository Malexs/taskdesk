(ns taskdesk.dal.protocols.task-db-protocol)

(defprotocol task-db-protocol
  (add-task [this task-opts])
  (edit-task [this task-opts]))
