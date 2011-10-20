(ns hobbit.bitly-test
  (:use [hobbit core bitly utils]
        clojure.test))


(def auth (:bitly (read-auth)))
(def long "http://blog.raynes.me")

(deftest shorten-test
  (is (shorten (bitly-shortener (:key auth) (:login auth)) long))
  (is (shorten (bitly-shortener (:key auth) (:login auth) "j.mp") long)))