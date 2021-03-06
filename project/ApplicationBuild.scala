import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "dislike"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "se.radley" %% "play-plugins-salat" % "1.2",
    "joda-time" % "joda-time" % "2.1",
    "com.github.seratch" %% "inputvalidator" % "[0.2,)",
    "com.github.seratch" %% "inputvalidator-play" % "[0.2,)",
    "jp.t2v" %% "play21.auth" % "0.7",
    "com.restfb" % "restfb" % "1.6.11",
    "com.github.sonic" %% "sonic_extractor" % "0.0.1",
    "com.google.code.crawler-commons" % "crawler-commons" % "0.2",
    "net.coobird" % "thumbnailator" % "0.4.3",
    "org.linkerz" %% "linkerz_parser" % "0.1-SNAPSHOT",
    "org.linkerz" %% "url_builder" % "0.1-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    requireJsFolder += "js",
    requireJs += "wall/comment",
    routesImport += "se.radley.plugin.salat.Binders._",
    templatesImport ++= Seq(
      "org.bson.types.ObjectId"
    ),
    resolvers ++= Seq(
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Spray repo" at "http://repo.spray.io",
      "t2v.jp repo" at "http://www.t2v.jp/maven-repo/",
      "sonatype-oss-public" at "https://oss.sonatype.org/content/groups/public/",
      "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
      Resolver.file("Local Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)
    )
  )

}
