(ns shackleton.views
  (:require [re-frame.core :as re-frame]
            [shackleton.subs :as subs]
            ))



(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div {:style {:display :flex :flex-direction :column}}
     [:div "Hello from " @name]
     [:canvas {:id "kartesian" :style {:position :absolute :top 0 :width "100%" :height "80%" :background-color "red"}}
      ]
     [:section {:id "menu" :style {:height "20%" :position :absolute :bottom 0}}]]))
