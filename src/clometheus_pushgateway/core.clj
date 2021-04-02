(ns clometheus-pushgateway.core
  (:require [clojure.tools.logging :as log]
            [clj-http.client :as http]
            [clometheus.core :as c]
            [clometheus.txt-format :as txt]
            [overtone.at-at :as at])
  (:import (java.net URLEncoder)
           (java.lang Thread)))

(defn- pushgateway-url [host-name job-name]
  (format "https://%s/metrics/job/%s" host-name (URLEncoder/encode job-name "UTF-8")))

(defn- push [registry host-name job-name]
  (http/post (pushgateway-url host-name job-name)
             {:throw-entire-message true
              :body                 (txt/txt-format registry)
              :content-type         "text/plain; version=0.0.4; charset=utf-8"
              :socket-timeout       10000
              :connection-timeout   10000}))

(defn- exception-to-log [f & args]
  (try (apply f args)
       (catch Exception e
         (log/error e "Exception caught in scheduled job."))))

(defn start-pushgateway-reporter!
  "Pushes metrics once and then schedules a job to do it regularly.
   Metrics are pushed a last time on shutdown, so no updates are lost.
   Returns a scheduled-fn in case the client wants to cancel it later on.
   Example usage:
   ```clojure
   (start-pushgateway-reporter! \"my-host.my-org.com\" \"my-batch-job-name\")
   ```
  "
  [host-name job-name &
   {interval-ms :interval-ms registry :registry :or {interval-ms 15000 registry c/default-registry}}]
  (let [push (partial push registry host-name job-name)]
    (push)
    (let [schedueled-fn (at/every  interval-ms (partial exception-to-log push) (at/mk-pool)
                                   :initial-delay interval-ms :desc "Push Gateway")]
      (.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable (partial exception-to-log push)))
      (log/info "Push Gateway started")
      schedueled-fn)))
