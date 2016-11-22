(ns taskdesk.bll.protocols.task-service-protocol)

(defprotocol task-service-protocol
  (edit-task [this task-opts])
  (get-by-id [this id]))