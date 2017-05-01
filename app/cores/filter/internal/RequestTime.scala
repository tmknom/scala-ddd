package cores.filter.internal

/**
  * リクエスト実行時間
  */
private[filter] sealed trait RequestTime {
  def value: Long
}

/**
  * リクエスト実行時間のコンパニオンクラス
  */
private[filter] object RequestTime {
  def apply(value: Long): RequestTime = {
    RequestTimeImpl(value)
  }
}

private final case class RequestTimeImpl(value: Long) extends RequestTime
