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

(re-frame/reg-event-db
  :file-uploaded
  (fn [db [_ message]]
    (assoc db :message message)
    )
  )

(defn upload-file [db]
  (go (let [file (:chosen-file db)
            response (<! (http/post "https://localhost:5001/api/ocr"
                                    {:with-credentials? false
                                     :multipart-params {"file" file}}))]
        (prn (:status response))
        (prn (js->clj response))
        (re-frame/dispatch [:file-uploaded (:message (:body response))])
        ))
  )

(re-frame/reg-event-db
  :choose-file
  (fn [db [_ file]]
    (assoc db :chosen-file file)
    ))

(re-frame/reg-event-db
  :upload-file
  (fn [db _]
    (upload-file db)
  ;;  (assoc db :chosen-file nil)  
    ))

