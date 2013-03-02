package models

import org.bson.types.ObjectId

/**
 * The Class Notification.
 *
 * @author Nguyen Duc Dung
 * @since 3/3/13 2:42 AM
 *
 */
case class Notification(
                         _id: ObjectId,
                         message: String,
                         fromUserId: ObjectId,
                         toUserId: ObjectId) extends BaseModel(_id)
