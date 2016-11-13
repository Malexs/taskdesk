(ns taskdesk.core
  (:use compojure.core)
  (:require [taskdesk.dal.db :as db]
            [taskdesk.views.view :as view]
            [taskdesk.dal.models.user-db-model :as user-db-model]
            [taskdesk.bll.services.user-service :as user-service-d]
            [clojure.java.jdbc :as jdbc]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]))

(def user-model (user-db-model/->user-db-model db/db-map))
(def user-service (user-service-d/->user-service user-model))


(defn index
  "I just show main page."
  []
  (let [val (jdbc/query db/db-map ["SELECT * FROM users"])]
    (view/render-home-page (first val))
    ))

(defn user
  "I show single user page"
  [login]
  (view/render-user-page (.sign-in user-service login "admin")))

(defroutes app-routes
           (GET "/" [] (index))
           (GET "/user/:login" [login] (user login))
           (route/not-found "Page not found"))

(def engine
  (-> (handler/site app-routes) (middleware/wrap-json-body {:keywords? true})))
