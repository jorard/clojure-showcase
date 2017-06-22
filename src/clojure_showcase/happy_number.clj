(ns clojure-showcase.happy-number)


(defn- square
  "Return the square of x."
  [x]
  (* x x))


(defn- int->numbers
  "Return a sequence of the numbers making up an integer."
  [n]
  (map #(Integer/parseInt (str %)) (str n)))


(defn- happify
  "Return the sum of the squares of each digit comprising the number n."
  [number]
  ;; only happify if input is not 1
  (when (and (not= number 1)
             (not (nil? number)))
    (let [number-sequence (int->numbers number)
          numbers-squared (map square number-sequence)
          numbers-summed (reduce + numbers-squared)]
      numbers-summed)))


(defn happy-sequence
  "Return happy number sequence of given number."
  [number]
  (take-while #(not (nil? %)) (iterate happify number)))


(defn is-happy?
  "Return whether or not a number is happy.

  A number is happy when, if you iteratively sum the squares of each
  digit comprising the number, you end with 1.

  A number is not happy when such an iteration repeats ad infinitum."
  [number]
  (loop [current-number number
         seen #{}]
    (cond
      (== current-number 1) true
      (contains? seen current-number) false
      :else (recur (happify current-number)
                   (conj seen current-number)))))
