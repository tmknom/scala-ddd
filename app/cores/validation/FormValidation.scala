package cores.validation

import cores.internal.exception.ValidationException
import play.api.data.Form
import play.api.mvc.Request

/**
  * 入力フォームのバリデーション
  */
object FormValidation {
  /**
    * 入力バリデーションを行い、フォームオブジェクトからドメインオブジェクトへマッピングする
    *
    * バリデーションエラーの場合は例外をスローする
    *
    * @param form    フォームオブジェクト
    * @param request リクエスト
    * @tparam A フォームオブジェクトからマッピングするクラス
    * @return
    */
  def validate[A](form: Form[A])(implicit request: Request[_]): A = {
    form.bindFromRequest.fold(
      formWithErrors => {
        throw new ValidationException(formWithErrors.errors)
      },
      validObject => validObject
    )
  }
}
