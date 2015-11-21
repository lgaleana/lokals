name         := """lokals"""
version      := "1.0"
scalaVersion := "2.11.7"

val dbDependencies = Seq(
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.typesafe.play" %% "play-slick" % "1.1.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.0",
  "com.zaxxer" % "HikariCP" % "2.3.5" % Compile
)

/** Frontend assets **/
val jsWebJars = Seq(
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "jquery" % "2.1.4",
  "org.webjars" % "jquery-ui" % "1.11.4",
  "org.webjars" % "react" % "0.13.3"
)

val cssWebJars = Seq(
  "org.webjars" % "font-awesome" % "4.3.0-2",
  "org.webjars" % "bootstrap" % "3.3.5"
)
/****/

val webJarAssetDependencies = cssWebJars ++ jsWebJars

val testDependencies = Seq(
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "org.mockito" % "mockito-all" % "1.9.0" % "test"
)

val consoleCommands =
  """
    | import scala.concurrent.duration.DurationInt
    | import scala.concurrent.{ Await, Future }
    | import play.api.libs.json._
    | import play.api.{ Environment, ApplicationLoader, Play, Mode }
    | val env = Environment(new java.io.File("."), this.getClass.getClassLoader, Mode.Dev)
    | val context = ApplicationLoader.createContext(env)
    | val loader = ApplicationLoader(context)
    | val app = loader.load(context)
    | Play.start(app)
    | import Play.current
    | implicit val _ = scala.concurrent.ExecutionContext.global
  """.stripMargin

libraryDependencies ++= Seq(
  cache,
  ws
) ++ dbDependencies ++ webJarAssetDependencies ++ testDependencies

initialCommands in console := consoleCommands

routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).enablePlugins(PlayScala)
