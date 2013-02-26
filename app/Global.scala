import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.typesafe.config.ConfigFactory
import java.io.File
import play.api.cache.Cache
import play.api.mvc.RequestHeader
import play.api._
import play.api.Play.current

/**
 * The Class Global.
 *
 * @author Nguyen Duc Dung
 * @since 11/2/12 12:23 AM
 *
 */
object Global extends GlobalSettings {

  val devConfFilePath = "conf/dev.conf"
  val prodConfFilePath = "prod.conf"

  override def onStart(app: Application) {
    Logger.info("Starting Application")
    RegisterJodaTimeConversionHelpers()
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

  override def onLoadConfig(config: Configuration, path: File, classLoader: ClassLoader, mode: Mode.Mode) = if (mode == Mode.Prod) {
    val prodConfig = ConfigFactory.parseResources(classLoader, prodConfFilePath)
    config ++ Configuration(prodConfig)
  } else {
    val devConfig = ConfigFactory.parseFileAnySyntax(new File(path, devConfFilePath))
    config ++ Configuration(devConfig)
  }

}
