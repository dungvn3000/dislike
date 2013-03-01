package controllers.wall

import play.api.mvc.{ResponseHeader, SimpleResult, Controller}
import models.{NormalUser, Dislike}
import auth.AuthConfigImpl
import jp.t2v.lab.play20.auth.Auth
import play.api.data.Form
import play.api.data.Forms._
import com.github.sonic.parser.extractor.DefaultExtractor
import com.github.sonic.parser.extractor.rule.WebUrlRule
import crawlercommons.fetcher.http.SimpleHttpFetcher
import downloader.FireFoxUserAgent
import org.apache.http.HttpStatus
import concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.Logger
import org.bson.types.ObjectId
import play.api.libs.iteratee.Enumerator
import java.io.{ByteArrayOutputStream, ByteArrayInputStream}
import javax.imageio.ImageIO
import net.coobird.thumbnailator.Thumbnails
import org.linkerz.parser.ArticleParser
import org.jsoup.Jsoup

/**
 * The Class DislikeController.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 11:23 PM
 *
 */
object DislikeController extends Controller with Auth with AuthConfigImpl {

  def post = authorizedAction(NormalUser)(implicit user => implicit request => {
    val futureInt = scala.concurrent.Future {
      Form("content" -> nonEmptyText).bindFromRequest.fold(
        errors => BadRequest,
        content => {

          var dislike = Dislike(
            userId = user._id,
            content = content
          )

          val webUrlExtractor = new DefaultExtractor(new WebUrlRule)
          webUrlExtractor.extract(content)

          if (webUrlExtractor.results.size > 0) {
            try {
              val fetcher = new SimpleHttpFetcher(100, new FireFoxUserAgent)
              val url = webUrlExtractor.results(0)
              val result = fetcher.fetch(url)
              if (result.getStatusCode == HttpStatus.SC_OK && result.getContentLength > 0) {
                if (result.getContentType.contains("image/jpeg")) {
                  val bytes = result.getContent
                  val inputStream = new ByteArrayInputStream(bytes)
                  val image = ImageIO.read(inputStream)
                  val outputStream = new ByteArrayOutputStream
                  Thumbnails.of(image).size(400, 400).outputFormat("jpeg").toOutputStream(outputStream)
                  dislike = dislike.copy(image = Some(outputStream.toByteArray) , url = Some(url))
                  outputStream.close()
                  inputStream.close()
                } else if (result.getContentType.contains("text/html")) {
                  val articleParser = new ArticleParser
                  val inputStream = new ByteArrayInputStream(result.getContent)
                  val doc = Jsoup.parse(inputStream, null, url)
                  val article = articleParser.parse(doc)
                  dislike = dislike.copy(title = Some(article.title), url = Some(url))
                  val description = article.description()
                  if (description.size > 0) {
                    dislike = dislike.copy(description = Some(description))
                  }
                  inputStream.close()
                }
              }
            } catch {
              case ex: Exception => Logger.error(ex.getMessage, ex)
            }
          }

          Dislike.insert(dislike)
          Redirect(controllers.routes.WallController.index())
        }
      )
    }
    Async {
      futureInt.map(i => i)
    }
  })

  def image(id: ObjectId) = authorizedAction(NormalUser)(implicit user => implicit request => {
    val dislike = Dislike.findOneById(id).getOrElse(throw new Exception("Can't find id " + id))
    dislike.image.map(bytes => {
      SimpleResult(
        header = ResponseHeader(200, Map(CONTENT_TYPE -> "image/jpeg")),
        body = Enumerator.fromStream(new ByteArrayInputStream(bytes))
      )
    }).getOrElse(Redirect("/assets/img/tool/beat-brick-icon.png"))
  })

}
