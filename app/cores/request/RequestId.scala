package cores.request

import java.util.UUID

/**
  * リクエストID
  *
  * HTTPリクエストされるたびに、ユニークなIDを払い出す。
  * ログのトラーサビリティ向上が目的。
  */
private[cores] sealed trait RequestId {
  def value: String
}

/**
  * リクエストIDのコンパニオンクラス
  */
private[cores] object RequestId {
  /**
    * リクエストIDの初期化
    *
    * @return リクエストID
    */
  def initialize(): RequestId = {
    val value = UUID.randomUUID().toString
    RequestIdImpl(value)
  }
}

private final case class RequestIdImpl(value: String) extends RequestId
