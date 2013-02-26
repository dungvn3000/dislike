package models

import com.novus.salat.Context

/**
 * The Class ModelContext.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 4:48 PM
 *
 */
object ModelContext {

  implicit val ctx = new Context {
    val name = "global"
  }

}
