(ns shackleton.db)

(def default-db
  {:elements [{:title "test1"
               :link  "http://dummmy"
               :x     50
               :y     200}
              {:title "test2"
               :link  "http://dummmy2"
               :x     -30
               :y     -150}]})

(defn element-hash [{:keys [title x y]}]
  (hash (str title x y)))