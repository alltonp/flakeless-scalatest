import scala.util.Try

import xerial.sbt.Sonatype._


name := "flakeless-scalatest"

organization := "im.mange"

version := Try(sys.env("TRAVIS_BUILD_NUMBER")).map("0.0." + _).getOrElse("1.0-SNAPSHOT")

scalaVersion:= "2.12.4"

unmanagedSourceDirectories in Compile += baseDirectory.value / "src" / "example" / "scala"

resolvers ++= Seq(
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "im.mange"      %% "flakeless" % "[0.0.186,0.0.999]" % "provided",

  //TODO: should be to 3.0.99, but appears they have a load of mangey snapshots
  //... would be nice to fix thought still
  "org.scalatest" %% "scalatest" % "[3.0.4,3.0.5]" % "provided"

  //TIP: only enable these for local testing ...
//  ,
//  "com.codeborne" % "phantomjsdriver" % "[1.3.0,1.99.9]" % "provided",
//  "org.seleniumhq.selenium" % "selenium-java" % "[2.53.1,3.99.9]" % "provided",
)

sonatypeSettings

publishTo <<= version { project_version ⇒
  val nexus = "https://oss.sonatype.org/"
  if (project_version.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

homepage := Some(url("https://github.com/alltonp/flakeless-scalatest"))

licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", System.getenv("SONATYPE_USER"), System.getenv("SONATYPE_PASSWORD"))

pomExtra :=
    <scm>
      <url>git@github.com:alltonp/flakeless-scalatest.git</url>
      <connection>scm:git:git@github.com:alltonp/flakeless-scalatest.git</connection>
    </scm>
    <developers>
      <developer>
        <id>alltonp</id>
      </developer>
    </developers>
