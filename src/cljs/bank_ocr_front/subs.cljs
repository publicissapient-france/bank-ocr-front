(ns bank-ocr-front.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
  ::file-name  
  (fn [db]
    (if (nil? (:chosen-file db)) "Choose a file..." (.-name (:chosen-file db)) )))     

(re-frame/reg-sub 
  ::file-chosen
  (fn [db]
    (if (some? (:chosen-file db)) true false)
    )
  )
