(ns shackleton.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [re-frame.db :as rfdb]
            [cljs.source-map.base64 :as b64]
            [shackleton.subs :as subs]
            [shackleton.svg.coordinate-system :as svgcs]
            [shackleton.svg.element :as el]
            [shackleton.db :as db]))

(def svg-height 1)
(def font-awesome (r/adapt-react-class (aget js/window "deps" "react-fontawesome")))

(defn kart-x->svg [w x-max x] (* (/ (+ x-max x) (* x-max 2)) w))
(defn kart-y->svg [h y-max y] (* (- 1 (/ (+ y-max y) (* y-max 2))) h))

(defn matrix []
  (let [elements (rf/subscribe [:elements])]
    (fn []
      (let [x-max (apply max (map (fn [el] (Math/abs (:x el))) @elements))
            y-max (apply max (map (fn [el] (Math/abs (:y el))) @elements))
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
          (into [:g]
                (map (fn [el] [el/element hw hh x->svg y->svg el]) @elements))]
         ]))))

(defn bind-to-fn [atom] (fn [e] (reset! atom (.. e -target -value))))

(defn add-element-dialog [active-dialog]
  (let [title-i (r/atom "")
        x-i (r/atom "")
        y-i (r/atom "")
        link-i (r/atom "")
        valid-i? (r/atom false)
        validate-i-fn (fn [fn] (reset! valid-i? (not (or (empty? @x-i) (empty? @y-i)))) fn)
        input-changed-fn (comp validate-i-fn bind-to-fn)]
    (fn [active-dialog]
      [:div.dialog.add-element
       [:div.label-input-gr
        [:text "Title"]
        [:input {:value @title-i :on-input (input-changed-fn title-i)}]]
       [:div.label-input-gr
        [:text "x"]
        [:input {:value @x-i :on-input (input-changed-fn x-i)}]]
       [:div.label-input-gr
        [:text "y"]
        [:input {:value @y-i :on-input (input-changed-fn y-i)}]]
       [:div.label-input-gr
        [:text "Link (optional)"]
        [:input {:value @link-i :on-input (input-changed-fn link-i)}]]
       [:div.bottom-button-cont
        [:button (merge {:on-click (fn [] (rf/dispatch [:add-el @title-i
                                                        (cljs.reader/read-string @x-i)
                                                        (cljs.reader/read-string @y-i)
                                                        @link-i]) (reset! active-dialog nil))}
                        (if @valid-i? {} {:disabled "true"}))
         [font-awesome {:name "plus"}]]
        [:button {:on-click (fn [] (reset! active-dialog nil))}
         [font-awesome {:name "times"}]]]])))

(defn share-dialog [active-dialog]
  (let [db-hash (rf/subscribe [:db-as-hash])
        i-db-hash (r/atom "")]
    (fn [active-dialog]
      [:div.dialog
       [:div.label-input-gr
        [:text "Export"]
        [:text.explanation "Use this text to restore your session below."]
        [:textarea {:id "state-hash" :value @db-hash :read-only true :style {:resize :none :background-color "#EEEEEE"} :rows 10}]]
       [:div.label-input-gr
        [:text "Import"]
        [:textarea {:style {:resize :none} :on-input (bind-to-fn i-db-hash) :rows 10}]]
       [:button {:disabled (empty? @i-db-hash)
                 :on-click (fn []
                             (rf/dispatch [:import-db @i-db-hash])
                             (reset! i-db-hash "")
                             (reset! active-dialog nil))}
        [font-awesome {:name "download"}]]])))

(defn main-panel []
  (let [active-dialog (r/atom nil)
        toggle-fn (fn [key]
                    (fn [] (if (= key @active-dialog) (reset! active-dialog nil) (reset! active-dialog key))))]
    (fn []
      [:div {:style {:display :flex :flex-direction :column}}
       [matrix]
       (cond
         (= :add @active-dialog) [add-element-dialog active-dialog]
         (= :share @active-dialog) [share-dialog active-dialog])
       [:div.button-bar
        [:button {:on-click (toggle-fn :share)}
         [font-awesome {:name "share"}]]
        [:button {:on-click (toggle-fn :add)}
         [font-awesome {:name "plus"}]]]]))
  )
