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
