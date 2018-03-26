(ns shackleton.subs
  (:require [re-frame.core :as re-frame]
            [shackleton.hash :as hash]))

(re-frame/reg-sub
 :elements
 (fn [db]
   (:elements db)))

(re-frame/reg-sub
 :db-as-hash
 (fn [db]
   (hash/encode db)))


