(ns hobbit.utils)

(defn read-auth []
  (read-string (slurp "auth.clj")))

(defn link? [s] (and (string? s) (.startsWith s "http")))

(def some-chars "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXY1234567890")

(defn random-string []
  (apply str (repeatedly 20 #(rand-nth some-chars))))