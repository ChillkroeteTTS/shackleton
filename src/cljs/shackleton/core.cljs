(ns shackleton.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [shackleton.events :as events]
            [shackleton.views :as views]
            [shackleton.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (rf/clear-subscription-cache!)
  (r/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
