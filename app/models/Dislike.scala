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
 * The Class Dislike.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 5:58 PM
 *
 */
case class Dislike(
                    _id: ObjectId = new ObjectId(),
                    content: String,
                    userId: ObjectId,
                    created: DateTime = DateTime.now()
                    ) extends BaseModel(_id)

object Dislike extends ModelCompanion[Dislike, ObjectId] {
  def dao = new SalatDAO[Dislike, ObjectId](collection = mongoCollection("dislike")) {}

  def getUserDislikeAndComment = {
    val dislikes = find(MongoDBObject.empty).sort(MongoDBObject("created" -> -1)).toList
    val comments = new ListBuffer[Comment]
    dislikes.foreach(dislike => {
      comments ++= Comment.find(MongoDBObject("dislikeId" -> dislike._id)).sort(MongoDBObject("created" -> -1)).toList
    })
    (dislikes, comments.toList)
  }

  def getUserDislikeAndComment(userId: ObjectId) = {
    val dislikes = find(MongoDBObject("userId" -> userId)).sort(MongoDBObject("created" -> -1)).toList
    val comments = new ListBuffer[Comment]
    dislikes.foreach(dislike => {
      comments ++= Comment.find(MongoDBObject("dislikeId" -> dislike._id)).sort(MongoDBObject("created" -> -1)).toList
    })
    (dislikes, comments.toList)
  }
}