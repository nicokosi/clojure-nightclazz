(ns nightclazz.game-of-life
  (:require [quil.core :as q]))

;; generation:
(def world (atom
             #{[0 1] [1 2] [1 1] [1 3]}))

(defn setup []
  (q/smooth)
  (q/background 30))

(defn draw []
  (q/background (q/color 0 0 255))
  (q/stroke-weight 0)
  (q/fill (q/color 255 255 0))
  (doseq [[x y] (deref world)]
    (let [diam 20
          x (+ 100 (* x 20))
          y (+ 100 (* y 20))]
      (q/ellipse x y diam diam))))

#_(q/defsketch example ;;Define a new sketch named example
             :title "game of life" ;;Set the title of the sketch
             :setup setup ;;Specify the setup fn
             :draw draw ;;Specify the draw fn
             :size [323 200]) ;;You struggle to beat the golden ratio

#_(swap! world step)

(defn neighbours
  "calcule les 8 voisines" ; doc
  [[x y]]
  {:post [(= (count %) 8)]} ; assertion sur le résultat
  (for [i (range (dec x) (+ x 2)) ;; for => produit cartésien
        j (range (dec y) (+ y 2))
        :when (not (and (= x i) (= y j)))] ; filtre la cellule courante
    [i j])
  )

(defn step [cells] ; fonction pure qu'on peut appeller en "explorant" dans le REPL
  ; ou pour connaitre la 1000 ème génération
  (let [freqs (frequencies
                (mapcat neighbours @world) ; flatmap
                )]
    (set (keys (filter (fn [[cell n]]
                         (if (contains? cells cell)
                                        (<= 2 n 3))
                                        ;(= n 3)))
                         ) freqs)))))

#_
  (while true?(swap! world step)
  (Thread/sleep 100))
