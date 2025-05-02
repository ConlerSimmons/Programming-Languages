;; Author: Conler Simmons
;; CSC 533 - Homework 6: Exploring Clojure Data Structures

;;; Comprehensive periodic table of elements
(def PERIODIC-TABLE
  {:H  [1   1.0080 :Hydrogen]      :He [2   4.0026 :Helium]
   :Li [3   6.9410 :Lithium]       :Be [4   9.0122 :Beryllium]
   :B  [5  10.8100 :Boron]         :C  [6  12.0110 :Carbon]
   :N  [7  14.0067 :Nitrogen]      :O  [8  15.9994 :Oxygen]
   ;; truncated for brevity — keep the rest of PERIODIC-TABLE unchanged
   :Ts [117 294.0000 :Tennessine]  :Og [118 294.0000 :Oganesson]})

;;; Functions to navigate binary trees represented as vectors
(defn root [btree] (nth btree 0))

(defn left-subtree [btree] (nth btree 1))

(defn right-subtree [btree] (nth btree 2))

(defn height [tree]
  (if (empty? tree)
    0
    (inc (max (height (left-subtree tree)) (height (right-subtree tree))))))

;;; Implementation of a Die type using protocols and mutable state
(defprotocol DieInterface
  (roll [this])
  (get-sides [this])
  (get-rolls [this]))

(deftype Die [num-sides ^:unsynchronized-mutable num-rolls]
  DieInterface
  (roll [this] (do (set! num-rolls (inc num-rolls)) (inc (rand-int num-sides))))
  (get-sides [this] num-sides)
  (get-rolls [this] num-rolls))

(defn make-die
  ([] (Die. 6 0))
  ([sides] (Die. sides 0)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;               BEGIN YOUR ADDITIONS BELOW THIS LINE
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;; Section One: Atomic & Molecular Calculations

;; Retrieve the atomic number associated with a given element keyword
(defn atomic-number [element]
  (first (get PERIODIC-TABLE element)) 
)

;; Obtain the atomic mass of the specified element
(defn atomic-weight [element]
  (second (get PERIODIC-TABLE element))
)

;; Calculate molecular weight by summing atomic weights of each element in the list
(defn molecular-weight1 [elements]
  (reduce + (map atomic-weight elements))
)

;; Compute molecular weight with support for element counts following each symbol
(defn molecular-weight2 [elements]
  (loop [els elements sum 0.0]
    (if (empty? els)
      sum
      (let [element (first els)
            rest-els (rest els)]
        (if (keyword? element)
          (if (and (not (empty? rest-els)) (number? (first rest-els)))
            (recur (rest rest-els) (+ sum (* (atomic-weight element) (first rest-els))))
            (recur rest-els (+ sum (atomic-weight element))))
          (recur rest-els sum))))))

;; Extend molecular weight calculation to handle nested molecular groups with counts
(defn molecular-weight3 [elements]
  (loop [els elements sum 0.0]
    (if (empty? els)
      sum
      (let [element (first els)
            rest-els (rest els)]
        (cond
          (list? element)
          (if (and (not (empty? rest-els)) (number? (first rest-els)))
            (recur (rest rest-els) (+ sum (* (molecular-weight3 element) (first rest-els))))
            (recur rest-els (+ sum (molecular-weight3 element))))
          (keyword? element)
          (if (and (not (empty? rest-els)) (number? (first rest-els)))
            (recur (rest rest-els) (+ sum (* (atomic-weight element) (first rest-els))))
            (recur rest-els (+ sum (atomic-weight element))))
          :else
          (recur rest-els sum))))))

;;; Section Two: Binary Tree Utilities and BST Verification

;; Example tree of animals for testing
(def ANIMALS
  '(:dog
    (:bird (:horse () ()) (:cat () ()))
    (:possum (:dog () ()) ())))

;; Example numeric binary tree for testing BST properties
(def NUMBERS
  '(2 (-1 () ()) (3 () ())))

;; Find the rightmost value in a binary tree by traversing right subtrees
(defn rightmost [btree]
  (if (empty? btree)
    nil
    (if (empty? (right-subtree btree))
      (root btree)
      (rightmost (right-subtree btree)))))

;; Find the leftmost value in a binary tree by traversing left subtrees
(defn leftmost [btree]
  (if (empty? btree)
    nil
    (if (empty? (left-subtree btree))
      (root btree)
      (leftmost (left-subtree btree)))))

;; Determine if a binary tree satisfies the binary search tree ordering property
(defn is-bst? [btree]
  (if (empty? btree)
    true
    (let [root-value (root btree)
          left-value (left-subtree btree)
          right-value (right-subtree btree)]
      (and (or (empty? left-value) (< (root left-value) root-value))
           (or (empty? right-value) (> (root right-value) root-value))
           (is-bst? left-value)
           (is-bst? right-value)))))

;; Verify if a binary tree is height-balanced, meaning subtree heights differ by no more than one
(defn is-balanced? [btree]
  (if (empty? btree)
    true
    (let [left-height (height (left-subtree btree))
          right-height (height (right-subtree btree))]
      (and (<= (Math/abs (- left-height right-height)) 1)
           (is-balanced? (left-subtree btree))
           (is-balanced? (right-subtree btree))))))

;; Confirm that a binary tree is an AVL tree: balanced and satisfies BST constraints
(defn is-avl? [btree]
  (if (empty? btree)
    true
    (let [left-height (height (left-subtree btree))
          right-height (height (right-subtree btree))]
      (and (<= (Math/abs (- left-height right-height)) 1)
           (is-bst? btree)
           (is-avl? (left-subtree btree))
           (is-avl? (right-subtree btree))))))

;;; Section Three: Dice Simulation and Rolling Mechanics

;; Simulate rolling two dice repeatedly until both dice show the same number (doubles)
(defn roll-until-doubles [sides]
  (let [die1 (make-die sides)
        die2 (make-die sides)]
    (loop [rolls 1]
      (let [roll1 (roll die1)
            roll2 (roll die2)]
        (println "Roll:" roll1 roll2)
        (if (= roll1 roll2)
          rolls
          (recur (inc rolls)))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;               DEMONSTRATION OUTPUT FOR FULL-RUN VALIDATION
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(println "Atomic number of H:" (atomic-number :H))
(println "Atomic weight of O:" (atomic-weight :O))
(println "Molecular weight 1 HHO:" (molecular-weight1 '(:H :H :O)))
(println "Molecular weight 2 H2O:" (molecular-weight2 '(:H 2 :O)))
(println "Molecular weight 3 Isopropyl Alcohol:" (molecular-weight3 '((:C :H 3) 2 :C :H :O :H)))

(println "Rightmost of ANIMALS:" (rightmost ANIMALS))
(println "Leftmost of ANIMALS:" (leftmost ANIMALS))
(println "Is NUMBERS a BST?" (is-bst? NUMBERS))
(println "Is ANIMALS balanced?" (is-balanced? ANIMALS))
(println "Is NUMBERS an AVL?" (is-avl? NUMBERS))

(println "Rolls until doubles on 6-sided dice:" (roll-until-doubles 6))