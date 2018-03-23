(ns shackleton.views
  (:require [re-frame.core :as re-frame]
            [shackleton.subs :as subs]
            ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div "Hello from " @name]))
