(ns taskdesk.bll.services.log-service
  (:import (java.util Date)))

(def root-dir "C:\\Uni\\clojure\\taskdesk\\logs.txt")

(defn now []
  (.. (Date.) (toString)))

(def log-agent (agent (clojure.java.io/writer root-dir :append true)))

(defn write-out [writer message]
  (.write writer message)
  writer)

(defn log-message [logger message]
  (send logger write-out message))

(defn close [writer]
  (send writer #(.close %)))

(defn close-log []
  (send log-agent close))

(defn flush-and-return [writer]
  (.flush writer))

(defn flush []
  (send log-agent flush-and-return)
  log-agent)

(defn log-signin [user-name]
  (when (not-empty (str user-name))
    (do (log-message log-agent (str "User: ("
                                    user-name
                                    ") signed in on ("
                                    (now)
                                    ")\r\n"))
        )))

(defn log-task-edit
  [user-name task-id edit-mode]
  (when (not-empty (str user-name))
    (do
      (if edit-mode
        (log-message log-agent (str "User: ("
                                    user-name
                                    ") edited task ("
                                    task-id
                                    ") on ("
                                    (now)
                                    ")\r\n"))
        (log-message log-agent (str "User: ("
                                    user-name
                                    ") added task ("
                                    task-id
                                    ") on ("
                                    (now)
                                    ")\r\n")))
      )))

(defn log-task-delete
  [user-name task-id]
  (when (not-empty (str user-name))
    (do
      (log-message log-agent (str "User: ("
                                  user-name
                                  ") deleted task with id: ("
                                  task-id
                                  ") on ("
                                  (now)
                                  ")\r\n"))
      )))
