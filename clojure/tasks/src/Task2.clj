(ns Task2)

; генерирует бесконечную ленивую последовательность натуральных чисел, начиная с n
(defn start-from [n]
  (iterate inc n))

; проверка на делимость
(defn isDivisible? [x y]
  (= (rem x y) 0))

; lazy-seq помечаем как ленивую
; (cons x seq):  (cons 1 (2 3 4 5 6)) => (1 2 3 4 5 6), вставка в начало
; first - первый элемент

; Решето Эратосфена
; Берем первое простое, вычеркиваем все числа, где оно делитель.
(defn sieve [stream]
  (cons (first stream)
        (lazy-seq (sieve (filter #(not (isDivisible? % (first stream))) (rest stream))))
        )
  )

(def primes (sieve (start-from 2)))

; (take 10 primes)


;; ---------- Tests ----------
(ns Task2 (:require [clojure.test :as test]))

(test/deftest primes-tests
  (test/is (= (take 1 primes) [2]))
  (test/is (= (take 5 primes) (list 2 3 5 7 11)))
  (test/is (= (take 10 primes) (list 2 3 5 7 11 13 17 19 23 29)))
  (test/is (= (take 25 primes) (list 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97)))
  )

(primes-tests)