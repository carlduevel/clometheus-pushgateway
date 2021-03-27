# clometheus pushgateway

__clometheus__ is a library for pushing [Prometheus](https://prometheus.io/)
metrics to a [Pushgateway](https://github.com/prometheus/pushgateway).

[![Clojars Project](https://img.shields.io/clojars/v/online.duevel/clometheus-pushgateway.svg)](https://clojars.org/online.duevel/clometheus-pushgateway)

[![Build Status](https://travis-ci.com/carlduevel/clometheus-pushgateway.svg?branch=master)](https://travis-ci.com/carlduevel/clometheus-pushgateway)

[![Dependencies
Status](https://versions.deps.co/carlduevel/clometheus-pushgateway/status.svg)](https://versions.deps.co/carlduevel/clometheus-pushgateway)

# Usage

```clojure
(require '[clometheus-pushgateway.core :as pg])
(pg/start-pushgateway-reporter! "my-host.my-org.com" "my-batch-job-name")
```


## License

This project is licensed under the Apache License 2.0.
