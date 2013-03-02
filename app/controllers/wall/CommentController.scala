package controllers.wall

import play.api.mvc.Controller
import models._
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

        val content = tuple._1
        val dislikeId = new ObjectId(tuple._2)

        val dislike = Dislike.findOneById(dislikeId).getOrElse(throw new Exception("can't find the dislike id " + dislikeId))
        val comment = Comment(
          userId = user._id,
          content = content,
          dislikeId = dislikeId
        )
        Comment.insert(comment)

        if (user._id != dislike.userId) {
          val notification = new Notification(
            message = "vừa bình luận trên bài viết của bạn",
            fromUserId = user._id,
            toUserId = dislike.userId,
            dislikeId = dislikeId
          )
          Notification.insert(notification)
        }

        Ok(comment.id)
      }
    )
  })

  def delete(id: ObjectId) = authorizedAction(NormalUser)(implicit user => implicit request => {
    Comment.removeById(id)
    Ok
  })

}
