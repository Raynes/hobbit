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
   final dispatch value is always a keyword.

   Implementers of this method should always take a map as well. This map looks
   like `{:service auth-map, :service2 auth-map}` where `service` is the dispatch
   keyword for a service and `auth-map` is the authentication map that the service
   requires. If a particular service does not require an authentication map, then
   this map as a whole may be left out.

   The purpose behind this map-oriented approach is so that users can create a
   shortener from nothing but an already-shortened URL and the shortener will be
   ready, authentication information and all. You only need to provide keys in the
   auth map for services that you wish to support. The idea behind all of this is that
   you can call `expand` on any given shortened URL and get the expanded URL without
   ever even knowing what service it was shortened with in the first place."
  dispatch)