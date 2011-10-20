;; # Bitly
;; Bitly is a URL shortening service with a large, useful API. In order to use it, you need a API
;; key. You can get one from [the sign up page](http://bitly.com/a/sign_up).
;; We're not going to implement the entire API. We're only going to implement the pieces that
;; do not require oauth.

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

;; Bitly requires an API key and login. They are not optional. Furthermore, bitly also owns
;; the [j.mp](j.mp) `domain`. If you set `domain` to "j.mp", it will prefer that domain.
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

;; If domain isn't set, `"bit.ly"` is used.
(defmethod shortener :bitly [_ key login & [domain]]
  (->Bitly key login (or domain "bit.ly")))