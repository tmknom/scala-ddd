package cores.internal.request

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
    * リクエストIDのインスタンス生成
    *
    * @return リクエストID
    */
  def apply(value: String): RequestId = {
    RequestIdImpl(value)
  }

  /**
    * リクエストIDの初期化
    *
    * @return リクエストID
    */
  def initialize(): RequestId = {
    val value = UUID.randomUUID().toString
    apply(value)
  }
}

private final case class RequestIdImpl(value: String) extends RequestId
