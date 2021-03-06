package controllers

import jp.t2v.lab.play20.auth.Auth
import auth.AuthConfigImpl
import play.api.mvc.{ResponseHeader, SimpleResult, Controller}
import models.{Notification, User, NormalUser}
import java.io.{ByteArrayInputStream, FileInputStream}
import org.apache.commons.io.IOUtils
import play.api.data._
import play.api.data.Forms._
import play.api.libs.iteratee.Enumerator

/**
 * The Class ProfileController.
 *
 * @author Nguyen Duc Dung
 * @since 2/28/13, 12:14 PM
 *
 */
object ProfileController extends Controller with Auth with AuthConfigImpl {

  def index = authorizedAction(NormalUser)(implicit user => implicit request => {
    implicit val notifications = Notification.findByUserId(user._id)
    val users = User.findAll().toList
    Ok(views.html.profile_page(users))
  })

  def update = authorizedAction(NormalUser)(implicit user => implicit request => {
    var updateUser = user
    Form {
      tuple("name" -> nonEmptyText, "quote" -> nonEmptyText)
    }.bindFromRequest.fold(
      error => BadRequest,
      data => updateUser = user.copy(name = data._1, quote = data._2)
    )

    request.body.asMultipartFormData.map(data => {
      data.file("avatar").map(avatar => {
        val input = new FileInputStream(avatar.ref.file)
        val bytes = IOUtils.toByteArray(input)
        if (bytes.size > 0) {
          updateUser = updateUser.copy(avatar = Some(bytes))
        }
      })
    })

    User.save(updateUser)
    implicit val notifications = Notification.findByUserId(user._id)
    val users = User.findAll().toList
    Ok(views.html.profile_page(users)(updateUser, notifications))
  })

  def avatar = authorizedAction(NormalUser)(implicit user => implicit request => {
    user.avatar.map(bytes => {
      SimpleResult(
        header = ResponseHeader(200, Map(CONTENT_TYPE -> "image/jpeg")),
        body = Enumerator.fromStream(new ByteArrayInputStream(bytes))
      )
    }).getOrElse(Redirect("/assets/img/noavatar.gif"))
  })

  def avatarOf(username: String) = authorizedAction(NormalUser)(implicit user => implicit request => {
    User.findByUserName(username).map(otherUser => {
      otherUser.avatar.map(bytes => {
        SimpleResult(
          header = ResponseHeader(200, Map(CONTENT_TYPE -> "image/jpeg")),
          body = Enumerator.fromStream(new ByteArrayInputStream(bytes))
        )
      }).getOrElse(Redirect("/assets/img/noavatar.gif"))
    }).getOrElse(BadRequest)
  })

}
