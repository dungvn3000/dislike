package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play20.auth.Auth
import auth.AuthConfigImpl
import models.{User, Dislike, NormalUser}

/**
 * The Class HomeController.
 *
 * @author Nguyen Duc Dung
 * @since 2/27/13 12:44 PM
 *
 */
object HomeController extends Controller with Auth with AuthConfigImpl {

  def index =  authorizedAction(NormalUser)(implicit user => implicit request => {
    val result = Dislike.getUserDislikeAndComment
    val users = User.findAll().toList
    Ok(views.html.home_page(result._1, result._2, users))
  })

}
