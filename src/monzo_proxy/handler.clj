(ns monzo-proxy.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clj-http.client :as client]
            [environ.core :refer [env]]))

(def client-secret (env :client-secret))

(defroutes app-routes
  (GET "/" [] "Service Running")
  (GET "/oauth2/token" {params :params}
       (-> (client/get "https://auth.getmondo.co.uk/"
                       {:query-params (assoc params :client_secret client-secret)})
           (select-keys [:body :status :headers])))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
