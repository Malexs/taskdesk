(ns taskdesk.bll.protocols.common-service-protocol)

(defprotocol common-service-protocol
  (get-all-items [this])
  (add-item [this options]))