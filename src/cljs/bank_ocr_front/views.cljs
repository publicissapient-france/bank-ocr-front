(ns bank-ocr-front.views
  (:require
    [re-frame.core :as re-frame]
    [re-com.core :as re-com]
    [bank-ocr-front.subs :as subs]))

(defn file-input [] 
  [re-com/box 
   :class "file-input-container"
   :child [:div 
           [:input {:type "file" :id "file" :name "file"
                    :class-name "file-input"
                    :on-change #(re-frame/dispatch [:choose-file (-> % .-target .-files (aget 0))])}
            ]
           [
            :label {:for "file"} @(re-frame/subscribe [::subs/file-name])
            ]
           ]])

(defn upload-button [] 
  [re-com/box 
   :child [re-com/button 
           :class "upload-button"
           :on-click #(re-frame/dispatch [:upload-file]) :label "Upload file" ]])

(defn split-new-line [input] 
  (clojure.string/split input #"\n"))

(defn upload-result []
  (let [message @(re-frame/subscribe [::subs/message])
        lines (split-new-line message)]
        [re-com/box 
         :class "upload-result"
         :child [:pre (for [line lines]
                        [:div {:key line} line])]]
        ))

(defn footer []
  [re-com/box 
   :class "footer"
   :child [:div ]])


(defn main-panel []
  [re-com/v-box 
   :align :center
   :class "main-panel"
   :children [[re-com/box 
               :class "nav"
               :child [:div {:class-name "nav-logo"}]]
              [re-com/box :child [:h1 {:class-name "title"} "Bank OCR"]]
              (file-input)
              (if @(re-frame/subscribe [::subs/file-chosen]) 
                (upload-button))
              (if @(re-frame/subscribe [::subs/message]) 
                (upload-result))
              (footer)]])
