(ns bank-ocr-front.events
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]]
    [re-frame.core :as re-frame]
    [bank-ocr-front.db :as db]
    ))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

(defn upload-file [upload-file]
  (go (let [response (<! (http/post "https://localhost:5001/api/ocr"
                                    {:with-credentials? false
                                     :multipart-params {"file" upload-file}}))]
        (prn (:status response))
        (prn (js->clj response))))
  )

(re-frame/reg-event-db
  :choose-file
  (fn [db [_ upload-file]]
    (assoc db :chosen-file upload-file)
    ))

(re-frame/reg-event-db
  :upload-file
  (fn [db _]
    (upload-file (:chosen-file db))
    (assoc db :chosen-file nil)  
    ))

