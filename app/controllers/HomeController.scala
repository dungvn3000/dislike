package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play20.auth.Auth
import auth.AuthConfigImpl
import models.NormalUser

/**
 * The Class HomeController.
 *
 * @author Nguyen Duc Dung
 * @since 2/27/13 12:44 PM
 *
 */
object HomeController extends Controller with Auth with AuthConfigImpl {

  def index =  authorizedAction(NormalUser)(implicit user => implicit request => {
    Ok(views.html.home_page())
  })

}
