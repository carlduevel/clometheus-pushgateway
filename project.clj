(defproject online.duevel/clometheus-pushgateway "0.2.1"
  :description "Pushgateway library for clometheus"
  :url "https://github.com/carlduevel/clometheus-pushgateway"
  :license {:name "Apache License 2.0"
            :url  "https://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [online.duevel/clometheus "0.2.2"]
                 [org.clojure/tools.logging "1.1.0"]
                 [overtone/at-at "1.2.0"]
                 [clj-http "3.12.1"]]
  :lein-release {:deploy-via :clojars}
  :profiles {:dev {:plugins [[lein-release/lein-release "1.0.9"]]}})
