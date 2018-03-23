(ns shackleton.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :elements
 (fn [db]
   (:elements db)))
