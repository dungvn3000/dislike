package models

import com.novus.salat.dao.{SalatDAO, ModelCompanion}
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import play.api.Play.current
import com.novus.salat.Context
import ModelContext._
import org.joda.time.DateTime

/**
 * The Class Dislike.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 5:58 PM
 *
 */
case class Dislike(
                    _id: ObjectId = new ObjectId(),
                    target: String,
                    comment: String,
                    userId: ObjectId,
                    created: DateTime = DateTime.now()
                    )

object Dislike extends ModelCompanion[Dislike, ObjectId] {
  def dao = new SalatDAO[Dislike, ObjectId](collection = mongoCollection("dislike")) {}
}