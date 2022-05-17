(ns Task5)

(def transactions-count (atom 0))

; nth - вернет значение по индексу
(defn philosopher [cycles dining-time thinking-time n forks]
  (let [f1 (nth forks n), f2 (nth forks (mod (inc n) (count forks)))] ; присваиваем левую и правую вилку (учитывая круглость стола)
    (dotimes [_ cycles]
      (do
        (Thread/sleep thinking-time)
        (dosync
          (swap! transactions-count inc) ; увеличиваем количество транзацкций
          (alter f1 inc)
          (locking *out*
            (println n "has the fork" n))
          (alter f2 inc)
          (locking *out*
            (println n "has the fork" (mod (inc n) (count forks))))
          (Thread/sleep dining-time))))))

(defn run-phil [n cycles eat-time think-time]
        ; #(..) - анонимная функция с аргументом %
        ; ref x - возвращает ref cо значением x
  (let [forks (map #(ref %) (repeat n 0)), ; получаем ссылки на номера вилок
        ; future - для вызова тела объекта в дургом потоке
        philosophers (map #(future (philosopher cycles eat-time think-time % forks)) (range n))] ; каждый филосов - отдельный поток
    (map deref philosophers))) ; map deref - ожидает результат future

(def n 5)
(def cycles 1)

(time (println (run-phil n cycles 100 200)))
; @ - получить текущее значение по ссылке
(println "Number of transactions:" (- @transactions-count (* n cycles)))
