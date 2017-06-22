# Clojure Showcase
Welcome to my Clojure showcase! It contains two relatively small solutions:

## Happy Numbers
One solution I wrote is to be able to tell whether a number is happy or not. A happy number is defined as follows (source: Wikipedia):

>A happy number is a number defined by the following process: Starting with any positive integer, replace the number by the sum of the squares of its digits, and repeat the process until the number either equals 1 (where it will stay), or it loops endlessly in a cycle that does not include 1. Those numbers for which this process ends in 1 are happy numbers, while those that do not end in 1 are unhappy numbers (or sad numbers).

In the happy-number namespace I've created two public functions (and several private helper functions) to tell whether a number is happy (`is-happy?`) and to give a lazy sequence of the happy number sequence for a given number `happy-sequence`).

## Sudoku
For the "Functional programming in Clojure" course I took on-line (link: http://iloveponies.github.io/120-hour-epic-sax-marathon/index.html) the final assignment was to write a function that could solve an unsolved sudoku board.

The assignment can be found here: http://iloveponies.github.io/120-hour-epic-sax-marathon/sudoku.html

In the sudoku namespace I provide two public functions. The first is to actually solve an unsolved sudoku board (`solve`), and the second is to pretty print a sudoku board (`prettify`). There are also two sample boards in the sudoku namespace, an unsolved one (`unsolved-board`) and a solved one (`solved-board`).

