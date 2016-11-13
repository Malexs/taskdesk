(ns taskdesk.dal.protocols.user-db-protocol)

(defprotocol user-db-protocol
  (sign-in [this login password])
  (sign-up [this user-info]))