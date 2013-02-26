package controllers

import play.api.mvc.{Action, Controller}
import com.restfb.util.StringUtils
import play.api.libs.ws.WS
import util.matching.Regex
import com.restfb.DefaultFacebookClient
import concurrent.ExecutionContext
import ExecutionContext.Implicits.global

/**
 * The Class LoginController.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 2:31 PM
 *
 */
object LoginController extends Controller {

  val app_id = "140099479491226"
  val app_secret = "7f5ca8df20e4002578cd17e2f4d997e9"
  val redirect_url = "http://vketnoi.com:9000/login"

  def login = Action {
    val url = s"https://www.facebook.com/dialog/oauth?client_id=$app_id&redirect_uri=$redirect_url&scope=email"
    Ok(url)
  }

  def auth(code: String) = Action {
    if (!StringUtils.isBlank(code)) {
      val accessTokenUrl = s"https://graph.facebook.com/oauth/access_token?client_id=$app_id&client_secret=$app_secret&code=$code&redirect_uri=$redirect_url"
      Async {
        WS.url(accessTokenUrl).get().map(response => {
          val regex = new Regex("access_token=(.*)&expires=(.*)")
          response.body match {
            case regex(accessToken, expires) => {
              val facebookClient = new DefaultFacebookClient(accessToken)
              val fbUser = facebookClient.fetchObject("me", classOf[com.restfb.types.User])
              println(fbUser)
              Ok(fbUser.getBio)
            }
            case _ => {
              Ok("no match")
            }
          }
        })
      }
    } else {
      Redirect(routes.ApplicationController.index())
    }
  }

}
