(ns hobbit.core-test
  (:use [hobbit core bitly utils]
        clojure.test))

(deftest shortener-stress-test
  (let [url "http://bit.ly/p9f2Yi"
        long "http://blog.raynes.me"]
    (is (expand (shortener url (read-auth)) url))
    (is (shorten (shortener :bitly (read-auth)) long))
    (is (shorten (shortener "bit.ly" (read-auth)) long))
    (is (nil? (shortener nil)))))