(ns notch.clj-runkeeper
  (:use clojure.set)
  (:use clojure.tools.logging)
  (:require [clojure.data.json :as json])
  (:require [clj-http.client :as http])
  (:require [clojure.java.io :as io]))


(do
  (def properties (-> (io/resource "runkeeper.properties.clj")
                    (io/as-file)
                    (slurp)
                    (read-string)))

  (def client_id (:client_id properties))
  (def client_secret (:client_secret properties))
  (def api_url "https://api.runkeeper.com" )
  )

(defn get-auth-url
  "Send the user to this URL for first part of OAuth"
  [callback_url]
  (str "https://runkeeper.com/apps/authorize?"
    "client_id=" client_id
    "&response_type=code"
    "&redirect_uri=" (java.net.URLEncoder/encode callback_url)))


(defn complete-oauth
  "Call this to complete OAuth process.
  code is what's returned from auth-url
  callback_url is callback_url used in get-auth-url"
  [code callback_url]
  (-> (http/post "https://runkeeper.com/apps/token"
        {:form-params {:grant_type "authorization_code"
                       :code code
                       :client_id client_id
                       :client_secret client_secret
                       :redirect_uri callback_url
                       }})
    :body
    json/read-json
    ))

(defn get-user
  "Returns user ID + URIs for health graph sections"
  [{access_token :access_token}]
  (-> (http/get (str api_url "/user")
        {:oauth-token access_token})
    :body
    json/read-json
    )
  )

(defn get-fitness-activities
  "Returns a list of fitness activities (runs)"
  [{access_token :access_token}]
  (let [url (str api_url (:fitness_activities (get-user {:access_token access_token})))]
    (-> (http/get url
          {:oauth-token access_token})
      :body
      json/read-json
      :items
      )
    )
  )

(defn get-fitness-activity
  "Returns the details of a fitness activity (run)"
  [{access_token :access_token} uri]
  (let [url (str api_url uri)]
    (-> (http/get url
          {:oauth-token access_token})
      :body
      json/read-json
      )
    )
  )