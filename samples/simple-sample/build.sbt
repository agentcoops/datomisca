name := "datomisca-simple-sample"

organization := "pellucidanalytics"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.0"

fork in test := true

resolvers ++= Seq(
  "datomisca-repo snapshots" at "https://github.com/pellucidanalytics/datomisca-repo/raw/master/snapshots",
  "datomisca-repo releases" at "https://github.com/pellucidanalytics/datomisca-repo/raw/master/releases",
  "clojars" at "https://clojars.org/repo"
)

libraryDependencies ++= Seq(
  "pellucidanalytics" %% "datomisca" % "0.3-SNAPSHOT",
  "com.datomic" % "datomic-pro" % "0.8.3814"
)
