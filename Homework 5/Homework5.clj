;; =============================================
;; Author: Conler Simmons
;; File: Homework5.clj
;; Version: 1.0
;; Created: April 15, 2025
;; Last Modified: April 15, 2025
;; Description: Clojure functions for modeling 
;; light travel time, relativistic physics, list 
;; operations, dice roll simulations, and a craps 
;; game. All functions follow functional and 
;; recursive programming principles.
;; =============================================

;; ----------------------------
;; LIGHT TRAVEL CALCULATIONS
;; ----------------------------

;; Returns how many seconds light takes to travel the given distance in miles.
(defn light-delay-sec [miles]
  (if (neg? miles)
    0
    (int (Math/round (/ miles 186262.0))))) ;; Speed of light in miles/sec

;; Converts a number of seconds into decimal minutes.
(defn convert-sec-to-min [secs]
  (double (/ secs 60)))

;; Computes the light travel time in minutes based on a given distance.
(defn light-delay-min [miles]
  (convert-sec-to-min (light-delay-sec miles)))

;; ----------------------------
;; RELATIVITY AND TIME DILATION
;; ----------------------------

;; Returns time (in years) for a trip observed externally, based on distance and fraction of light speed.
(defn time-to-observer [light-years speed-ratio]
  (/ light-years speed-ratio))

;; Calculates the relativistic time dilation factor based on velocity as a fraction of light speed.
(defn calc-dilation [speed-ratio]
  (Math/sqrt (- 1 (Math/pow speed-ratio 2))))

;; Returns the perceived travel time to the traveler, adjusted for time dilation.
(defn time-to-traveler [light-years speed-ratio]
  (* (time-to-observer light-years speed-ratio)
     (calc-dilation speed-ratio)))

;; ----------------------------
;; LIST UTILITIES
;; ----------------------------

;; Recursively removes an element at a specified index; returns unchanged list if index is out of bounds.
(defn drop-at-index [lst idx]
  (cond
    (neg? idx) lst
    (>= idx (count lst)) lst
    (zero? idx) (rest lst)
    :else (cons (first lst) (drop-at-index (rest lst) (dec idx)))))

;; Removes an element at a specified index using built-in list slicing.
(defn cut-at-index [lst idx]
  (concat (take idx lst) (nthrest lst (inc idx))))

;; Inserts a value into the list at the given index using recursion.
(defn insert-value-A [lst idx val]
  (cond
    (neg? idx) lst
    (> idx (count lst)) lst
    (zero? idx) (cons val lst)
    :else (cons (first lst) (insert-value-A (rest lst) (dec idx) val))))

;; Inserts a value using slicing; returns original list if index is out of range.
(defn insert-value-B [lst idx val]
  (if (and (>= idx 0) (<= idx (count lst)))
    (concat (take idx lst) (list val) (nthrest lst idx))
    lst))

;; Replaces the value at a specified index; unchanged list if index invalid.
(defn replace-at-index [lst idx val]
  (if (and (>= idx 0) (< idx (count lst)))
    (concat (take idx lst) (list val) (nthrest lst (inc idx)))
    lst))

;; Increments the value at the specified index by one.
(defn increment-at-index [lst idx]
  (if (and (>= idx 0) (< idx (count lst)))
    (replace-at-index lst idx (inc (nth lst idx)))
    lst))

;; ----------------------------
;; DICE ROLL SIMULATION
;; ----------------------------

;; Simulates the roll of two dice with a given number of sides and returns their sum.
(defn roll-two-dice [sides]
  (if (<= sides 0)
    0
    (+ (inc (rand-int sides)) (inc (rand-int sides)))))

;; Repeatedly rolls two dice and counts how many times the desired total occurs.
(defn match-rolls [trials sides target]
  (letfn [(count-matches [left hits]
            (if (zero? left)
              hits
              (recur (dec left)
                     (if (= (roll-two-dice sides) target)
                       (inc hits)
                       hits))))]
    (count-matches trials 0)))

;; Tallies the frequency of all possible totals from rolling two dice over many trials.
(defn tally-all-rolls [trials sides]
  (letfn [(tally-loop [left results]
            (if (zero? left)
              results
              (let [result (roll-two-dice sides)
                    idx (- result 2)]
                (recur (dec left) (increment-at-index results idx)))))]
    (tally-loop trials (repeat (- (* 2 sides) 1) 0))))

;; ----------------------------
;; CRAPS GAME SIMULATION
;; ----------------------------

;; Simulates one game of craps and returns :WINNER or :LOSER based on game rules.
(defn play-craps []
  (letfn [(continue-roll [point]
            (let [next-roll (roll-two-dice 6)]
              (cond
                (= next-roll 7) :LOSER
                (= next-roll point) :WINNER
                :else (recur point))))]
    (let [initial-roll (roll-two-dice 6)]
      (cond
        (or (= initial-roll 2) (= initial-roll 12)) :LOSER
        (= initial-roll 7) :WINNER
        :else (continue-roll initial-roll)))))

;; Simulates multiple games of craps and returns the win ratio as a decimal value.
(defn craps-success-rate [games]
  (letfn [(simulate [remaining wins]
            (if (zero? remaining)
              (double (/ wins games))
              (recur (dec remaining)
                     (if (= (play-craps) :WINNER)
                       (inc wins)
                       wins))))]
    (simulate games 0)))
   
   