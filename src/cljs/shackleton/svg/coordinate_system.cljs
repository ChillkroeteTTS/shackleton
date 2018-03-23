(ns shackleton.svg.coordinate-system)

(def disc-marks-ps 7)
(def disc-mark-height 5)
(def disc-mark-text-gap 7)
(def disc-mark-stroke 1)

(defn svg->kart-x [hw x-max x] (- (* (/ x (* 2 hw)) x-max 2) x-max))
(defn svg->kart-y [hh y-max y] (- (- (* (/ y (* 2 hh)) y-max 2) y-max)))

(defn disc-marks [hw x-max hh y-max operator]
  (concat (reduce (fn [agg i]
                    (let [x (operator hw (* (+ 1 i) (/ hw disc-marks-ps)))]
                      (conj agg
                            [:line {:x1 x :y1 (- hh disc-mark-height) :x2 x :y2 (+ hh disc-mark-height) :stroke "rgb(0,0,0)" :stroke-width disc-mark-stroke}]
                            [:text {:x x :y (+ hh disc-mark-height disc-mark-text-gap) :text-anchor "middle" :alignment-baseline "before-edge"} (str (int (svg->kart-x hw x-max x)))])))
                  [] (range (+ 1 disc-marks-ps)))
          (reduce (fn [agg i]
                 (let [y (operator hh (* (+ 1 i) (/ hh disc-marks-ps)))]
                   (conj agg
                     [:line {:x1 (- hw disc-mark-height) :y1 y :x2 (+ hw disc-mark-height) :y2 y :stroke "rgb(0,0,0)" :stroke-width disc-mark-stroke}]
                     [:text {:x (- hw disc-mark-height disc-mark-text-gap) :y y :text-anchor "end" :alignment-baseline "middle"} (str (int (svg->kart-y hh y-max y)))])))
               [] (range disc-marks-ps))))
(defn disc-mark-cross [hw hh]
  (let [diag-fn (fn [hd op] (op hd disc-mark-height))]
    [
     [:line {:x1 (diag-fn hw -) :y1 (diag-fn hh -) :x2 (diag-fn hw +) :y2 (diag-fn hh +) :stroke "rgb(0,0,0)" :stroke-width disc-mark-stroke}]
     [:line {:x1 (diag-fn hw +) :y1 (diag-fn hh -) :x2 (diag-fn hw -) :y2 (diag-fn hh +) :stroke "rgb(0,0,0)" :stroke-width disc-mark-stroke}]]))

(defn background [w h]
  (let [hw (/ w 2)
        hh (/ h 2)
        x-max 500
        y-max 300]
    (into [] (concat [[:line {:x1 hw :y1 h :x2 hw :y2 0 :stroke "rgb(0,0,0)" :stroke-width 1}]
                      [:line {:x1 0 :y1 hh :x2 w :y2 hh :stroke "rgb(0,0,0)" :stroke-width 1}]]
                     (disc-marks hw x-max hh y-max +)
                     (disc-marks hw x-max hh y-max -)
                     (disc-mark-cross hw hh)))))