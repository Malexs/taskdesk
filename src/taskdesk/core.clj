(ns taskdesk.core
  (:use compojure.core)
  (:require [taskdesk.dal.db :as db]
            [clojure.java.jdbc :as jdbc]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [taskdesk.views.view :as view]
            [ring.middleware.json :as middleware]))



(defn index
  "I don't do a whole lot."
  []
  (let [val (jdbc/query db/db-map ["SELECT * FROM users"])]
    (view/render-home-page {:user val})
    ))

(defn user
  []
  (let [val (jdbc/query db/db-map ["SELECT * FROM users"])]
    (view/render-user-page {:user val})
    ))

(defroutes app-routes
           (GET "/" [] (index))
           (GET "/user" [] (user))
           (route/not-found "Page not found"))

(def engine
  (-> (handler/site app-routes) (middleware/wrap-json-body {:keywords? true})))
