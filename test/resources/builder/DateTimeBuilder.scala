package resources.builder

import java.time.{LocalDateTime, ZonedDateTime}

import cores.datetime.DateTimeProvider

// scalastyle:off
object DateTimeBuilder {
  def fixedJST: ZonedDateTime = {
    ZonedDateTime.of(fixed, DateTimeProvider.JST)
  }

  def fixed: LocalDateTime = {
    LocalDateTime.of(2016, 12, 31, 23, 59, 59)
  }
}
