(ns bank-ocr-front.events
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [ajax.core :as ajax]
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx :as http-fx]
    [cljs.core.async :refer [put! chan <! >!]]
    [bank-ocr-front.db :as db]))

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
    (assoc db :message "Something went wrong... try another file")))

;; File preview
(re-frame/reg-event-db
  :file-data
  (fn [db [_ data]]
    (assoc db :message data)))

(def extract-result
  (map #(-> % .-target .-result js->clj)))

(def upload-reqs (chan 1))
(def file-reads (chan 1 extract-result))

;; wait for a file to appear in the upload-reqs channel, and stick the file on the file-reads channel.
(go-loop []
  (let [reader (js/FileReader.)
        file (<! upload-reqs)]
    (set! (.-onload reader) #(put! file-reads %))
    (.readAsText reader file)
    (recur)))

;; wait for a string to appear in the file-reads channel and dispatch.
(go-loop []
  (re-frame/dispatch [:file-data (<! file-reads)])
  (recur))


(re-frame/reg-event-db
  :upload-reqs
  (fn [db [_ file]]
    (put! upload-reqs file)
    db))

(re-frame/reg-event-fx
  :choose-file
  (fn [{:keys [db]} [_ file]]
    {:dispatch [:upload-reqs file]
     :db (assoc db :chosen-file file)
     }))

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
