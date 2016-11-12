(defproject taskdesk "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.8.8"]
            [compojure "1.1.6"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [mysql/mysql-connector-java "5.1.38"]
                 [ring/ring "1.5.0"]
                 [ring/ring-json "0.4.0"]
                 [metosin/ring-http-response "0.8.0"]
                 [compojure "1.5.1"]
                 [hiccup "1.0.5"]
                 [selmer "1.0.9"]
                 [enlive "1.0.0-20100502.112537-18"]]
  :dev-dependencies [[lein-ring "0.4.0"]]
  :ring { :handler taskdesk.core/engine })
