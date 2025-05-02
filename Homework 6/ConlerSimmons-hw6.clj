;; Name: Conler Simmons
;; CSC 533 - HW6: CLojure Structures
;;; periodic table of elements
(def PERIODIC-TABLE
  {:H  [1   1.0080 :Hydrogen]      :He [2   4.0026 :Helium]
   :Li [3   6.9410 :Lithium]       :Be [4   9.0122 :Beryllium]
   :B  [5  10.8100 :Boron]         :C  [6  12.0110 :Carbon]
   :N  [7  14.0067 :Nitrogen]      :O  [8  15.9994 :Oxygen]
   :F  [9  18.9984 :Fluorine]      :Ne [10  20.1790 :Neon]
   :Na [11  22.9898 :Sodium]        :Mg [12  24.3050 :Magnesium]
   :Al [13  26.9815 :Aluminum]      :Si [14  28.0860 :Silicon]
   :P  [15  30.9738 :Phosphorus]    :S  [16  32.0600 :Sulfur]
   :Cl [17  35.4530 :Chlorine]      :Ar [18  39.9480 :Argon]
   :K  [19  39.1020 :Potassium]     :Ca [20  40.0800 :Calcium]
   :Sc [21  44.9559 :Scandium]      :Ti [22  47.9000 :Titanium]
   :V  [23  50.9414 :Vanadium]      :Cr [24  51.9960 :Chromium]
   :Mn [25  54.9380 :Manganese]     :Fe [26  55.8470 :Iron]
   :Co [27  58.9332 :Cobalt]        :Ni [28  58.7100 :Nickel]
   :Cu [29  63.5460 :Copper]        :Zn [30  65.3700 :Zinc]
   :Ga [31  69.7200 :Gallium]       :Ge [32  72.5900 :Germanium]
   :As [33  74.9216 :Arsenic]       :Se [34  78.9600 :Selenium]
   :Br [35  79.9040 :Bromine]       :Kr [36  83.8000 :Krypton]
   :Rb [37  85.4678 :Rubidium]      :Sr [38  87.6200 :Strontium]
   :Y  [39  88.9059 :Yttrium]       :Zr [40  91.2200 :Zirconium]
   :Nb [41  92.9064 :Niobium]       :Mo [42  95.9400 :Molybdenum]
   :Tc [43  98.9062 :Technetium]    :Ru [44 101.0700 :Ruthenium]
   :Rh [45 102.9055 :Rhodium]       :Pd [46 106.4000 :Palladium]
   :Ag [47 107.8680 :Silver]        :Cd [48 112.4000 :Cadmium]
   :In [49 114.8200 :Indium]        :Sn [50 118.6900 :Tin]
   :Sb [51 121.7500 :Antimony]      :Te [52 127.6000 :Tellurium]
   :I  [53 126.9045 :Iodine]        :Xe [54 131.3000 :Xenon]
   :Cs [55 132.9055 :Caesium]       :Ba [56 151.9600 :Barium]
   :La [57 138.9055 :Lanthanum]     :Ce [58 140.1160 :Cerium]
   :Pr [59 140.9077 :Praseodymium]  :Nd [60 144.2420 :Neodymium]
   :Pm [61 145.0000 :Promethium]    :Sm [62 150.3600 :Samarium]
   :Eu [63 151.9640 :Europium]      :Gd [64 157.2500 :Gadolinium]
   :Tb [65 158.9254 :Terbium]       :Dy [66 162.5000 :Dysprosium]
   :Ho [67 164.9303 :Holmium]       :Er [68 167.2590 :Erbium]
   :Tm [69 168.9342 :Thulium]       :Yb [70 173.0450 :Ytterbium]
   :Lu [71 174.9669 :Lutetium]      :Hf [72 178.4900 :Hafnium]
   :Ta [73 180.9479 :Tantalum]      :W  [74 183.8500 :Tungsten]
   :Re [75 186.2000 :Rhenium]       :Os [76 190.2000 :Osmium]
   :Ir [77 192.2200 :Iridium]       :Pt [78 195.0900 :Platinum]
   :Au [79 196.9665 :Gold]          :Hg [80 200.5900 :Mercury]
   :Tl [81 204.3700 :Thallium]      :Pb [82 207.2000 :Lead]
   :Bi [83 208.9806 :Bismuth]       :Po [84 210.0000 :Polonium]
   :At [85 210.0000 :Astatine]      :Rn [86 222.0000 :Radon]
   :Fr [87 223.0000 :Francium]      :Ra [88 226.0254 :Radium]
   :Ac [89 227.0000 :Actinium]      :Th [90 232.0381 :Thorium]
   :Pa [91 231.0359 :Protactinium]  :U  [92 238.0289 :Uranium]
   :Np [93 237.0482 :Neptunium]     :Pu [94 242.0000 :Plutonium]
   :Am [95 243.0000 :Americium]     :Cm [96 247.0000 :Curium]
   :Bk [97 249.0000 :Berkelium]     :Cf [98 251.0000 :Californium]
   :Es [99 252.0000 :Einsteinium]   :Fm [100 257.0000 :Fermium]
   :Md [101 258.0000 :Mendelevium]   :No [102 259.0000 :Nobelium]
   :Lr [103 266.0000 :Lawrencium]    :Rf [104 267.0000 :Rutherfordium]
   :Db [105 268.0000 :Dubnium]       :Sg [106 269.0000 :Seaborgium]
   :Bh [107 270.0000 :Bohrium]       :Hs [108 270.0000 :Hassium]
   :Mt [109 278.0000 :Meitnerium]    :Ds [110 281.0000 :Darmstadtium]
   :Rg [111 282.0000 :Roentgenium]   :Cn [112 285.0000 :Copernicium]
   :Nh [113 286.0000 :Nihonium]      :Fl [114 289.0000 :Flerovium]
   :Mc [115 290.0000 :Moscovium]     :Lv [116 293.0000 :Livermorium]
   :Ts [117 294.0000 :Tennessine]    :Og [118 294.0000 :Oganesson]})

