(ns taskdesk.bll.protocols.task-service-protocol)

(defprotocol task-service-protocol
  (add-task [this task-opts])
  (edit-task [this task-opts])
  (get-by-id [this id]))