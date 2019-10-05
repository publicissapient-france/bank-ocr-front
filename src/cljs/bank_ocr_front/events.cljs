(ns bank-ocr-front.events
  (:require
   [re-frame.core :as re-frame]
   [bank-ocr-front.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
