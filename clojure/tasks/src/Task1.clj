(ns Task1)

; берем слово и начинаем лепить на него буквы из алфавита
(defn scaleWord [word, alphabet]
  (filter (fn [x] (not (nil? x))) ; Удаляем nil значение, появятся, если if () => false
          (map (fn [letter] (if (not= (compare letter (str (first word))) 0) (str letter word))) alphabet) ; на весь алфавит применяется анонимная функция
          ) ; берем голову word и текущую букву из алфавита, если они не одинаковые, склеиваем их
  )


(defn scaleWords [words alphabet]
  (map (fn [word] (scaleWord word alphabet)) words) ; к каждому word применям scaleWord
  )

(defn main [alphabet, N]
  (if (== N 1)
    (alphabet)
    (sort (reduce (fn [words _] (reduce concat (scaleWords words alphabet))) alphabet (range 1 N)))
    )
  )

; (reduce + [1 2 3 4 5]) => 15
; (concat [1 2] [3 4]) => (1 2 3 4)

(println (main ["a" "b" "c" "d"] 2))
