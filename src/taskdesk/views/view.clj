(ns taskdesk.views.view
  (:use hiccup.page
        hiccup.element)
  (:require [taskdesk.views.renderer :as renderer]))

(defn render-home-page
  [val]
  (renderer/render "home.html" {:docs "document" :something val}))

(defn render-auth-page
  []
  (renderer/render "home.html"))

(defn render-user-page
  [user]
  (renderer/render "user.html" {:user user}))
