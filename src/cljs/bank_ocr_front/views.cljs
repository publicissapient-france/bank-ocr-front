(ns bank-ocr-front.views
  (:require
    [re-frame.core :as re-frame]
    [re-com.core :as re-com]
    [bank-ocr-front.subs :as subs]
    ))

(defn file-input [] 
  [re-com/box 
   :margin "20px 0 0 0"
   :child [:div 
           [:input {:type "file" :id "file" :name "file"
                    :style {:width "0.1px"
                            :height "0.1px"
                            :opacity "0"
                            :overflow "hidden"
                            :position "absolute"
                            :z-index "-1"}
                    :on-change #(re-frame/dispatch [:choose-file (-> % .-target .-files (aget 0))])}
            ]
           [
            :label {:for "file"
                    :style {
                            :height "50px"
                            :margin "0"
                            :font-size "12px"
                            :font-weight "700"
                            :text-transform "uppercase"
                            :color "white"
                            :line-height "46px"
                            :background-color "black"
                            :display "inline-block"
                            :width "200px auto"
                            :padding "0 1.25rem 0 1.25rem"
                            :border "2px solid #fff"
                            :border-radius "7px"
                            :cursor "pointer"
                            :box-shadow "0 10px 20px rgba(0,0,0,.2)"
                            :text-align "center"
                            }
                    } @(re-frame/subscribe [::subs/file-name])
            ]
           ]
   ]  
  )

(defn upload-button [] 
  [re-com/box 
   :margin "20px 0 0 0"
   :child [re-com/button 
           :style {
                   :background-color "rgb(254, 65, 77)"
                   :font-size "12px"
                   :font-weight "700"
                   :text-transform "uppercase"
                   :height "50px"
                   :color "#fff"
                   :border "2px solid #fff"
                   :border-radius "7px"
                   :box-shadow "0 10px 20px rgba(0,0,0,.2)"
                   :padding "0 1.25rem 0 1.25rem"
                   }
           :on-click #(re-frame/dispatch [:upload-file]) :label "Upload file" ]
   ]
  )

(defn upload-result []
  [re-com/box 
   :margin "20px 0 0 0"
   :height "50px"
   :style {
           :color "#000"
           :border "2px solid rgb(254, 65, 77)"
           :text-align "center"
           :line-height "48px"
           :font-size "30px"
           :font-weight "700"
           :border-radius "7px"
           :padding "0 1.25rem 0 1.25rem"
           }
   :child [:div @(re-frame/subscribe [::subs/message])]]
  )

(defn footer []
  [re-com/box 
   :width "100%"
   :height "10px"
   :style {:background-color "#FD414C"
           :position "fixed"
           :bottom "0"
           }
   :child [:div ]]
  )


(defn main-panel []
  [re-com/v-box 
   :height "400px"
   :align :center
   :children [[re-com/box 
               :width "100%"
               :height "70px"
               :style {:background-color "black"}
               :child [:div {:style {
                                     :background "no-repeat url('img/xebicon19-header.png') "
                                     :height "100%"
                                     }}]]
              [re-com/box :child [:h1 {:style {
                                               :color "black"
                                               :font-size "48px"
                                               :margin-top "70px"
                                               }} "Bank OCR"]]
              (file-input)
              (if @(re-frame/subscribe [::subs/file-chosen]) 
                (upload-button))
              (if @(re-frame/subscribe [::subs/message]) 
                (upload-result))
              (footer)]])
