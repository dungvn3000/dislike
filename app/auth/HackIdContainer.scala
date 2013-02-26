package auth

import jp.t2v.lab.play20.auth._

/**
 * The Class only using in development mode.
 *
 * @author Nguyen Duc Dung
 * @since 2/20/13, 2:01 PM
 *
 */
class HackIdContainer extends IdContainer[String] {
  def startNewSession(userId: String, timeoutInSeconds: Int) = ""

  def remove(token: AuthenticityToken) {}

  //Dummy user.
  def get(token: AuthenticityToken) = Some("dungvn3000")

  def prolongTimeout(token: AuthenticityToken, timeoutInSeconds: Int) {}
}
