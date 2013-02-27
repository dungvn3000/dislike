package controllers.login

import play.api.mvc.{Action, Controller}
import jp.t2v.lab.play20.auth.LoginLogout
import auth.AuthConfigImpl

/**
 * The Class LoginController.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 2:31 PM
 *
 */
object LoginController extends Controller with LoginLogout with AuthConfigImpl {

  def login = Action {
    Ok(views.html.login())
  }

  def logout = Action(implicit request => {
    gotoLogoutSucceeded
  })

}
