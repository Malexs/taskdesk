(ns taskdesk.dal.dao.invoice-dao
  (:use [taskdesk.dsl.execs]
        [taskdesk.dsl.struct-func]
        [taskdesk.dsl.renders])
  (:require [clojure.java.jdbc :as jdbc]
            [taskdesk.dal.protocols.invoice-protocol :as ip]
            [taskdesk.dal.db :as db]))

(deftype invoice-dao []

  ip/invoice-protocol

  (save-invoice
    [this inv-info]
    (jdbc/insert! db/db-map
                  :invoices
                  {:user (:user inv-info)
                   :localid (:id inv-info)
                   :text (:text inv-info)}))

  (watch-invoice
    [this user id]
    (jdbc/execute! db/db-map
                   ["UPDATE invoices SET watched=1
                    WHERE user=? AND localid=?"
                    user
                    (Integer. (re-find #"[0-9]*" id))]))

  (get-invoices
    [this user]
    (into [] (fetch (select (fields {:id :localid
                                     :text :text})
                            (from :invoices)
                            (where (and (== :user user) (== :watched 0)))))))
  )