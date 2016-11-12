(ns taskdesk.core
     (:use compojure.core)
     (:require [taskdesk.dal.db :as db]
               [clojure.java.jdbc :as jdbc]
               [net.cgrand.enlive-html :as html]
               [compojure.route :as route]
               [compojure.handler :as handler]))


(html/deftemplate view-index "taskdesk/index.html" [ctxt]
    [:title] (html/content "My taskdesk")
    [:#old]  (html/content (:old ctxt)))

(defn index
  "I don't do a whole lot."
  []
  (let [val (jdbc/query db/db-map ["SELECT * FROM users"])]
    (view-index {:old val})))

(defroutes app-routes
    (GET "/" [] (index))
    (route/not-found "Page not found"))

(def engine
  (handler/site app-routes))
