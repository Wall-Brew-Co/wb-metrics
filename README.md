# wb-metrics

A Wall Brew infrastructure library to monitor function performance utilizing [timbre.](https://github.com/ptaoussanis/timbre)
This library has utility functions to work with [clj-http](https://github.com/dakrone/clj-http) and [ring](https://github.com/ring-clojure/ring) to provide cross-application session and request IDs.

This repository follows the guidelines and standards of the [Wall Brew Open Source Policy.](https://github.com/Wall-Brew-Co/open-source "Our open source guidelines")

## DEPRECATION NOTICE

As of 2023-03-31, this library has been deprecated and will no longer receive updates.
If you wish to continue using the functionality within this library, feel free to fork this repository or to reuse the contents within: As a reminder, this code is provided under the MIT License.

This reflects the natural evolution of observability enhancements across our industry, and an explicit preference for community standards over local optima.

As of writing, Wall Brew now prefers the following alternatives:

- [OpenTelemetry](https://opentelemetry.io/) for distributed log correlation
- [Logback](https://logback.qos.ch/) for log formatting and filtering


## License

Copyright © 2020-2023 - [Wall Brew Co](https://wallbrew.com/)

This software is provided for free, public use as outlined in the [MIT License](https://github.com/Wall-Brew-Co/wb-metrics/blob/master/LICENSE)
