package controllers.login

import play.api.mvc.{Action, Controller}
import jp.t2v.lab.play20.auth.LoginLogout
import auth.AuthConfigImpl
import play.api.data._
import play.api.data.Forms._
import models.User

/**
 * The Class LoginController.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 2:31 PM
 *
 */
object LoginController extends Controller with LoginLogout with AuthConfigImpl {

  val loginForm = Form {
    "username" -> nonEmptyText
  }

  def login = Action {
    Ok(views.html.login())
  }

  def auth = Action(implicit request => {
    loginForm.bindFromRequest.fold(
      error => BadRequest,
      username => {
        createOrGetUser(username)
        gotoLoginSucceeded(username)
      }
    )
  })

  def logout = Action(implicit request => {
    gotoLogoutSucceeded
  })

  def createOrGetUser(username: String) = {
    val userInDb = User.findByUserName(username).getOrElse {
      val newUser = User(username = username, name = username, email = "")
      User.insert(newUser)
      newUser
    }
    userInDb
  }

}
