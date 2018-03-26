(ns shackleton.hash)

(def base64 (aget js/window "deps" "base64"))

(defn encode [str]
  (.encode base64 str))

(defn decode [str]
  (.decode base64 str))