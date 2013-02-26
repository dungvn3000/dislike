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
    "com.github.seratch" %% "inputvalidator-play" % "[0.2,)"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    requireJsFolder += "js",
    routesImport += "se.radley.plugin.salat.Binders._",
    templatesImport ++= Seq(
      "org.bson.types.ObjectId"
    ),
    resolvers ++= Seq(
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Spray repo" at "http://repo.spray.io",
      "t2v.jp repo" at "http://www.t2v.jp/maven-repo/",
      "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
      Resolver.file("Local Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)
    )
  )

}
