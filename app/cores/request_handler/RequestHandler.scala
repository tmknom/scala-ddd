package cores.request_handler

import javax.inject.{Inject, Singleton}

import cores.request_handler.internal.RequestInitializer
import play.api.http.{DefaultHttpRequestHandler, HttpConfiguration, HttpErrorHandler, HttpFilters}
import play.api.mvc.{Handler, RequestHeader}
import play.api.routing.Router

/**
  * リクエストハンドラー
  *
  * HTTPリクエストされると呼び出される。
  * RequestHeader オブジェクトにタグをセットするために、本リクエストハンドラーを実装している。
  *
  * ドキュメントにも記載がある通り、リクエスト時の共通処理は、
  * 可能な限り Router や Filter で行うべきであり、本クラスを拡張するのは最終手段とすること。
  *
  * 本クラスで実現している処理は当初 Filter で実装を試みたが、
  * RequestHeader オブジェクトへのタグのセットができなかったため、やむなく独自のリクエストハンドラーを定義した。
  * もしかすると Filter の実装方法が間違っていただけかもしれず、その場合は、本クラスの処理は Filter へ移動可能である。
  *
  * 本リクエストハンドラーを使用するようアプリケーションに組み込むには、明示的に設定ファイルへの記述が必要。
  * 多くの場合 conf/application.conf ファイルに記述することになる。
  * 設定箇所は play.http の requestHandler の項目である。
  *
  * @see https://www.playframework.com/documentation/2.5.x/ScalaHttpRequestHandlers#Extending-the-default-request-handler
  * @param router        A router.
  * @param errorHandler  Component for handling HTTP errors in Play
  * @param configuration HTTP related configuration of a Play application
  * @param filters       Provides filters to the [[play.api.http.HttpRequestHandler]]
  */
@Singleton
final class RequestHandler @Inject()(router: Router,
                                     errorHandler: HttpErrorHandler,
                                     configuration: HttpConfiguration,
                                     filters: HttpFilters
                                    ) extends DefaultHttpRequestHandler(router, errorHandler, configuration, filters) {

  /**
    * リクエストの処理を開始する前に、リクエストヘッダーにタグをセットする
    *
    * @param request The request to handle
    * @return The possibly modified/tagged request, and a handler to handle it
    */
  override def handlerForRequest(request: RequestHeader): (RequestHeader, Handler) = {
    val taggedRequest: RequestHeader = RequestInitializer.initialize(request)
    super.handlerForRequest(taggedRequest)
  }
}
