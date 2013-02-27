package controllers.wall

import play.api.mvc.Controller
import models.{NormalUser, Dislike}
import auth.AuthConfigImpl
import jp.t2v.lab.play20.auth.Auth
import play.api.data.Form
import play.api.data.Forms._
/**
 * The Class DislikeController.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 11:23 PM
 *
 */
object DislikeController extends Controller with Auth with AuthConfigImpl {

  def post = authorizedAction(NormalUser)(implicit user => implicit request => {
    Form("content" -> nonEmptyText).bindFromRequest.fold(
      errors => BadRequest,
      content => {
        Dislike.insert(Dislike(
          userId = user._id,
          content = content
        ))
        Redirect(controllers.routes.WallController.index())
      }
    )
  })

}
