(ns hobbit.bitly
  (:use [clojure.data.json :only [read-json]]
        hobbit.core)
  (:require [clj-http.client :as http]))

(def bitly-url "http://api.bit.ly/v3/")

(defn- auth-map [key login] {"apiKey" key "login" login})

(defn- request [key login end arg-map]
  (-> (http/get
       (str bitly-url end)
       {:query-params (merge arg-map (auth-map key login))})
      :body
      read-json))

(defrecord Bitly [key login domain]
  Shortener
  
  (shorten [this url]
    (-> (request key login "shorten" {"longUrl" url, "domain" domain})
        :data
        :url))
  
  (expand [this url]
    (-> (request key login "expand" {"shortUrl" url})
        :data
        :expand
        first
        :long_url)))
