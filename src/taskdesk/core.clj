(ns taskdesk.core
  (:use compojure.core)
  (:require [taskdesk.dal.db :as db]
            [taskdesk.views.view :as view]
            [taskdesk.dal.dao.user-data-access-object :as user-db-model]
            [taskdesk.bll.services.user-service :as user-service-d]
            [clojure.java.jdbc :as jdbc]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [ring.util.response :as response]))

(def user-model (user-db-model/->user-data-access-object db/db-map))
(def user-service (user-service-d/->user-service user-model))
(def logged false)

(defn post-auth
  [request]
  (let [current-user (.sign-in user-service
                               (get-in request [:params :login])
                               (get-in request [:params :password]))
        request-login (get-in request [:params :login])]

       (if (= (:count current-user) 0)
         (println "We dont have such user")
         (do
             (def logged true)
             (response/redirect (str "user/" request-login))))
      ))

(defn add-user
  [request]
  (.sign-up user-service (:params request)))

(defn show-user-page
  [login]
  (do
    (if (true? logged)
      (view/render-user-page (.get-user-by-login user-service login)))))

(defroutes app-routes
           (GET "/" [] (view/render-home-page))
           (GET "/auth" [] (view/render-signin-page))
           (POST "/auth" request (post-auth request))
           (GET "/signup" [] (view/render-signup-page))
           (POST "/signup" request (add-user request))
           (GET "/user/:login" [login] (show-user-page login))
           (GET "/user/logoff" [] (do (def logged false)
                                      (response/redirect "/")))
           (route/not-found "Page not found"))

(def engine
  (-> (handler/site app-routes) (middleware/wrap-json-body {:keywords? true})))
