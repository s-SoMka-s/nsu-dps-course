(ns Task4
  (:import (java.text SimpleDateFormat)
           (java.util Date)))

(declare supply-msg)
(declare notify-msg)

(defn storage
  "Создает новое хранилище
   ware - наименование изделия для хранения (string)
   notify-step - количество сохраненных элементов, необходимых для реакции регистратора.
   consumers - фабрики, которые нужно уведомлять, когда хранилище обновляется
   returns a map that contains:
     :storage - атом для хранения предметов, которые могут использоваться фабриками напрямую
     :ware - наименование изделия для хранения
     :worker - агент для отправки сообщения поставки (notify-msg)"
  [ware notify-step & consumers]
  ;; Атомы предоставляют способ управления общим, синхронным, независимым состоянием.
  ;; : - индикатор ключевого слова, ключ в map-e
  (let [counter (atom 0 :validator #(>= % 0)),
        worker-state {:storage     counter,
                      :ware        ware,
                      :notify-step notify-step,
                      :consumers   consumers}]
    {:storage counter,
     :ware    ware,
     :worker  (agent worker-state)}))

(defn factory
  "Создаем новую фабрику
   amount - количество изделий, произведенных за цикл
   duration - продолжительность цикла в миллисекундах
   target-storage - хранилище для продуктов с supply-msg
   ware-amounts - список наименований изделий и их количество, необходимое для одного цикла
   returns a map that contains:
     :worker - агент для отправки сообщения поставки (notify-msg)"
  [amount duration target-storage & ware-amounts]
  (let [bill (apply hash-map ware-amounts),
        buffer (reduce-kv (fn [acc k _] (assoc acc k 0))
                          {} bill),
        ;;a state of factory agent:
        ;;  :amount
        ;;  :duration
        ;;  :target-storage
        ;;  :bill - map-a с названиями товаров в качестве ключей и их количеством значений
        ;;          показывает, сколько изделий необходимо израсходовать для выполнения одного производственного цикла
        ;; то есть bill - маппинг, ключи - названия деталей, значения - их количество для одного производственного цикла.
        ;;  :buffer - map-a со структурой, аналогичной :bill, которая показывает, сколько товаров уже собрано;
        ;;          это единственная изменяемая часть.
        ;; то есть buffer - маппинг, ключи - названия деталей, значения - сколько их уже накопилось
        worker-state {:amount         amount,
                      :duration       duration,
                      :target-storage target-storage,
                      :bill           bill,
                      :buffer         buffer}]
    {:worker (agent worker-state)}))

(defn source
  "Создает источник, представляющий собой поток, производящий 'amount' изделий за цикл для хранения в 'target-storage'
   с заданной 'duration' цикла в миллисекундах
   returns Thread that must be run explicitly"
  [amount duration target-storage]
  (new Thread
       (fn []
         (Thread/sleep duration)
         (send (target-storage :worker) supply-msg amount)
         (recur))))

(defn supply-msg
  "Сообщение, которое может быть отправлено работнику хранилища, чтобы уведомить о том, что данный 'amount' изделий нужно добавить.
   Добавляет заданный 'amount' изделий в хранилище и уведомляет все зарегистрированные фабрики об этом
   state - описание см в коде 'storage'"
  [state amount]
  ;; swap! - меняет значение атома
  (swap! (state :storage) #(+ % amount))
  (let [ware (state :ware),
        cnt @(state :storage),
        notify-step (state :notify-step),
        consumers (state :consumers)]
    ;;logging part, notify-step == 0 means no logging
    (when (and (> notify-step 0)
               (> (int (/ cnt notify-step))
                  (int (/ (- cnt amount) notify-step))))
      (println (.format (new SimpleDateFormat "hh.mm.ss.SSS") (new Date))
               "|" ware "amount: " cnt))
    ;;factories notification part
    (when consumers
      (doseq [consumer (shuffle consumers)]
        (send (consumer :worker) notify-msg ware (state :storage) amount))))
  state)

; у нас есть список ключ-значение. Мы берем необх. ключ (текущей ware) и берем значение. Проверяем У нас сейчас >= необходимого
; если достаточно деталей то ставим галочку и так для каждого элемента
(defn enough-ware? [ware_raw buffer]
  (some (fn [kv] (and (= (key ware_raw) (key kv)) (>= (val kv) (val ware_raw)))) (seq buffer)))

(defn update-map [m f]
  (reduce-kv (fn [m k v]
               (assoc m k (f k v))) {} m))

; берем детали для производства и очищаем буфер на эти детали
(defn clear-buffer [buffer bill]
  (update-map buffer (fn [key val] (- val (bill key)))))

(defn notify-msg
  "Сообщение, которое можно отправить заводскому рабочему, чтобы уведомить о том, что 'amount' изделий
       отправилось в 'storage-atom'."
  ;; 'state' - агент созданный в 'factory'
  ;; Требования к реализации:
  ;; - попытаться получить некоторые элементы из 'storage-atom' если необходимо
  ;; - если поиск не удался, не забудьте правильно обработать исключение проверки
  ;; - если поиск прошел успешно, поместите товары во внутренний ':buffer'
  ;; - при наличии достаточного количества товаров всех видов по :bill, новый цикл должен быть запущен с заданной продолжительностью;
  ;;   после его завершения все изделия должны быть удалены из внутреннего ':buffer' и ':target-storage' должен быть уведомлен
  ;;   с помощью 'supply-msg'
  ;; - return новое состояние агента с возможным изменением ':buffer' при любых обстоятельствах!

  ; Приходит сообщение, что склад пополнился деталями. Соответственно фабрики могут взять к себе детали на производство
  [state ware storage-atom amount]
  (let [required_count (- ((state :bill) ware) ((state :buffer) ware)) ; required_count сколько деталей нам нужно - Разница между кол-во нужных и текущем кол-вом
        updated_state (if (> required_count 0)              ; если это число > 0 значит что-то требуется произвести
                        (try
                          (let [cnt_to_take (min @storage-atom required_count)] ; cnt_to_take берем минимум из storage. Например. Нам надо 5 деталей. У нас 1000. Мы берем 5.
                            (swap! storage-atom (fn [x] (- x cnt_to_take))) ; заменяем значение деталей на складе (из текущего вычитаем сколько мы берем со склада)
                            (let [newBuffer (update (state :buffer) ware (fn [prev_count] (+ prev_count cnt_to_take)))] ; в фабрику добавляем новые детали
                              (update state :buffer (fn [_] newBuffer)))) ; обновляем буффер
                          (catch IllegalStateException _ state))
                        state)]

    (if (every? (fn [ware_raw] (enough-ware? ware_raw (state :buffer))) (seq (state :bill))) ; проверяем, достаточно ли у нас деталей для производства. Каждая деталь должна быть в достаточном кол-ве
      (let [state_clear_buffer (update state :buffer (fn [buff] (clear-buffer buff (state :bill))))] ; тогда производим путем обновления буфера.
        (Thread/sleep (state :duration))                    ; производство занимает какое-то время
        (send ((state :target-storage) :worker) supply-msg (state :amount)) ; отправляем сообщение чтобы дальше следующий делал
        state_clear_buffer)
      updated_state)))


;; Хранение часов: это хранилище, хранит сейфы, 1 - число хранимых вещей для реакции логгера, потребителя нет
(def safe-storage (storage "Safe" 1))

;; Производство часов: это фабрика, 1 - число деталей за цикл, 3000 мс - время производства,
;; складываем детали в safe-storage, для производства требуется 3 единицы металла.
(def safe-factory (factory 1 3000 safe-storage "Metal" 3))

;; Хранение часов: это хранилище, хранит часы, 1 - число хранимых вещей для реакции логгера, потребителя нет
(def cuckoo-clock-storage (storage "Cuckoo-clock" 1))

;; Производство часов: это фабрика, 1 - число деталей за цикл, 2000 мс - время производства,
;; складываем детали в cuckoo-clock-storage, для производства требуется 5 брёвен и 10 шестерёнок.
(def cuckoo-clock-factory (factory 1 2000 cuckoo-clock-storage "Lumber" 5 "Gears" 10))

;; Хранение деталей: это хранилище, хранит детали, 20 - число хранимых вещей для реакции логгера,
;; cuckoo-clock-factory - потребитель
(def gears-storage (storage "Gears" 20 cuckoo-clock-factory))

;; Производство шестерёнок: это фабрика, 4 - число деталей за цикл, 1000 мс - время производства,
;; складываем детали в gears-storage, для производства требуется 4 единиц руды.
(def gears-factory (factory 4 1000 gears-storage "Ore" 4))

;; Хранение металла: это хранилище, хранит металл, 5 - число хранимых вещей для реакции логгера,
;; safe-factory - потребитель
(def metal-storage (storage "Metal" 5 safe-factory))

;; Производство металла: это фабрика, 1 - число деталей за цикл, 1000 мс - время производства,
;; складываем детали в metal-storage, для производства требуется 10 единиц руды/
(def metal-factory (factory 1 1000 metal-storage "Ore" 10))

;; Хранение брёвен: это хранилище, хранит брёвна, 20 - число хранимых вещей для реакции логгера,
;; cuckoo-clock-factory - потребитель
(def lumber-storage (storage "Lumber" 20 cuckoo-clock-factory))

;; Добыча брёвен: это источник (создаём поток), 5 - число деталей за цикл, 4000 мс - время производства,
;; складываем детали в lumber-storage
(def lumber-mill (source 5 4000 lumber-storage))

;; Хранение руды: это хранилище, хранит руду, 10 - число хранимых вещей для реакции логгера,
;; metal-factory и gears-factory - потребители
(def ore-storage (storage "Ore" 10 metal-factory gears-factory))

;; Добыча руды: это источник (создаём поток), 2 - число деталей за цикл, 1000 мс - время производства,
;; складываем детали в ore-storage
(def ore-mine (source 2 1000 ore-storage))

(defn start []
  (.start ore-mine)
  (.start lumber-mill))

(defn stop []
  (.stop ore-mine)
  (.stop lumber-mill))


(defn run []
  (start)
  (Thread/sleep 50000)
  (stop))

(run)