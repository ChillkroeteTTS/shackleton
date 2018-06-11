(ns shackleton.events
  (:require [re-frame.core :as rf]
            [shackleton.db :as db]
            [shackleton.hash :as hash]
            [cljs.reader :as cljsr]))

(rf/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-db
 ::initialize-dev-db
 (fn  [_ _]
   db/default-dev-db))

(rf/reg-event-db
  :delete-el
  (fn [db [_ dispatching-el]]
    (assoc db :elements
              (remove (fn [el] (= (db/element-hash dispatching-el) (db/element-hash el)))
                      (:elements db)))))

(rf/reg-event-db
  :add-el
  (fn [db [_ title x y z link]]
    (assoc db :elements
              (conj (:elements db) {:title title
                                    :x x
                                    :y y
                                    :z z
                                    :link link}))))

(rf/reg-event-db
  :import-db
  (fn [db [_ db-hash]]
    (let [state (cljsr/read-string (hash/decode db-hash))]
      (assoc state :window-dimension (:window-dimension db))))) ;; retain window size

(rf/reg-event-db
  :set-window-dimension
  (fn [db [_ dimension]]
    (assoc db :window-dimension dimension)))

