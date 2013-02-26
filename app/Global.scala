import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import play.api.cache.Cache
import play.api.mvc.RequestHeader
import play.api.{Logger, Application, GlobalSettings}
import play.api.Play.current

/**
 * The Class Global.
 *
 * @author Nguyen Duc Dung
 * @since 11/2/12 12:23 AM
 *
 */
object Global extends GlobalSettings {
  override def onStart(app: Application) {
    Logger.info("Starting Application")
    RegisterJodaTimeConversionHelpers()
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}
