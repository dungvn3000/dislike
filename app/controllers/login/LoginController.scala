package controllers.login

import play.api.mvc.{Action, Controller}

/**
 * The Class LoginController.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 2:31 PM
 *
 */
object LoginController extends Controller {

  def login = Action {
    Ok(views.html.login())
  }

}
