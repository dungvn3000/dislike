package models

import org.bson.types.ObjectId

/**
 * The Class BaseModel.
 *
 * @author Nguyen Duc Dung
 * @since 2/28/13, 1:37 AM
 * 
 */
abstract class BaseModel(_id: ObjectId) {

  def id = _id.toString

}
