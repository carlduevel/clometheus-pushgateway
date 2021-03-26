(ns clometheus-pushgateway.core-test
  (:require [clojure.test :refer [deftest is]]
            [clometheus-pushgateway.core :as sut]
            [clj-http.client :as http]
            [overtone.at-at :as at]))

(deftest start-pushgateway-reporter
  (let [expected '("https://my-host/metrics/job/my-job"
                   {:throw-entire-message true
                    :body                 ""
                    :content-type         "text/plain; version=0.0.4; charset=utf-8"
                    :socket-timeout       10000 :connection-timeout 10000})]
    (with-redefs
      [http/post (fn[& args] (is (= args expected)) )
       at/every  (constantly nil )]
      (sut/start-pushgateway-reporter! "my-host" "my-job"))))

