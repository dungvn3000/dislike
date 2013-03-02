package controllers

import play.api.mvc._
import auth.AuthConfigImpl
import jp.t2v.lab.play20.auth.Auth
import models.{Notification, User, Dislike, NormalUser}
import org.bson.types.ObjectId

object WallController extends Controller with Auth with AuthConfigImpl {

  def index = authorizedAction(NormalUser)(implicit user => implicit request => {
    val result = Dislike.getUserDislikeAndComment(user._id)
    val users = User.findAll().toList
    implicit val notifications = Notification.findByUserId(user._id)
    Ok(views.html.wall(result._1, result._2, users))
  })

  def wall(username: String) = authorizedAction(NormalUser)(implicit user => implicit request => {
    User.findByUserName(username).map(otherUser => {
      val result = Dislike.getUserDislikeAndComment(otherUser._id)
      val users = User.findAll().toList
      implicit val notifications = Notification.findByUserId(user._id)
      Ok(views.html.wall(result._1, result._2, users, Some(otherUser)))
    }).getOrElse(BadRequest)
  })

  def view(id: ObjectId) = authorizedAction(NormalUser)(implicit user => implicit request => {
    val result = Dislike.findUserDislikeAndComment(id)
    val users = User.findAll().toList
    implicit val notifications = Notification.findByUserId(user._id)
    Ok(views.html.wall(result._1, result._2, users))
  })
}