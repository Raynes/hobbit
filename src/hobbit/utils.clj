(ns hobbit.utils
  (:import java.net.URLEncoder))

(defn encode-url [url]
  (URLEncoder/encode url "UTF-8"))