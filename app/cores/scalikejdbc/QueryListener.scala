package cores.scalikejdbc

import play.api.Logger
import play.api.libs.json.Json
import scalikejdbc.GlobalSettings.QueryCompletionListener

/**
  * SQL クエリ実行後にフックして処理を差し込む
  *
  * ScalikeJDBC 標準のログ出力機構は loggingSQLAndTime であるが
  * これをそのまま使うと、秘匿情報も含めてそのままログに垂れ流しになるので
  * マスク処理をしたうえで、ログ出力するように実装することとした。
  *
  * @see http://scalikejdbc.org/documentation/query-inspector.html
  */
object QueryListener {
  /**
    * マスク対象の文字列のプレフィックス
    */
  private val SecretPrefix = "secret"

  /**
    * Event handler to be called every query completion.
    */
  def queryCompletionListener: QueryCompletionListener = (sql: String, params: Seq[Any], millis: Long) => {
    val stringParams = rawOrMaskedParams(sql, params)
    val message = createMessage(sql, stringParams, millis)
    Logger.debug(message)
  }

  private def createMessage(sql: String, stringParams: Seq[String], millis: Long): String = {
    Json.obj(
      "query" ->
        Json.obj(
          "sql" -> sql,
          "params" -> stringParams,
          "millis" -> millis
        )
    ).toString()
  }

  // QueryCompletionListener が Seq[Any] を引数に取るので、どうしても警告が出るので抑制
  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.ToString"))
  private def rawOrMaskedParams(sql: String, params: Seq[Any]): Seq[String] = {
    if (includeSecret(sql)) {
      // 雑に masked って文字で全部差し替えてるが、もうちょっと頑張ってもいいかも
      Seq.fill(params.length)("masked")
    } else {
      // Seq[Any] のまま引き回すと、警告がうるさいので、強制的に String に変換する
      params.map(_.toString)
    }
  }

  private def includeSecret(sql: String): Boolean = {
    sql.contains(SecretPrefix)
  }
}
