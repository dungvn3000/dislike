package controllers

import play.api.mvc.{Action, Controller}
import jp.t2v.lab.play20.auth.LoginLogout
import auth.AuthConfigImpl
import com.restfb.util.StringUtils
import play.api.libs.ws.WS
import util.matching.Regex
import com.restfb.DefaultFacebookClient
import concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.PlayException
import models.User

/**
 * The Class FacebookController.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 3:26 PM
 *
 */
object FacebookController extends Controller with LoginLogout with AuthConfigImpl {

  val app_id = "140099479491226"
  val app_secret = "7f5ca8df20e4002578cd17e2f4d997e9"
  val redirect_url = "http://vketnoi.com:9000/facebook/auth"

  def login = Action {
    val url = s"https://www.facebook.com/dialog/oauth?client_id=$app_id&redirect_uri=$redirect_url&scope=email"
    Redirect(url)
  }

  def auth(code: String) = Action(implicit request => {
    if (!StringUtils.isBlank(code)) {
      val accessTokenUrl = s"https://graph.facebook.com/oauth/access_token?client_id=$app_id&client_secret=$app_secret&code=$code&redirect_uri=$redirect_url"
      Async {
        WS.url(accessTokenUrl).get().map(response => {
          val regex = new Regex("access_token=(.*)&expires=(.*)")
          response.body match {
            case regex(accessToken, expires) => {
              val facebookClient = new DefaultFacebookClient(accessToken)
              val fbUser = facebookClient.fetchObject("me", classOf[com.restfb.types.User])
              val user = createOrGetUser(fbUser)
              gotoLoginSucceeded(user.username)
            }
            case _ => {
              throw new PlayException("Login error", "Some thing goes wrong please try again")
            }
          }
        })
      }
    } else {
      Redirect(routes.ApplicationController.index())
    }
  })

  private def createOrGetUser(user: com.restfb.types.User) = {
    val userInDb = User.findByUserName(user.getUsername).getOrElse {
      val newUser = User(username = user.getUsername, name = user.getName, email = user.getEmail)
      User.insert(newUser)
      newUser
    }
    userInDb
  }

}
