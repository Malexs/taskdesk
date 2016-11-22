(ns taskdesk.core
  (:use compojure.core)
  (:require [taskdesk.dal.db :as db]
            [taskdesk.views.view :as view]
            [taskdesk.dal.dao.user-data-access-object :as user-dao]
            [taskdesk.dal.dao.task-data-access-object :as task-dao]
            [taskdesk.dal.dao.group-data-access-object :as group-dao]
            [taskdesk.dal.dao.status-data-access-object :as stat-dao]
            [taskdesk.bll.services.user-service :as user-service-d]
            [taskdesk.bll.services.task-service :as task-service-d]
            [taskdesk.bll.services.group-service :as group-service-d]
            [taskdesk.bll.services.status-service :as stat-service-d]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [ring.util.response :as response]))

;Users
(def usr-dao (user-dao/->user-data-access-object db/db-map))
(def user-service (user-service-d/->user-service usr-dao))
;Tasks
(def tsk-dao (task-dao/->task-data-access-object))
(def task-servise (task-service-d/->task-service tsk-dao))
;Groups
(def grp-dao (group-dao/->group-data-access-object))
(def group-service (group-service-d/->group-service grp-dao))
;Statuses
(def stt-dao (stat-dao/->status-data-access-object))
(def stat-service (stat-service-d/->status-service stt-dao))
;Common
(def logged false)
(def stats (.get-all-items stat-service))

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
    (when (true? logged)
      (.get-all-items usr-dao)
      (view/render-user-page (.get-user-by-login user-service login)))))

(defroutes app-routes
           (GET "/" [] (response/redirect "/home"))
           (GET "/home" [] (view/render-home-page))
           (GET "/auth" [] (view/render-signin-page))
           (POST "/auth" request (post-auth request))
           (GET "/signup" [] (view/render-signup-page))
           (POST "/signup" request (add-user request))
           (GET "/user/:login" [login] (show-user-page login))
           (GET "/user/logoff" [] (def logged false)
                                  (response/redirect "/home"))

           (GET "/taskdesk" [] (view/render-taskdesk-page (.get-all-items task-servise)
                                                          (.get-all-items group-service)))
           (GET "/taskdesk/task/:id" [id] (view/render-edit-task (.get-by-id task-servise id)
                                                                 (.get-all-items user-service)
                                                                 (.get-all-items grp-dao)
                                                                 stats))
           (GET "/taskdesk/task/new" [] (view/render-edit-task nil
                                                               (.get-all-items user-service)
                                                               (.get-all-items grp-dao)
                                                               stats))
           (POST "/taskedit" request (.edit-task task-servise request)
                                     (response/redirect "/taskdesk"))
           (POST "/taskadd" request (.add-item task-servise request)
                                    (response/redirect "/taskdesk"))
           (route/not-found "Page not found"))

(def engine
  (-> (handler/site app-routes) (middleware/wrap-json-body {:keywords? true})))
