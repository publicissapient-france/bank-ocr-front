(ns bank-ocr-front.events
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [cljs-http.client :as http]
    [ajax.core :as ajax]
    [cljs.core.async :refer [<!]]
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx :as http-fx]
    [bank-ocr-front.db :as db]
    ))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-db
  :file-upload-success
  (fn [db [_ body]]
    (assoc db :message (:message body))
    )
  )

(re-frame/reg-event-db
  :file-upload-failure
  (fn [db [_ body]]
    (assoc db :message "Something went wrong... try another file")
    )
  )



(defn upload-file-old [db]
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

(re-frame/reg-event-fx
  :upload-file
  (fn [{:keys [db]} _]
    (let [file (:chosen-file db)
          filename (.-name db)
          form-data (doto
                      (js/FormData.)
                      (.append "id" "file")
                      (.append "file" file filename))]
      {:db (assoc db :show-spinner true)
       :http-xhrio {:method :post
                    :uri "https://localhost:5001/api/ocr"
                    :body form-data
                    :timeout 5000
                    :with-credentials false
                    :response-format (ajax/json-response-format {:keywords? true})
                    :on-success [:file-upload-success]
                    :on-failure [:file-upload-failure]
                    }
       })
    )
  )
