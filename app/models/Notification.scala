package models

import com.novus.salat.dao.{SalatDAO, ModelCompanion}
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import play.api.Play.current
import com.novus.salat.Context
import ModelContext._
import org.joda.time.DateTime
import collection.mutable.ListBuffer

/**
 * The Class Notification.
 *
 * @author Nguyen Duc Dung
 * @since 3/3/13 2:42 AM
 *
 */
case class Notification(
                         _id: ObjectId = new ObjectId,
                         message: String,
                         fromUserId: ObjectId,
                         toUserId: ObjectId,
                         create: DateTime = DateTime.now()) extends BaseModel(_id)

object Notification extends ModelCompanion[Notification, ObjectId] {

  def dao = new SalatDAO[Notification, ObjectId](collection = mongoCollection("notification")) {}

  def findByUserId(userId: ObjectId) = Notification.find(MongoDBObject("toUserId" -> userId)).toList

}