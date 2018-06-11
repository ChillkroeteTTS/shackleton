(ns shackleton.db)

(def default-dev-db
  {:elements [{:title "test1"
               :link  "http://dummmy"
               :x     50
               :y     200
               :z     50}
              {:title "test2"
               :link  "http://dummmy2"
               :x     -30
               :y     -150
               :z     0}
              {:title "test3"
               :link  "http://dummmy3"
               :x     -40
               :y     -120
               :z     25}]
   :window-dimension {:w 800 :h 600}})

(def default-db
  (assoc default-dev-db :elements []))

(defn element-hash [{:keys [title x y z]}]
  (hash (str title x y z)))