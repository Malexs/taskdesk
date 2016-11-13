(ns taskdesk.core
  (:use compojure.core)
  (:require [taskdesk.dal.db :as db]
            [taskdesk.views.view :as view]
            [taskdesk.dal.models.user-db-model :as user-db-model]
            [taskdesk.bll.services.user-service :as user-service-d]
            [clojure.java.jdbc :as jdbc]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [ring.util.response :as response]))

(def user-model (user-db-model/->user-db-model db/db-map))
(def user-service (user-service-d/->user-service user-model))

(defn post-auth
  [request]
  (let [current-user (.sign-in user-service
                               (get-in request [:params :login])
                               (get-in request [:params :password]))]

         (if (= current-user nil)
           (println "We dont have such user")
           (response/redirect (str "user/" (:login current-user))))
      ))

(defn add-user
  [request]
  (.sign-up user-service (:params request)))

(defroutes app-routes
           (GET "/" [] (view/render-home-page))
           (GET "/auth" [] (view/render-signin-page))
           (POST "/auth" request (post-auth request))
           (GET "/signup" [] (view/render-signup-page))
           (POST "/signup" request (add-user request))
           (GET "/user/:login" [login] (view/render-user-page (.sign-in user-service login "admin")))
           (route/not-found "Page not found"))

(def engine
  (-> (handler/site app-routes) (middleware/wrap-json-body {:keywords? true})))
