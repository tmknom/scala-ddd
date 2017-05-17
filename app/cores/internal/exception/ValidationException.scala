package cores.internal.exception

import play.api.data.FormError

/**
  * バリデーション例外
  */
final class ValidationException(val errors: Seq[FormError]) extends RuntimeException
