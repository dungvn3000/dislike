package models

import com.novus.salat.dao.{SalatDAO, ModelCompanion}
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import play.api.Play.current
import com.novus.salat.Context
import ModelContext._

/**
 * The Class User.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 2:29 PM
 *
 */
case class User(
                 _id: ObjectId = new ObjectId(),
                 username: String,
                 avatar: Option[Array[Byte]] = None,
                 name: String,
                 email: String
                 )

object User extends ModelCompanion[User, ObjectId] {

  def dao = new SalatDAO[User, ObjectId](collection = mongoCollection("user")) {}

  def findByUserName(username: String) = findOne(MongoDBObject("username" -> username))

  def login(username: String): Boolean = findByUserName(username).isDefined

}