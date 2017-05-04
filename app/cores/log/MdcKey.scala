package cores.log

/**
  * logback の MDC(Mapped Diagnostic Context) のキー名を定義
  *
  * logback.xml の pattern 部に %X{requestId} のように記述する
  *
  * @see https://logback.qos.ch/manual/mdc.html
  */
private[cores] object MdcKey {
  val RequestId: String = "requestId"
}
