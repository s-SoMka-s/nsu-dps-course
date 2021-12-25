(ns Task3)

; ceil(x) - первое целое число, больше чем x;
; (partition-all 4 [0 1 2 3 4 5 6 7 8 9]) => ((0 1 2 3) (4 5 6 7) (8 9)) - пытаестя разделить на части по n элементов
; doall работа в обход ленивости, здесь сразу применяется map
; map deref - ожидает результат future
; flatten - склейка списка списков (flatten [1 [2 3]]) => (1 2 3)

; фильтр на потоках
(defn parallel-filter
  ([pred coll]
   (let [chunk-size (int (Math/ceil (Math/sqrt (count coll)))), ; вычисляем размер блока для филтрации
         parts (partition-all chunk-size coll)]             ; разбиваем инпут на блоки
     (->> parts
          (map (fn [coll1] (future (doall (filter pred coll1))))) ; фильтруем каждый блок на месте в отдельном потоке
          (doall)
          (map deref)
          (flatten)))))


(defn parallel-filter-handler ([pred coll]
                               (if (empty? coll) '()        ; пусто -> вернем пустоту
                                                 (concat (parallel-filter pred (take 500 coll)) ; берем по 500 элементов, надо тк последовательность ленивая
                                                         (lazy-seq (parallel-filter-handler pred (drop 500 coll))))))) ; выкидываем тот набор, который отфильтровали


;; ---------- Tests ----------
(ns Task3
  (:require [clojure.test :as test]))

(test/deftest filter-tests
  (test/is (= (reduce + (parallel-filter-handler even? (range 0 50))) (reduce + (filter even? (range 0 50)))))
  (test/is (= (reduce + (parallel-filter-handler odd? (range 0 100))) (reduce + (filter odd? (range 0 100)))))
  (test/is (= (reduce + (parallel-filter-handler even? (range 50 160))) (reduce + (filter even? (range 50 160)))))
  (test/is (= (reduce * (parallel-filter-handler even? (range 0 6))) (reduce * (filter even? (range 0 6)))))
  (test/is (= (reduce + (take 1000 (parallel-filter-handler even? (range)))) (reduce + (take 1000 (filter even? (range))))))
  (test/is (= (reduce + (take 1100 (parallel-filter-handler even? (range)))) (reduce + (take 1100 (filter even? (range)))))))

(filter-tests)


;; ---------- Efficiency ----------
(time (reduce + (parallel-filter-handler odd? (range 100))))
(time (reduce + (filter odd? (range 100))))

(time (reduce + (take 1250 (parallel-filter-handler odd? (range)))))
(time (reduce + (take 1250 (filter odd? (range)))))