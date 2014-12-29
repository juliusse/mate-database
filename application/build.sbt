import de.johoop.jacoco4sbt.JacocoPlugin._
import com.typesafe.config._
import aether.Aether._
import play.Play.autoImport._
import PlayKeys._

name := "mate-database"

organization := "info.seltenheim"

organizationName  := "Julius Seltenheim"

version := "1.0.0-alpha1"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
	//misc
	"joda-time" % "joda-time" % "2.1"
	,"org.xerial" % "sqlite-jdbc" % "3.7.2"
	,"commons-dbutils" % "commons-dbutils" % "1.5"
	,"commons-io" % "commons-io" % "2.4"
	, "commons-codec" % "commons-codec" % "1.8"
	//Web stuff
	,"org.webjars" %% "webjars-play" % "2.3.0"
  	,"org.webjars" % "bootstrap" % "3.1.1-2"
  	,"org.webjars" % "jquery" % "2.1.1"
  	,"org.webjars" % "backbonejs" % "1.1.2-2"
  	,"org.webjars" % "underscorejs" % "1.6.0-3"
  	,"org.webjars" % "requirejs-text" % "2.0.10-1"
	//belongs together
	, "org.springframework" % "spring-context" % "4.0.3.RELEASE"
	, "cglib" % "cglib" % "2.2.2"
    , "org.jacoco" % "org.jacoco.core" % "0.6.3.201306030806" artifacts(Artifact("org.jacoco.core", "jar", "jar"))
	, "org.jacoco" % "org.jacoco.report" % "0.6.3.201306030806" artifacts(Artifact("org.jacoco.report", "jar", "jar"))
)     

lazy val root = (project in file(".")).enablePlugins(PlayJava)

seq(aetherSettings: _*)

seq(aetherPublishSettings: _*)

//play.Project.playJavaSettings ++ Seq(jacoco.settings:_*)

//Project.enablePlugins(play.PlayJava)

parallelExecution in jacoco.Config := false

jacoco.excludes in jacoco.Config := Seq("views*","*Routes*","*Reverse*")

//pipelineStages := Seq(rjs,gzip) 
