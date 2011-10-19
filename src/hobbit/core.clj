(ns hobbit.core)

(defprotocol Shortener
  "An API for shortening and expanding URLs."
  (shorten [this url] "Shorten a URL.")
  (expand  [this url] "Expand a URL."))

(defmulti shortener #(first %&))