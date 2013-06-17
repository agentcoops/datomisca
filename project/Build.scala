import sbt._
import Keys._

object BuildSettings {
  val buildName              = "datomisca"
  val buildOrganization      = "pellucidanalytics"
  val buildVersion           = "0.3-SNAPSHOT"
  val buildScalaVersion      = "2.10.2"

  val datomicVersion         = "0.8.3993"
 
  val typesafeRepo = Seq(
    "Typesafe repository snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
    "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"
  )

  val datomicRepo = Seq(
    "clojars" at "https://clojars.org/repo",
    "couchbase" at "http://files.couchbase.com/maven2"
  )

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization    := buildOrganization,
    version         := buildVersion,
    scalaVersion    := buildScalaVersion,
    resolvers ++= typesafeRepo ++ datomicRepo ++ List(Resolver.sonatypeRepo("snapshots")),
    libraryDependencies ++= Seq(
      "com.datomic" % "datomic-free" % BuildSettings.datomicVersion % "provided" exclude("org.slf4j", "slf4j-nop"),        
      "org.scala-lang" % "scala-compiler" % "2.10.2",
      "org.specs2" %% "specs2" % "1.13" % "test",
      "junit" % "junit" % "4.8" % "test"
    )
  )
}

object ApplicationBuild extends Build {

  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("core"),
    settings = buildSettings
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      // NOTE: macros are compiled with macro paradise 2.10
      scalaVersion := "2.10.2-SNAPSHOT",
      scalaOrganization := "org.scala-lang.macro-paradise",
      libraryDependencies <+= (scalaVersion)("org.scala-lang.macro-paradise" % "scala-reflect" % _)
    )
  )

  lazy val core = Project(
    "core", 
    file("core"),
    settings = BuildSettings.buildSettings ++ Seq(
      //logLevel := Level.Debug,
      //ivyLoggingLevel := UpdateLogging.Full,
      scalacOptions ++= Seq(
        //"-Xlog-implicits",
        //"-deprecation",
        //"-feature"
      ),
      fork in Test := true,
      //parallelExecution in Test := false,
      publishMavenStyle := true,
      publishTo <<= version { (version: String) =>
        val localPublishRepo = "../datomisca-repo/"
        if(version.trim.endsWith("SNAPSHOT"))
          Some(Resolver.file("snapshots", new File(localPublishRepo + "/snapshots")))
        else Some(Resolver.file("releases", new File(localPublishRepo + "/releases")))
      }
    )
  ) dependsOn(macros)
}
