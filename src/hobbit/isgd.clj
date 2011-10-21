;; # Is.gd
;; is.gd probably has the easiest API of all, since you can get URLs back in plaintext. No
;; JSON parsing!
(ns hobbit.isgd
  (:use hobbit.core)
  (:require [clj-http.client :as http]))

(defn shorten-isgd [url & [custom]]
  (:body (http/get "http://is.gd/create.php"
                   {:query-params (merge {"format" "simple"
                                          "url" url}
                                         (when custom {:shorturl custom}))})))

;; Isgd doesn't require any authentication. It does, however, support a `shorturl` parameter
;; that lets a user set what the URL will look like.
(defrecord Isgd []
  Shortener
  
  (shorten [this url] (shorten-isgd url))
  (expand [this url]
    (:body (http/get "http://is.gd/lookup.php"
                     {:query-params {"format" "simple", "shorturl" url}})))

  CustomizableShortner

  (shorten-custom [this url custom] (shorten-isgd url custom)))

;; No authentication. Just a shortcut for ->Isgd.
(defmethod shortener :isgd [& _]
  (->Isgd))

(defn isgd-shortener
  "Convenience function for creating an isgd shortner."
  [] (shortener :isgd))