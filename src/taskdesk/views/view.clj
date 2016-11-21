(ns taskdesk.views.view
  (:use hiccup.page
        hiccup.element)
  (:require [taskdesk.views.renderer :as renderer]))

(defn render-home-page
  []
  (renderer/render "home.html" {:docs "document"}))

(defn render-signin-page
  []
  (renderer/render "auth.html"))

(defn render-user-page
  [user]
  (renderer/render "user.html" {:user user}))

(defn render-signup-page
  []
  (renderer/render "signup.html"))

(defn render-taskdesk-page
  [tasks groups]
  (renderer/render "taskdesk.html" {:tasks tasks :groups groups}))

(defn render-edit-task
  [task users groups]
  (renderer/render "taskedit.html" {:task task :users users :groups groups}))
