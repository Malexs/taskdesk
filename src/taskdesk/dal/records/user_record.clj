(ns taskdesk.dal.records.user-record)

(defrecord user-record
  [id
   login
   password
   name
   email
   role
   karma])