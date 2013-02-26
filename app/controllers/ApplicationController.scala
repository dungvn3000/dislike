package controllers

import play.api.mvc._
import auth.AuthConfigImpl
import jp.t2v.lab.play20.auth.Auth
import models.NormalUser

object ApplicationController extends Controller with Auth with AuthConfigImpl {

  def index = authorizedAction(NormalUser)(implicit user => implicit request => {
    Ok(views.html.index())
  })


}