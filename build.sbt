import de.johoop.jacoco4sbt.JacocoPlugin._
import com.typesafe.config._
import aether.Aether._

name := "mate-database"

organization := "info.seltenheim"

organizationName  := "Julius Seltenheim"

version := "1.0.0-alpha1"

libraryDependencies ++= Seq(
	//misc
	"joda-time" % "joda-time" % "2.1"
	,"org.xerial" % "sqlite-jdbc" % "3.7.2"
	,"commons-dbutils" % "commons-dbutils" % "1.5"
	,"commons-io" % "commons-io" % "2.4"
	, "commons-codec" % "commons-codec" % "1.8"
	//belongs together
	, "org.springframework" % "spring-context" % "3.2.5.RELEASE"
	, "cglib" % "cglib" % "2.2.2"
    , "org.jacoco" % "org.jacoco.core" % "0.6.3.201306030806" artifacts(Artifact("org.jacoco.core", "jar", "jar"))
	, "org.jacoco" % "org.jacoco.report" % "0.6.3.201306030806" artifacts(Artifact("org.jacoco.report", "jar", "jar"))
)     

seq(aetherSettings: _*)

seq(aetherPublishSettings: _*)

play.Project.playJavaSettings ++ Seq(jacoco.settings:_*)

parallelExecution in jacoco.Config := false

jacoco.excludes in jacoco.Config := Seq("views*","*Routes*","*Reverse*") 
