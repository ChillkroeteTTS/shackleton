(ns shackleton.views
  (:require [re-frame.core :as re-frame]
            [shackleton.subs :as subs]
            ))

(def svg-height 0.8)

(defn x->svg [swidth x] (+ (* x (/ swidth 2.0)) (/ swidth 2.0)))
(defn y->svg [sheight y] (+ (* y (/ sheight 2.0)) (/ sheight 2.0)))

(defn kartesian []
  (let [w js/innerWidth
        h (* svg-height js/innerHeight)
        hw (/ w 2)
        hh (/ h 2)
        x->svg (partial x->svg w)
        y->svg (partial y->svg h)
        center {:x (x->svg 0) :y (y->svg 0)}]
    [:section {:id "kartesian" :style {:position :absolute :top 0 :width "100%" :height (str (int (* 100 svg-height)) "%") :background-color "#EEEEEE"}}
     [:svg {:style {:width "100%" :height "100%"}}
      [:line {:x1 hw :y1 h :x2 hw :y2 0 :stroke "rgb(0,0,0)" :stroke-width 1}]
      [:line {:x1 0 :y1 hh :x2 w :y2 hh :stroke "rgb(0,0,0)" :stroke-width 1}]
      [:circle {:cx hw :cy h :r 20}]
      ]]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div {:style {:display :flex :flex-direction :column}}
     [:div "Hello from " @name]
     [kartesian]
     [:section {:id "menu" :style {:height "20%" :position :absolute :bottom 0}}]]))
