package controllers.wall

import play.api.mvc.Controller
import models.{Comment, NormalUser}
import jp.t2v.lab.play20.auth.Auth
import auth.AuthConfigImpl
import play.api.data.Form
import play.api.data.Forms._
import org.bson.types.ObjectId

/**
 * The Class CommentController.
 *
 * @author Nguyen Duc Dung
 * @since 2/27/13, 11:21 PM
 *
 */
object CommentController extends Controller with Auth with AuthConfigImpl {

  def post = authorizedAction(NormalUser)(implicit user => implicit request => {
    Form(tuple("content" -> nonEmptyText, "dislikeId" -> nonEmptyText)).bindFromRequest.fold(
      errors => BadRequest,
      tuple => {
        Comment.insert(Comment(
          userId = user._id,
          content = tuple._1,
          dislikeId = new ObjectId(tuple._2)
        ))
        Ok
      }
    )
  })

}
