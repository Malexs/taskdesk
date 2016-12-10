(ns taskdesk.dsl.struct-func
  (:use [clojure.template]))

(defn join*
  [{:keys [tables joins] :as q} type alias table on]
  (let [a (or alias table)]
    (let [jns (assoc q
                :tables (assoc tables a table)
                :joins (conj (or joins []) [a type on]))]
      jns)
    ))

(defn from
  ([q table] (join* q nil table table nil))
  ([q table alias] (join* q nil table alias nil)))

(defn- conj-expression
  [e1 e2]
  (cond
    (not (seq e1)) e2
    (= 'and (first e1)) (conj (vec e1) e2)
    :else (vector 'and e1 e2)))

(defn where*
  [query expr]
  (assoc query :where (conj-expression (:where query) expr)))

(defn function-symbol? [s]
  (and (re-matches #"\w+" (name s)) (not (= "and" (name s)))))

(defn- canonize-operator-symbol
  [op]
  (get '{not= <>, == =} op op))

(defn prepare-expression
  [e]
  (if (seq? e)
    `(vector
       (quote ~(canonize-operator-symbol (first e)))
       ~@(map prepare-expression (rest e)))
    e))

(defmacro where
  [q body]
  `(where* ~q ~(prepare-expression body)))





(defn- map-vals
  [f m]
  (into (if (map? m) (empty m) {}) (for [[k v] m] [k (f v)])))

(def surrogate-alias-counter (atom 0))

(defn generate-surrogate-alias
  []
  (let [k (swap! surrogate-alias-counter #(-> % inc (mod 1000000)))]
    (keyword (format "__%08d" k))))

(defn as-alias
  [n]
  (cond
    (keyword? n) n
    (string? n) (keyword n)
    :else (generate-surrogate-alias)))

(defn- prepare-fields
  [fs]
  (if (map? fs)
    (map-vals prepare-expression fs)
    (into {} (map (juxt as-alias prepare-expression) fs))))

(defn fields*
  [query fd]
  (assoc query :fields fd))

(defmacro fields
  [query fd]
  `(fields* ~query ~(prepare-fields fd)))


(do-template
  [join-name join-key]


  (defmacro join-name
    ([relation alias table cond]
     `(join* ~relation ~join-key ~alias ~table ~(prepare-expression cond)))
    ([relation table cond]
     `(let [table# ~table]
        (join* ~relation ~join-key nil table# ~(prepare-expression cond)))))

  join-inner :inner,
  join :inner,
  join-right :right,
  join-left :left,
  join-full :full)


