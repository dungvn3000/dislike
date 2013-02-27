package controllers

import play.api.mvc._
import auth.AuthConfigImpl
import jp.t2v.lab.play20.auth.Auth
import models.{Dislike, NormalUser}

object WallController extends Controller with Auth with AuthConfigImpl {

  def index = authorizedAction(NormalUser)(implicit user => implicit request => {
    val dislikes = Dislike.getUserDislike(user._id)
    Ok(views.html.index(dislikes))
  })


}