(ns taskdesk.dal.models.task-data-access-object)

(defrecord task-record
  [;;unchangable
     id
     author
     date
   ;;changable
     title
     description
     milestone
     label
     assignee
     status])