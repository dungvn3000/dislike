package controllers

import jp.t2v.lab.play20.auth.Auth
import auth.AuthConfigImpl
import play.api.mvc.Controller
import models.NormalUser

/**
 * The Class ProfileController.
 *
 * @author Nguyen Duc Dung
 * @since 2/28/13, 12:14 PM
 *
 */
object ProfileController extends Controller with Auth with AuthConfigImpl {
  def index = authorizedAction(NormalUser)(implicit user => implicit request => {
    Ok(views.html.profile_page())
  })
}