;;; binary tree functions
(defn root [btree]
  (nth btree 0))

(defn left-subtree [btree]
  (nth btree 1))

(defn right-subtree [btree]
  (nth btree 2))

(defn height [tree]
  (if (empty? tree)
    0
    (inc (max (height (left-subtree tree)) (height (right-subtree tree))))))

;;; Die "class" using protocol & interface
(defprotocol DieInterface
  (roll [this])
  (get-sides [this])
  (get-rolls [this]))

(deftype Die [num-sides ^:unsynchronized-mutable num-rolls]
  DieInterface
  (roll [this]
    (do (set! num-rolls (inc num-rolls)) (inc (rand-int num-sides))))
  (get-sides [this] num-sides)
  (get-rolls [this] num-rolls))

(defn make-die
  ([] (Die. 6 0))
  ([sides] (Die. sides 0)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;      ADD YOUR CODE BELOW HERE
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; The periodic table, which identifies each element and its atomic weight, is listed below as a Clojure map:

;;     (def PERIODIC-TABLE
;;        {:H  [  1   1.0080 :Hydrogen]    :He [  2   4.0026 :Helium]
;;         :Li [  3   6.9410 :Lithium]     :Be [  4   9.0122 :Beryllium]
;;         :B  [  5  10.8100 :Boron]       :C  [  6  12.0110 :Carbon]
;;         :N  [  7  14.0067 :Nitrogen]    :O  [  8  15.9994 :Oxygen]
;;         .
;;         .
;;         .
;;         :Ts [117 294.0000 :Tennessine]  :Og [118 294.0000 :Oganesson]})

;; Define a function named atomic-number that takes one input, the keyword for an element, and returns the atomic number of that element (i.e., its position in the table). For example, (atomic-number :H) should evaluate to 1 while (atomic-number :O) should evaluate to 8.

(defn atomic-number [element]
  (first (get PERIODIC-TABLE element))) ;;directly accesses the first elelment of the periodic table which is the atomic number

(atomic-number :H)
(atomic-number :O)

;; Define a function named atomic-weight that takes one input, the keyword for an element, and returns the atomic weight of that element. For example, (atomic-weight :H) should evaluate to 1.008 while (atomic-weight :O) should evaluate to 15.9994.

(defn atomic-weight [element]
  (second (get PERIODIC-TABLE element))) ;;directly accesses the second element of the periodic table which is the atomic weight

(atomic-weight :H)
(atomic-weight :O)

;; Define a function named molecular-weight1 that takes one input, a list of element keywords, and returns the molecular weight (i.e., the sum of the atomic weights) for that list. For example, (molecular-weight1 '(:H :H :O)) should evaluate to 18.0154.

(defn molecular-weight1 [elements]
  (reduce + (map atomic-weight elements))) ;;uses reduce to sum the atomic weights of the elements in the list holy clojure has everything

(molecular-weight1 '(:H :H :O))

;; When writing formulas for molecules in which an element appears multiple times, scientists use subscripts to denote repetition. For example, the formula for water is written H2O instead of HHO. Copy your molecular-weight1, rename it molecular-weight2, and modify it to handle lists in which an element may be followed by a number. For example, (molecular-weight2 '(:H 2 :O)) should evaluate to 18.0154.

(defn molecular-weight2 [elements]

  (loop [els elements ;;here, elements is a binding of mutable paris which is great because that is exactly what periodic elemnts are
         sum 0.0] ;;start with the sum of 0
    (if (empty? els) ;;if the list is empty, return the sum
      sum
      (let [element (first els) ;;bind the first element to the variable element
            rest-els (rest els)] ;;bind the rest of the elements to the variable rest-els
        (if (keyword? element) ;;check if the element is a keyword
          (if (and (not (empty? rest-els)) (number? (first rest-els))) ;;check if the rest of the elements are not empty and if the first element is a number
            ;; if the element is followed by a number
            (recur (rest rest-els) (+ sum (* (atomic-weight element) (first rest-els))))
            ;;if the lement is just a keyword, add its weight to the sum
            (recur rest-els (+ sum (atomic-weight element))))
          ;;not an element, just continue
          (recur rest-els sum))))))

(molecular-weight2 '(:H 2 :O))

;; Complex formulas can contain nested sub-formulas. For example, the formula for isopropyl alcohol is (CH3)2CHOH. Copy your molecular-weight2, rename it molecular-weight3, and modify it to handle nested lists. For example, (molecular-weight3 '((:C :H 3) 2 :C :H :O :H)) should evaluate to 60.0964.

(defn molecular-weight3 [elements]
  (loop [els elements ;;samem stuff as before
         sum 0.0]
    (if (empty? els)
      sum
      (let [element (first els)
            rest-els (rest els)] ;;through to here
        (cond
          (list? element) ;;check if the element is a list
          (if (and (not (empty? rest-els)) (number? (first rest-els))) ;;check if the rest of the elements are not empty and if the first element is a number
            (recur (rest rest-els) ;;if the element is followed by a number
                   (+ sum (* (molecular-weight3 element) (first rest-els)))) ;;add the weight of the element multiplied by the number to the sum
            (recur rest-els (+ sum (molecular-weight3 element)))) ;;if the element is just a list, add its weight to the sum
          (keyword? element) ;;check if the element is a keyword
          (if (and (not (empty? rest-els)) (number? (first rest-els))) ;; check if the rest of the elements are not empty and if the first element is a number
            (recur (rest rest-els) ;;recur with the rest of the elements
                   (+ sum (* (atomic-weight element) (first rest-els)))) ;;get the atomic weight of the element and then 
            (recur rest-els (+ sum (atomic-weight element)))) ;;recur through the rest of the elements, adding the sum
          :else
          (recur rest-els sum)))))) ;;otherwise just recur through the rest of the elements

(molecular-weight3 '((:C :H 3) 2 :C :H :O :H))

;; In class, we discussed how structured lists could be used to represent binary trees. For example, the following lists represent a tree of animal names (strings) and a tree of numbers, respectively.

(def ANIMALS
  '(:dog
    (:bird (:horse () ()) (:cat () ()))
    (:possum (:dog () ()) ())))

(def NUMBERS
  '(2 (-1 () ()) (3 () ())))

;; Define a function named rightmost that takes one input, a list representing a binary tree, and returns the rightmost value in the tree. For example, (rightmost ANIMALS) should evaluate to possum. Note: since an empty tree does not have a rightmost element, the function should return nil when applied to an empty tree.

(defn rightmost [btree]
  (if (empty? btree)
    nil ;;todo: ask if this is the right way to do this
    (if (empty? (right-subtree btree)) ;;if the right subtree of the da tree is empty
      (root btree) ;;if it is, return the root
      (rightmost (right-subtree btree))))) ;;otherwise, recur through the right subtree

;; Define a function named leftmost that takes one input, a list representing a binary tree, and returns the leftmost value in the tree. For example, (leftmost ANIMALS) should evaluate to horse. Note: since an empty tree does not have a leftmost element, the function should return nil when applied to an empty tree.

(defn leftmost [btree]
  (if (empty? btree)
    nil
    ;; same as above but for the left subtree
    (if (empty? (left-subtree btree))
      (root btree)
      (leftmost (left-subtree btree)))))

;; Define a function named is-bst? that takes one input, a list representing a binary tree, and returns true if that list is a binary search tree (otherwise false). For example, (is-bst? NUMBERS) should evaluate to true, while (is-bst? ANIMALS) should evaluate to false.

(defn is-bst? [btree]
  (if (empty? btree)
    true ;;technically a an empty tree is a binary search tree
    (let [root-value (root btree) ;;get the root value of the tree
          left-value (left-subtree btree) ;;get the left subtree
          right-value (right-subtree btree)] ;;get the right subtree
      (and (or (empty? left-value) (< (root left-value) root-value)) ;;check if the left subtree is empty or if the root of the left subtree is less than the root of the tree
           (or (empty? right-value) (> (root right-value) root-value)) ;;check if the right subtree is empty or if the root of the right subtree is greater than the root of the tree
           (is-bst? left-value) ;;recur check if the left subtree is a binary search tree
           (is-bst? right-value))))) ;;recur check if the right subtree is a binary search tree

;; Define a function named is-balanced? that takes one input, a list representing a binary tree, and returns true if that list is relatively balanced (otherwise false) . For example, (is-balanced? ANIMALS) and (is-balanced? NUMBERS) should both evaluate to false.

(defn is-balanced? [btree]
  (if (empty? btree)
    true ;;againm an empty tree is balanced
    (let [left-height (height (left-subtree btree)) ;;get the height of the left subtree
          right-height (height (right-subtree btree))] ;; get the height of the right subtree
      (and (<= (Math/abs (- left-height right-height)) 1) ;;check if the absolute difference between the left and right heights is less than or equal to 1
           (is-balanced? (left-subtree btree)) ;;recur check if the left subtree is balanced
           (is-balanced? (right-subtree btree)))))) ;;recur check if the right subtree is balanced

;; Define a function named is-avl? that takes one input, a list representing a binary tree, and returns true if that list is an AVL tree (otherwise false) . For example, (is-avl? NUMBERS) should evaluate to true, while (is-avl? ANIMALS) should evaluate to false. 

(defn is-avl? [btree]
  (if (empty? btree)
    true ;; an empty tree is an AVL tree
    (let [left-height (height (left-subtree btree)) ;; get the height of the left subtree
          right-height (height (right-subtree btree))] ;; get the height of the right subtrees
      (and (<= (Math/abs (- left-height right-height)) 1) ;; check if the absolute difference betwen the left and ride heights is less than or rqual to one
           (is-bst? btree) ;; check if the tree is a binary search tree
           (is-avl? (left-subtree btree)) ;; recur check if the left subtree is an AVL tree
           (is-avl? (right-subtree btree)))))) ;; recur check if the right subtree is an AVL tree

;;Define a function named roll-until-doubles that utilizes this "class" to simulate repeated dice rolls until doubles are obtained. The function should have one input, the number of die sides, and should return the number of rolls required to achieve doubles. In addition, each pair of dice rolls should be displayed. For example, (roll-until-doubles 6) might produce the following:

(defn roll-until-doubles [sides]
  (let [die1 (make-die sides) ;;create a die with the number of sides
        die2 (make-die sides)] ;;create a second die with the same number of sides
    (loop [rolls 0] ;;start with 0 rolls
      (let [roll1 (roll die1) ;;roll the first die
            roll2 (roll die2)] ;;roll the second die
        (println "Roll:" roll1 roll2) ;;print the rolls NOT FUNCTIONAL
        (if (= roll1 roll2) ;;check if the rolls are equal
          rolls ;;if they are, return the number of rolls
          (recur (inc rolls))))))) ;;otherwise, recur with the number of rolls incremented by one

(roll-until-doubles 6) 