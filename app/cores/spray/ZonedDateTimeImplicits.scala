package cores.spray

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import spray.json.{DeserializationException, JsString, JsValue, RootJsonFormat}

/**
  * ZonedDateTime を spray で JSON に変換する
  *
  * @see http://stackoverflow.com/questions/25178108/converting-datetime-to-a-json-string
  */
object ZonedDateTimeImplicits {

  implicit object ZonedDateTimeJsonFormat extends RootJsonFormat[ZonedDateTime] {
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    override def write(obj: ZonedDateTime): JsString = JsString(formatter.format(obj))

    @SuppressWarnings(Array("org.wartremover.warts.Throw"))
    override def read(json: JsValue): ZonedDateTime = json match {
      case JsString(string) => ZonedDateTime.parse(string, formatter)
      case other => throw DeserializationException(s"Cannot parse json value $other as timestamp")
    }
  }

}
