package controllers

import play.api.mvc._
import auth.AuthConfigImpl
import jp.t2v.lab.play20.auth.Auth
import models.{Dislike, NormalUser}

object WallController extends Controller with Auth with AuthConfigImpl {

  def index = authorizedAction(NormalUser)(implicit user => implicit request => {
    val result = Dislike.getUserDislikeAndComment(user._id)
    Ok(views.html.wall(result._1, result._2))
  })


}