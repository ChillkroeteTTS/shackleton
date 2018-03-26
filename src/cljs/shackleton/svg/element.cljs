(ns shackleton.svg.element
  (:require [shackleton.db :as db]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(def info-box-w 120)
(def info-box-hw (/ info-box-w 2))
(def info-box-h 50)
(def info-box-hh (/ info-box-h 2))
(def info-box-marker-stroke 1)

(defn tt-first-row [title show? el]
  [:div {:style {:display :flex :flex-direction "row"}}
   [:text {:style {:font-weight "bold" :flex-grow 2 :align-self "flex-start"}}
    title]
   (when @show?
     [:g
      ;;[:button {:class "edit-button" :style {}} "e"]
      [:button {:class    "delete-button"
                :on-click (fn [] (rf/dispatch [:delete-el el]))} "x"]])])

(defn tooltip [{:keys [title link x y] :as el} show?]
  [:foreignObject {:x 0 :y 0 :width info-box-w :height (if @show? 200 50)}
   [:div {:style          {:background-color "#FFFFFF" :display :flex :flex-direction "column"
                           :justify-content  "flex-end" :border-radius 3
                           :border           "solid 1px" :padding 6}
          :on-mouse-enter (fn [] (reset! show? true))
          :on-mouse-leave (fn [] (reset! show? false))}
    [tt-first-row title show? el]
    (when @show?
      [:div {:style {:display :flex :flex-direction "column"}}
       [:text (str x)]
       [:text (str y)]])]])

(defn pointing-lines [svgx svgy hw hh]
  [:g
   [:line {:x1 svgx :y1 svgy :x2 hw :y2 svgy :stroke "rgb(0,0,0)" :stroke-dasharray "10,10" :stroke-width info-box-marker-stroke}]
   [:line {:x1 svgx :y1 svgy :x2 svgx :y2 hh :stroke "rgb(0,0,0)" :stroke-dasharray "10,10" :stroke-width info-box-marker-stroke}]])

(defn element [hw hh x->svg y->svg {:keys [title link x y] :as el}]
  (let [show? (r/atom false)]
    (fn [hw hh x->svg y->svg {:keys [title link x y] :as el}]
      (let [svgx (x->svg x)
            svgy (y->svg y)]
        [:g
         [pointing-lines svgx svgy hw hh]
         [:g {:transform (str "translate(" (- svgx info-box-hw) "," (- svgy 12) ")") :width info-box-w :height info-box-h}
          [tooltip el show?]
          ]]))))