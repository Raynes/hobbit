(ns hobbit.utils)

(defn read-auth []
  (read-string (slurp "auth.clj")))