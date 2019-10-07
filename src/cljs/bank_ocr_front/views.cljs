(ns bank-ocr-front.views
  (:require
    [re-frame.core :as re-frame]
    [re-com.core :as re-com]
    [bank-ocr-front.subs :as subs]
    ))

(defn main-panel []
  [re-com/v-box 
   :height "400px"
   :justify :center
   :align :center
   :children [
              [re-com/box :child [:h1 "Bank OCR"]]
              [re-com/box 
               :height "50px"
               :padding "1em"
               :child [:div 
                       [:input {:type "file" :id "file" :name "upload-file"
                                ;; TODO: Put styles in a separate file
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
                                        :font-size "1.25em"
                                        :font-weight "700"
                                        :color "white"
                                        :background-color "black"
                                        :display "inline-block"
                                        :padding "0.625rem 1.25rem"
                                        }
                                } @(re-frame/subscribe [::subs/file-name])
                        ]
                       ]
               ]

              (if @(re-frame/subscribe [::subs/file-chosen]) [re-com/box 
                                                              :child [re-com/button :on-click #(re-frame/dispatch [:upload-file]) :label "Upload file" ]
                                                              ])
              ]])
