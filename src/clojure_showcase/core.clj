(ns clojure-showcase.core
  (:require [clojure.string :as string]
            [clojure-showcase.sudoku :as sudoku]
            [clojure-showcase.happy-number :as happy]))


(defn- format-numbers
  "Return string containing bullet point list of supplied number sequence."
  [number-sequence]
  (string/join "\n - " number-sequence))


(defn -main
  "Main entry point."
  [& args]

  ;; list all happy numbers from 1 to 100
  (println
    "The following numbers up to 100 are happy:\n -"
    (format-numbers (filter happy/is-happy? (range 101)))
    "\n")

  ;; print the happy number sequence for 79
  (println
    "The happy number sequence for 79 is:\n -"
    (format-numbers (take-while (complement nil?) (happy/happy-sequence 79)))
    "\n")

  ;; print unsolved sudoku board
  (println
    "Unsolved sudoku board:\n"
    (sudoku/prettify sudoku/unsolved-board)
    "\n")

  (println
    "Solved sudoku board:\n"
    (sudoku/prettify (sudoku/solve sudoku/unsolved-board))))
