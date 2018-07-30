(ns shackleton.svg.element
  (:require [shackleton.db :as db]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(def info-box-w 120)
(def info-box-hw (/ info-box-w 2))
(def info-box-h 50)
(def info-box-hh (/ info-box-h 2))
(def info-box-marker-stroke 1)

(def font-awesome (r/adapt-react-class (aget js/window "deps" "react-fontawesome")))

(defn tt-first-row [title show? el]
  [:div.elem-headline
   [:text title]
   (when @show?
     [:div {:on-click (fn [] (rf/dispatch [:delete-el el]))}
      [font-awesome {:name "times"}]])])

(defn tooltip [{:keys [title link x y z] :as el} color-intensity show?]
  (let [background-color (str "rgb(" (- 255 color-intensity) "," (- 255 color-intensity) "," 255 ")")]
    [:foreignObject {:x 0 :y 0 :width info-box-w :height (if @show? 250 50)}
     [:div.element {:style {:background (str "linear-gradient(to bottom left, " background-color ", white, white)")}
                           :on-mouse-enter (fn [] (reset! show? true))
                           :on-mouse-leave (fn [] (reset! show? false))}
      [tt-first-row title show? el]
      (when @show?
        [:div.coords {:style {:display :flex :flex-direction "column"}}
         [:text (str "x: " x)]
         [:text (str "y: " y)]
         [:text (str "z: " z)]])]]))

(defn pointing-lines [svgx svgy hw hh]
  [:g
   [:line {:x1 svgx :y1 svgy :x2 hw :y2 svgy :stroke "rgb(0,0,0)" :stroke-dasharray "10,10" :stroke-width info-box-marker-stroke}]
   [:line {:x1 svgx :y1 svgy :x2 svgx :y2 hh :stroke "rgb(0,0,0)" :stroke-dasharray "10,10" :stroke-width info-box-marker-stroke}]])

(defn element [hw hh x->svg y->svg z->color {:keys [title link x y z] :as el}]
  (let [show? (r/atom false)]
    (fn [hw hh x->svg y->svg z->color {:keys [title link x y z] :as el}]
      (let [svgx (x->svg x)
            svgy (y->svg y)
            color-intensity (z->color z)]
        [:g
         [pointing-lines svgx svgy hw hh]
         [:g {:transform (str "translate(" (- svgx info-box-hw) "," (- svgy 20) ")") :width info-box-w :height info-box-h}
          [tooltip el color-intensity show?]
          ]]))))