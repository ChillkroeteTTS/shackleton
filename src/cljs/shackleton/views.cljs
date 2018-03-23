(ns shackleton.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [shackleton.subs :as subs]
            [shackleton.svg.coordinate-system :as svgcs]
            [shackleton.svg.element :as el]
            [shackleton.db :as db]))

(def svg-height 1)

(defn kart-x->svg [w x-max x] (* (/ (+ x-max x) (* x-max 2)) w))
(defn kart-y->svg [h y-max y] (* (- 1 (/ (+ y-max y) (* y-max 2))) h))

(def create-dialog (r/atom false))

(defn matrix [x-max]
  (let [elements (rf/subscribe [:elements])
        selected-element (r/atom nil)]
    (fn []
      (let [x-max (Math/abs (apply max (map :x @elements)))
            y-max (Math/abs (apply max (map :y @elements)))
            w js/innerWidth
            h (* svg-height js/innerHeight)
            hw (/ w 2)
            hh (/ h 2)
            x-max (* x-max 1.2)
            y-max (* y-max 1.2)
            x->svg (partial kart-x->svg w x-max)
            y->svg (partial kart-y->svg h y-max)]
        [:section {:id "kartesian" :style {:position :absolute :top 0 :left 0 :right 0 :height (str (int (* 100 svg-height)) "%") :background-color "white"}}
         [:svg {:style {:width "100%" :height "100%"}}
          [svgcs/background w h x-max y-max]
          (into [:g] (map (fn [el] [el/element-transducer hw hh x->svg y->svg el]) @elements))]
         ]))))
(defn bind-to-fn [atom] (fn [e] (reset! atom (.. e -target -value))))

(defn creation-dialog []
  (let [title-i (r/atom "")
        x-i (r/atom "")
        y-i (r/atom "")
        link-i (r/atom "")]
    (fn []
      [:div {:style {:position      :absolute :bottom 30 :right 30 :display :flex :flex-direction "column" :border "solid 1px rgb(186, 186, 186)"
                     :padding       "5px"
                     :border-radius 8}}
       [:text "Title"]
       [:input {:value @title-i :on-input (bind-to-fn title-i)}]
       [:div {:style {:flex-direction "row"}}
        [:text {:style {:margin-right 2.5}} "x"] [:input {:value @x-i :on-input (bind-to-fn x-i)}]
        [:text {:style {:margin "0 2.5px 0 2.5px"}} "y"] [:input {:value @y-i :on-input (bind-to-fn y-i)}]]
       [:text "Link (optional)"]
       [:input {:value @link-i :on-input (bind-to-fn link-i)}]
       [:div {:style {:display "flex" :flex-direction "row"}}
        [:button {:style    {:border-radius 10 :font-size 43 :width 63}
                  :on-click (fn [] (rf/dispatch [:add-el @title-i
                                                 (cljs.reader/read-string @x-i)
                                                 (cljs.reader/read-string @y-i)
                                                 @link-i]) (reset! create-dialog false))} "o"]
        [:button {:style    {:border-radius 10 :font-size 43 :width 63}
                  :on-click (fn [] (reset! create-dialog false))} "x"]]])))

(defn main-panel []
  [:div {:style {:display :flex :flex-direction :column}}
   [matrix]
   (if @create-dialog
     [creation-dialog]
     [:button {:style    {:position      :absolute :bottom 30 :right 30
                          :border-radius 10 :font-size 43 :width 63}
               :on-click (fn [] (reset! create-dialog true))}
      "+"])]
  )
