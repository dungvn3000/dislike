package controllers

import play.api.libs.ws.WS
import play.api.mvc._
import com.restfb.DefaultFacebookClient
import com.restfb.util.StringUtils
import util.matching.Regex
import concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Application extends Controller {

  val app_id = "140099479491226"
  val app_secret = "7f5ca8df20e4002578cd17e2f4d997e9"
  val redirect_url = "http://vketnoi.com:9000/login"

  def index = Action {
    val url = s"https://www.facebook.com/dialog/oauth?client_id=$app_id&redirect_uri=$redirect_url&scope=email"
    Ok(url)
  }

  def login(code: String) = Action {
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
      Redirect(routes.Application.index())
    }
  }

}