(ns taskdesk.dal.protocols.common-db-protocol)

(defprotocol common-db-protocol
  (get-all-items [this])
  (add-item [this options]))