(ns clometheus-pushgateway.core
  (:require [clojure.tools.logging :as log]
            [clj-http.client :as http]
            [clometheus.txt-format :as txt]
            [overtone.at-at :as at])
  (:import (java.net URLEncoder)
           (java.lang Thread)))

(defn- pushgateway-url [host-name job-name]
  (format "https://%s/metrics/job/%s" host-name (URLEncoder/encode job-name "UTF-8")))

(defn- push [host-name job-name]
  (http/post (pushgateway-url host-name job-name)
             {:throw-entire-message true
              :body                 (txt/txt-format)
              :content-type         "text/plain; version=0.0.4; charset=utf-8"
              :socket-timeout       10000
              :connection-timeout   10000}))

(defn- exception-to-log [f & args]
  (try (apply f args)
       (catch Exception e
         (log/error e "Exception caught in scheduled job."))))

(defn start-pushgateway-reporter [host-name job-name & {interval-ms :interval-ms :or {interval-ms 15000}}]
  (let [push (partial push host-name job-name)]
    (push)
    (at/every  interval-ms (partial exception-to-log push) (at/mk-pool)
               :initial-delay interval-ms :desc "Push Gateway")
    (.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable (partial exception-to-log push)))
    (log/info "Push Gateway started")))



