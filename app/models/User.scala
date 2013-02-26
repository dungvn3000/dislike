package models

import org.bson.types.ObjectId
import com.novus.salat.dao.{SalatDAO, ModelCompanion}
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import play.api.Play.current
import com.novus.salat.global._

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
                 name: String,
                 email: String
                 )

object User extends ModelCompanion[User, ObjectId] {
  def dao = new SalatDAO[User, ObjectId](collection = mongoCollection("user")) {}

  def findByUserName(userName: String) = findOne(MongoDBObject("userName" -> userName))

  def login(userName: String): Boolean = {
    val user = findByUserName(userName)
    if (user.isDefined) {
      return true
    }
    false
  }

}