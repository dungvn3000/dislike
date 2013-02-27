package models

import org.bson.types.ObjectId
import org.joda.time.DateTime
import com.novus.salat.dao.{SalatDAO, ModelCompanion}
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import play.api.Play.current
import ModelContext._

/**
 * The Class Comment.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 6:01 PM
 *
 */
case class Comment(
                    _id: ObjectId = new ObjectId(),
                    dislikeId: ObjectId,
                    userId: ObjectId,
                    content: String,
                    created: DateTime = DateTime.now()
                    )

object Comment extends ModelCompanion[Comment, ObjectId] {
  def dao = new SalatDAO[Comment, ObjectId](collection = mongoCollection("comment")) {}
}