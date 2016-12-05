(ns taskdesk.views.view
  (:use hiccup.page
        hiccup.element)
  (:require [taskdesk.views.renderer :as renderer]
            [taskdesk.bll.invoice-center :as ic]))

(defn render-home-page
  [session]
  (let [invoices (ic/get-invoices (:name session))
        invoice-count (count invoices)]
    (renderer/render "home.html" {:docs "document" :session session :invoices invoice-count})))

(defn render-signin-page
  []
  (renderer/render "auth.html"))

(defn render-user-page
  [user session]
  (let [invoices (ic/get-invoices (:name session))
        invoice-count (count invoices)]
    (renderer/render "user.html" {:user user :session session :invoices invoice-count})))

(defn render-signup-page
  []
  (renderer/render "signup.html"))

(defn render-taskdesk-page
  [tasks groups user]
  (let [invoices (ic/get-invoices (:name user))
        invoice-count (count invoices)]
    (renderer/render "taskdesk.html" {:tasks tasks :groups groups :session user :invoices invoice-count})))

(defn render-edit-task
  [task users groups stats session]
  (let [invoices (ic/get-invoices (:name session))
        invoice-count (count invoices)]
    (renderer/render "taskedit.html" {:task task :users users :groups groups :stats stats :session session :invoices invoice-count})))

(defn render-edit-group
  [group session]
  (let [invoices (ic/get-invoices (:name session))
        invoice-count (count invoices)]
    (renderer/render "groupedit.html" {:group group :session session :invoices invoice-count})))

(defn render-invoice-page
  [session]
  (let [invoices (ic/get-invoices (:name session))
        invoice-count (count invoices)]
    (renderer/render "invoices.html" {:invoices invoice-count :invoice-list invoices :session session})))
