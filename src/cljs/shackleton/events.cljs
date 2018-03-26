(ns shackleton.events
  (:require [re-frame.core :as rf]
            [shackleton.db :as db]
            [shackleton.hash :as hash]))

(rf/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-db
  :delete-el
  (fn [db [_ dispatching-el]]
    (assoc db :elements
              (remove (fn [el] (= (db/element-hash dispatching-el) (db/element-hash el)))
                      (:elements db)))))

(rf/reg-event-db
  :add-el
  (fn [db [_ title x y link]]
    (assoc db :elements
              (conj (:elements db) {:title title
                                    :x x
                                    :y y
                                    :link link}))))

(rf/reg-event-db
  :import-db
  (fn [db [_ db-hash]]
    (let [state (hash/decode db-hash)]
      (cljs.reader/read-string state))))
