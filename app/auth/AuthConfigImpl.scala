package auth

import jp.t2v.lab.play20.auth.AuthConfig
import models.{User, Permission}
import play.api.mvc._
import play.api.mvc.Results._
import controllers.routes
import scala.reflect._

/**
 * The Class AuthConfigImpl.
 *
 * @author Nguyen Duc Dung
 * @since 11/6/12 10:10 AM
 *
 */
trait AuthConfigImpl extends AuthConfig {

  type Id = String

  type User = models.User

  type Authority = Permission

  def sessionTimeoutInSeconds = 3600

  def resolveUser(id: Id) = User.findByUserName(id)

  implicit val idTag: ClassTag[Id] = classTag[Id]

  def loginSucceeded(request: RequestHeader) = Redirect(routes.ApplicationController.index())

  def logoutSucceeded(request: RequestHeader) = Redirect(routes.ApplicationController.index())

  def authenticationFailed(request: RequestHeader) = Redirect(routes.LoginController.login())

  def authorizationFailed(request: RequestHeader) = Forbidden("bien di may")

  def authorize(user: User, authority: Authority) = User.login(user.username)

}