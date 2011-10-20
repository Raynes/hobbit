(ns hobbit.isgd-test
  (:use [hobbit core isgd utils]
        clojure.test))

(def long "http://blog.raynes.me")

(deftest shorten-test
  (is (link? (shorten (isgd-shortener) long)))
  (is (link? (shorten (isgd-shortener) long))))

(deftest custom-shorten-test
  (let [r (random-string)]
    (is (.endsWith (shorten-custom (isgd-shortener) long r) r))))