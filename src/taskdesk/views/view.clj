(ns taskdesk.views.view
  (:use hiccup.page
        hiccup.element)
  (:require [taskdesk.views.renderer :as renderer]))

(defn render-home-page
  [val]
  (renderer/render "index.html" {:docs "document" :something val}))
