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
    (when-let [data (request key login "shorten"
                             {"longUrl" url, "domain" domain})]
      (-> data :data :url)))
  
  (expand [this url]
    (-> (request key login "expand" {"shortUrl" url})
        :data
        :expand
        first
        :long_url)))

;; This auth-map can contain a :domain key. If it isn't there, `"bit.ly"` will be used.
(defmethod shortener :bitly [_ {{:keys [key login domain]
                                 :or {domain "bit.ly"}}
                                :bitly}]
  (->Bitly key login domain))

;; For j.mp URLs.
(defmethod shortener :jmp [_ auth]
  (shortener :bitly auth))

(defn bitly-shortener
  "Create a Bitly instance."
  [key login & [domain]]
  (shortener :bitly {:bitly {:key key :login login :domain domain}}))