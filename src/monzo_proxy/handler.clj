(ns monzo-proxy.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [clj-http.client :as client]
            [environ.core :refer [env]]))

(def client-secret (env :client-secret))

(defroutes app-routes
  (GET "/" [] "Service Running")
  (POST "/oauth2/token" {params :form-params}
        (-> (client/post "https://api.monzo.com/oauth2/token"
                         {:form-params (assoc params :client_secret client-secret)
                          :throw-exceptions false})
            (select-keys [:body :status :headers])
            (update :headers #(assoc % :Access-Control-Allow-Origin "*"))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes
                 (merge api-defaults
                        {:params {:keywordize true
                                  :urlencoded true}})))
