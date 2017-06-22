(ns clojure-showcase.sudoku
  (:require [clojure.set :as set]
            [clojure.string :as string]))


(def board identity)


;;; unnecessarily fancy way of defining #{1 2 3 4 5 6 7 8 9} :p
(def all-values (set (range 1 10)))


(def unsolved-board
  (board [[5 3 0 0 7 0 0 0 0]
          [6 0 0 1 9 5 0 0 0]
          [0 9 8 0 0 0 0 6 0]
          [8 0 0 0 6 0 0 0 3]
          [4 0 0 8 0 3 0 0 1]
          [7 0 0 0 2 0 0 0 6]
          [0 6 0 0 0 0 2 8 0]
          [0 0 0 4 1 9 0 0 5]
          [0 0 0 0 8 0 0 7 9]]))


(def solved-board
  (board [[5 3 4 6 7 8 9 1 2]
          [6 7 2 1 9 5 3 4 8]
          [1 9 8 3 4 2 5 6 7]
          [8 5 9 7 6 1 4 2 3]
          [4 2 6 8 5 3 7 9 1]
          [7 1 3 9 2 4 8 5 6]
          [9 6 1 5 3 7 2 8 4]
          [2 8 7 4 1 9 6 3 5]
          [3 4 5 2 8 6 1 7 9]]))


(defn- value-at
  "Return value at the given coordinates on the board."
  [board coord]
  (get-in board coord))


(defn has-value? [board coord]
  (> (value-at board coord) 0))


(defn- row-values
  "Return set of values for the entire row of the given coordinate."
  [board [y x :as coord]]
  (set (get board y)))


(defn- col-values
  "Return set of values for the entire column of the given coordinate."
  [board [y x :as coord]]
  (set (map #(get %1 x) board)))


(defn- coord-pairs
  "Return all coordinate pairs for the given coordinate sequence."
  [coords]
  (for [row-num coords
        col-num coords]
    [row-num col-num]))


(defn- block-top-left
  "Return the top left coordinate of the block the given coordinate is located
  in."
  [coordinates]
  (for [coordinate coordinates]
    (cond
      (<= 0 coordinate 2) 0
      (<= 3 coordinate 5) 3
      (<= 6 coordinate 8) 6)))


(defn- block-coords
  "Return collection of coordinates for the block starting at the given top
  left coordinate."
  [[y x]]
  (let [coord-range #(range % (+ % 3))
        y-coords (coord-range y)
        x-coords (coord-range x)]
    (for [y y-coords
          x x-coords]
      [y x])))


(defn- block-values
  "Return set of the values currently in the block the given coordinate is
  located in."
  [board coord]
  (let [top-left (block-top-left coord)
        value (partial value-at board)]
    (set (map value (block-coords top-left)))))


(defn- valid-values-for
  "Return set of valid values for the given coordinate on the board."
  [board coord]
  (if (has-value? board coord)
    #{}
    (let [used-values (juxt row-values
                            col-values
                            block-values)]
      (apply set/difference
             all-values
             (used-values board coord)))))


(defn- filled?
  "Return whether the given board is completely filled or not."
  [board]
  (empty? (filter #(some zero? %) board)))


(defn- rows
  "Return collection of sets containing the numbers for each row in the given
  board."
  [board]
  (for [row board]
    (set row)))


(defn- cols
  "Return collection of sets containing the numbers for each column in the
  given board."
  [board]
  (loop [x 0
         row (first board)
         result `()]
    (if (empty? row)
      result
      (recur (inc x)
             (rest row)
             (conj result (col-values board [0 x]))))))


(defn- blocks
  "Return collection of sets containing the numbers for each block in the
  given board."
  [board]
  (let [row-length (count (first board))
        col-length (count board)
        block-coords #(range 0 % 3)]
    (for [y (block-coords col-length)
          x (block-coords row-length)]
      (block-values board [y x]))))


(defn- valid-sets?
  "Return whether each of the given sets is a valid set."
  [sets]
  (every? #(= all-values %) sets))


(defn- valid-cols?
  "Return whether each of the board's columns contain a valid number set."
  [board]
  (valid-sets? (cols board)))


(defn- valid-rows?
  [board]
  "Return whether each of the board's rows contain a valid number set."
  (valid-sets? (rows board)))


(defn- valid-blocks?
  "Return whether each of the nine blocks add up to a valid number set."
  [board]
  (valid-sets? (blocks board)))


(defn- valid-solution?
  "Return whether the board is correctly filled."
  [board]
  ;; if board is nil it's obviously invalid.
  (if (nil? board)
    false
    ;; juxtapose our three check functions with identical signature.
    (let [check (juxt valid-cols?
                      valid-rows?
                      valid-blocks?)]
      ;; board is valid if we have a triple true :)
      (every? true? (check board)))))


(defn- set-value-at
  "Set the value at the given coordinate on the board."
  [board coord new-value]
  (assoc-in board coord new-value))


(defn- find-empty-point
  "Moving left to right, top to bottom, find the first empty point on the
  sudoku board."
  [board]
  (let [is-empty? (complement (partial has-value? board))
        coordinate-pairs (coord-pairs (range (count board)))]
    (first (filter is-empty? coordinate-pairs))))


(defn- solution-helper
  "Recursively fill the given board using backtracking search.

  Return either vector containing the filled & correct board, or an empty
  vector if no correct solution was found."
  [board]
  (cond
    ;; if the board is filled and correct, return vector containing board.
    (and (filled? board)
         (valid-solution? board))
    [board]

    ;; if the board is filled, but not correct, return empty vector.
    (filled? board)
    []

    ;; if the board is not full yet, find the next empty point
    :else
    (let [empty-point (find-empty-point board)]
      (for [value (valid-values-for board empty-point)
            solution (-> board
                         (set-value-at empty-point value)
                         (solution-helper))]
        solution))))


(defn solve
  "Return solved board if board is solvable, else nil."
  [board]
  (first (solution-helper board)))


(defn prettify
  "Return prettified string representation of sudoku board."
  [board]
  (string/join "\n " (map #(string/join " " %) board)))