clj-runkeeper
=============

Runkeeper API Clojure Wrapper

## Usage
```clj
(ns im.a.happy.namespace
    (:use notch.clj-runkeeper))

;;First add your client id and client secret to runkeeper.properties.clj

;; Send the user to the URL returned by this
(get-auth-url "http://notch.me/somethinghere")

;;The above redirects to something like:
;;http://notch.me/somethinghere?code=1234567890

;;Then complete oauth by getting an access token
;;returns something like {:access_token "accesstoken123"}
(def auth (complete-oauth "1234567890" "http://notch.me/somethinghere"))

;;List the user's activities
 (get-fitness-activities auth)

 ;;Get details of a specific activity
 ;;(Note: Runkeeper API doc wants URIs to be generated dynamically
 ;;from their responses. So :uri would come from the response to get-fitness-activities
 (let [first_activity_uri (:uri (first (get-fitness-activities test_auth)))]
    (get-fitness-activity auth first_activity_uri)
 )