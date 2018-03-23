(ns shackleton.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]  
            [cljs.core.async :refer [chan close! go-loop <!]]
            [shackleton.events :as events]
            [shackleton.views :as views]
            [shackleton.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

;;var c = document.getElementById("myCanvas");
;;var ctx = c.getContext("2d");
;;ctx.font = "30px Arial";
;;ctx.strokeText("Hello World", 10, 50);
;;context.clearRect(0, 0, canvas.width, canvas.height)
(defn timeout [ms]
  (let [c (chan)]
    (js/setTimeout (fn [] (close! c)) ms)
    c))

(defn draw-loop []
  (go-loop []
           (let [_ (<! (timeout 500))
                 c (.getElementById js/document "kartesian")
                 ctx (.getContext c "2d")]
             (.clearRect ctx 0 0 (.-width c) (.-height c))
             (set! (.-font ctx) "20px Arial")
             (.strokeText ctx "Hello World" 10 50)
             (recur))))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (draw-loop)
  (mount-root))
