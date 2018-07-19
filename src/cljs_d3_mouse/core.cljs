(ns cljs-d3-mouse.core
  (:require [goog.object]
            [d3-selection :as d3]
            [d3-ease :refer [easeLinear easeElastic easeBackOut]
             :rename {easeLinear linear easeElastic elastic easeBackOut back-out}]
            [d3-transition]))

(defn get [prop] (let [prop (name prop)] #(goog.object/get % prop)))

(defn make-point
  ([x y svg]
   (doto (.createSVGPoint svg)
     (goog.object/set "x" x)
     (goog.object/set "y" y)))
  ([x y svg node]
   (.. (make-point x y svg)
     (matrixTransform (.. node getScreenCTM inverse)))))

(defn juxt-map [f coll] (apply juxt (map f coll)))

(let [[svg span rect circle] (map d3/select ["svg" "span" "rect" "circle"])
      ease (.. elastic (amplitude 1.1) (period 0.4))]
  (.. svg
     (on "mousemove"
         (fn []
           (let [svg (.node svg)
                 e d3/event
                 [x y] ((juxt-map get '(x y)) e)
                 [cx cy] (map #(.toFixed % 3) (d3/mouse svg))
                 [top r1 bottom left] ((juxt-map get '(top right bottom left))
                                          (.. span node getBoundingClientRect))
                 [top left bottom r2] (into (vec (map #(- % 5) [top left]))
                                               (map #(+ % 5) [bottom r1]))
                 [x1 y1] ((juxt-map get '(x y)) (make-point left top svg svg))
                 [x0  _] ((juxt-map get '(x y)) (make-point r1 top svg svg))
                 [x2 y2] ((juxt-map get '(x y)) (make-point r2 bottom svg svg))]
             (.. span
                 (text (str x  ", " y  "\n"
                            cx ", " cy)))
             (.. circle
                 (transition)
                 (duration 500)
                 (ease ease)
                 (attr 'cx cx)
                 (attr 'cy cy)
                 (attr 'r (- x2 x0)))
             (.. rect
                 (attr 'x x1)
                 (attr 'y y1)
                 (attr 'width (- x2 x1))
                 (attr 'height (- y2 y1))))))))
