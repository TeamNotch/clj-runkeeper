(ns notch.test-clj-runkeeper
  (:use clojure.set)
  (:use clojure.tools.logging)
  (:require [clojure.data.json :as json])
  (:require [clj-http.client :as http])
  (:require [clojure.java.io :as io])
  (:use clojure.test)
  (:use notch.clj-runkeeper :reload)

  )


(do
  (def test_auth {:access_token (:test_access_token properties)})
  )
(->>
(get-fitness-activity test_auth (:uri (first (get-fitness-activities test_auth))))
:path
(map #(println (str (:timestamp %) ", " (:latitude %) ", " (:longitude %) ", " (:altitude %))))
)


(deftest test-basics

  ;;Rough smoke test
  (is (string? (get-auth-url "http://notch.me/somethinghere")))
  (is (string? (get-auth-url "http://notch.me/somethinghere" "somestatehere")))
  (is (map? (get-user test_auth)))
  (is (map? (get-user-profile test_auth)))
  (is (vector? (get-fitness-activities test_auth)))
  (is (vector? (get-fitness-activities test_auth 1)))
  (is (vector? (get-fitness-activities test_auth 0 3)))
  (is (map? (get-fitness-activity test_auth (:uri (first (get-fitness-activities test_auth))))))

  )

;(run-tests 'notch.test-clj-runkeeper)
