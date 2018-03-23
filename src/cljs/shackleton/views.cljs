(ns shackleton.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [shackleton.subs :as subs]
            [shackleton.svg.coordinate-system :as svgcs]
            [shackleton.db :as db]))

(def svg-height 0.8)

(defn kart-x->svg [w x-max x] (* (/ (+ x-max x) (* x-max 2)) w))
(defn kart-y->svg [h y-max y] (* (- 1 (/ (+ y-max y) (* y-max 2))) h))

(def info-box-w 120)
(def info-box-hw (/ info-box-w 2))
(def info-box-h 50)
(def info-box-hh (/ info-box-h 2))
(def info-box-marker-stroke 1)

(def selected-element (r/atom nil))

(defn matrix []
  (let [elements (rf/subscribe [:elements])
        w js/innerWidth
        h (* svg-height js/innerHeight)
        hw (/ w 2)
        hh (/ h 2)
        x->svg (partial kart-x->svg w 500)
        y->svg (partial kart-y->svg h 300)
        center {:x (x->svg 0) :y (y->svg 0)}]
    [:section {:id "kartesian" :style {:position :absolute :top 0 :left 0 :right 0 :height (str (int (* 100 svg-height)) "%") :background-color "white"}}
     (into [] (concat [:svg {:style {:width "100%" :height "100%"}}]
                      (svgcs/background w h)
                      (reduce (fn [agg {:keys [title link x y] :as el}]
                                (let [svgx (x->svg x)
                                      svgy (y->svg y)
                                      hash (db/element-hash el)
                                      hovered? (= hash @selected-element)]
                                  (conj agg
                                        [:line {:x1 svgx :y1 svgy :x2 hw :y2 svgy :stroke "rgb(0,0,0)" :stroke-width info-box-marker-stroke}]
                                        [:line {:x1 svgx :y1 svgy :x2 svgx :y2 hh :stroke "rgb(0,0,0)" :stroke-width info-box-marker-stroke}]
                                        [:g {:transform (str "translate(" (- svgx info-box-hw) "," (- svgy 12) ")") :width info-box-w :height info-box-h}
                                         [:foreignObject {:x 0 :y 0 :width info-box-w :height 50}
                                          (into [] (concat
                                                     [:div {:style          {:background-color "#EEEEEE" :display :flex :flex-direction "column"
                                                                             :justify-content  "flex-end"
                                                                             :border           "solid 1px" :padding 2}
                                                            :on-mouse-enter (fn [] (reset! selected-element hash))
                                                            :on-mouse-leave (fn [] (reset! selected-element nil))}
                                                      (into [] (concat
                                                                 [:div {:style {:display :flex :flex-direction "row"}}
                                                                  [:text {:style {:flex-grow 2 :align-self "flex-start"}} title]]
                                                                 (if hovered?
                                                                   [[:button {:class "edit-button" :style {}} "e"]
                                                                    [:button {:class "delete-button"
                                                                              :on-click (fn [] (rf/dispatch [:delete-el-by-hash hash]))} "x"]]
                                                                   )))]
                                                      (if hovered?
                                                        [[:text (str x)]
                                                         [:text (str y)]]
                                                        [])))]
                                         ])))
                              [] @elements)))

     ]))

(defn main-panel []
  [:div {:style {:display :flex :flex-direction :column}}
   [matrix]
   [:section {:id "menu" :style {:height "20%" :left 0 :right 0 :position :absolute :bottom 0 :background-color "#EEEEEE"}}
    [:text "hello"]]])
