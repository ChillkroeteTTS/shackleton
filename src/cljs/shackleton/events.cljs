(ns shackleton.events
  (:require [re-frame.core :as rf]
            [shackleton.db :as db]))

(rf/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-db
  :delete-el-by-hash
  (fn [db [_ hash]]
    (assoc db :elements
              (remove (fn [el] (= hash (db/element-hash el)))
                      (:elements db)))))