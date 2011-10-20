(ns hobbit.core
  (:require [clojure.string :as string])
  (:import java.net.URL))

(defprotocol Shortener
  "An API for shortening and expanding URLs."
  (shorten [this url] "Shorten a URL.")
  (expand  [this url] "Expand a URL."))

(defn- sanitize [s]
  (keyword
   (string/replace
    (if (re-find #"://" s)
      (-> s URL. .getHost)
      s) #"[^\w]" "")))

(defn- dispatch [x & args]
  (if (keyword? x)
    x
    (sanitize x)))

(defmulti shortener
  "Create a Shortener for a service determined by dispatch. Dispatch value
   can be a string or keyword. If it is a string then we check to see if
   it is a URL, in which case the domain is found and pulled out. The string
   is sanitized (whether it is a URL or not) by filtering out any non-word
   characters and then `keyword` is called on it, making it a keyword. The
   final dispatch value is always a keyword."
  dispatch)