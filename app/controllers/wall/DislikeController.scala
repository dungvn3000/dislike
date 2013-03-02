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
import java.io.{FileInputStream, ByteArrayOutputStream, ByteArrayInputStream}
import javax.imageio.ImageIO
import net.coobird.thumbnailator.Thumbnails
import org.linkerz.parser.ArticleParser
import org.jsoup.Jsoup
import org.apache.commons.validator.routines.UrlValidator
import org.apache.commons.lang.StringUtils
import edu.uci.ics.crawler4j.url.URLCanonicalizer
import gumi.builders.UrlBuilder
import org.apache.http.client.utils.URIUtils
import java.net.URI
import collection.mutable.ListBuffer
import java.awt.image.BufferedImage
import org.apache.commons.io.IOUtils

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
              fetcher.setMaxContentSize("image/jpeg", 1024 * 1024)
              val url = webUrlExtractor.results(0)
              val result = fetcher.fetch(url)
              val contentWithOutUrl = content.replaceAll(url, "")
              if (result.getStatusCode == HttpStatus.SC_OK && result.getContentLength > 0) {
                if (result.getContentType.contains("image/jpeg")) {
                  val bytes = result.getContent
                  val inputStream = new ByteArrayInputStream(bytes)
                  val image = ImageIO.read(inputStream)
                  val outputStream = new ByteArrayOutputStream
                  Thumbnails.of(image).size(400, 400).outputFormat("jpeg").toOutputStream(outputStream)
                  dislike = dislike.copy(image = Some(outputStream.toByteArray), url = Some(url), content = contentWithOutUrl)
                  outputStream.close()
                  inputStream.close()
                } else if (result.getContentType.contains("text/html")) {
                  val articleParser = new ArticleParser
                  val inputStream = new ByteArrayInputStream(result.getContent)
                  val doc = Jsoup.parse(inputStream, null, url)
                  val article = articleParser.parse(doc)
                  dislike = dislike.copy(title = Some(article.title), url = Some(url), content = contentWithOutUrl)
                  val description = article.description()
                  if (description.size > 0) {
                    dislike = dislike.copy(description = Some(description), content = contentWithOutUrl)
                  }
                  inputStream.close()

                  //find feature image
                  val potentialImages = new ListBuffer[String]
                  val urlValidator = new UrlValidator(Array("http", "https"))
                  article.images.foreach(image => {
                    val imgSrc = UrlBuilder.fromString(image.src).toString
                    val baseUrl = URIUtils.extractHost(new URI(url)).toURI
                    if (StringUtils.isNotBlank(imgSrc)) {
                      val imageUrl = URLCanonicalizer.getCanonicalURL(imgSrc, baseUrl)
                      if (StringUtils.isNotBlank(url) && urlValidator.isValid(url)) {
                        potentialImages += imageUrl
                      }
                    }
                  })

                  val scoreImage = new ListBuffer[(BufferedImage, Double)]

                  var skip = false
                  potentialImages.foreach(imageUrl => if (!skip) {
                    try {
                      val result = fetcher.get(imageUrl)
                      if (result.getStatusCode == HttpStatus.SC_OK && result.getContentLength > 0) {
                        try {
                          val bytes = result.getContent
                          val inputStream = new ByteArrayInputStream(bytes)
                          val image = ImageIO.read(inputStream)
                          val score = image.getWidth + image.getHeight
                          if (score >= 200) {
                            scoreImage += image -> score
                          }
                          inputStream.close()

                          //Avoid download too much images, if the image score is 600, definitely it is good.
                          if (score >= 400) {
                            skip = true
                          }

                        } catch {
                          case ex: Exception => {
                            Logger.error(ex.getMessage, ex)
                          }
                        }
                      }
                    } catch {
                      case ex: Exception => {
                        Logger.error(ex.getMessage, ex)
                      }
                    }
                  })

                  if (!scoreImage.isEmpty) {
                    val bestImage = scoreImage.sortBy(-_._2).head._1
                    val outputStream = new ByteArrayOutputStream
                    try {
                      Thumbnails.of(bestImage).size(150, 150).outputFormat("jpeg").toOutputStream(outputStream)
                      dislike = dislike.copy(featureImage = Some(outputStream.toByteArray))
                    } catch {
                      case ex: Exception => {
                        Logger.error(ex.getMessage, ex)
                      }
                    } finally {
                      outputStream.close()
                    }
                  }
                }
              }
            } catch {
              case ex: Exception => Logger.error(ex.getMessage, ex)
            }
          }

          Dislike.insert(dislike)
          Redirect(controllers.routes.HomeController.index())
        }
      )
    }
    Async {
      futureInt.map(i => i)
    }
  })

  def postImage = authorizedAction(NormalUser)(implicit user => implicit request => {
    request.body.asMultipartFormData.map(data => {
      data.file("image").map(image => {
        val input = new FileInputStream(image.ref.file)
        val bytes = IOUtils.toByteArray(input)
        if (bytes.size > 0) {
          val dislike = Dislike(
            content = "",
            userId = user._id,
            image = Some(bytes)
          )
          Dislike.insert(dislike)
        }
      })
    })
    Redirect(controllers.routes.HomeController.index())
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

  def featureImage(id: ObjectId) = authorizedAction(NormalUser)(implicit user => implicit request => {
    val dislike = Dislike.findOneById(id).getOrElse(throw new Exception("Can't find id " + id))
    dislike.featureImage.map(bytes => {
      SimpleResult(
        header = ResponseHeader(200, Map(CONTENT_TYPE -> "image/jpeg")),
        body = Enumerator.fromStream(new ByteArrayInputStream(bytes))
      )
    }).getOrElse(Redirect("/assets/img/tool/beat-brick-icon.png"))
  })
}
