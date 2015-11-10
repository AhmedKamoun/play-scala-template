import sbt.Keys._
import sbt._

object Common {
  // Settings for the app, i.e. the root project
  val appSettings = settings(appName)
  val spring_version = "4.1.7.RELEASE"
  val commonDependencies = Seq(
    // Add your project dependencies here,
    "mysql" % "mysql-connector-java" % "5.1.10",
    "org.springframework" % "spring-context" % spring_version,
    "org.springframework" % "spring-orm" % spring_version,
    "org.springframework" % "spring-jdbc" % spring_version,
    "org.springframework" % "spring-tx" % spring_version,
    "org.springframework" % "spring-test" % spring_version % "test",
    "org.springframework" % "spring-context-support" % spring_version,
    "org.springframework.data" % "spring-data-jpa" % "1.9.0.RELEASE",
    "net.sf.ehcache" % "ehcache" % "2.10.0",
    "org.hibernate" % "hibernate-entitymanager" % "4.3.6.Final",
    "org.hibernate" % "hibernate-core" % "4.3.6.Final",
    "org.hibernate" % "hibernate-ehcache" % "4.3.6.Final",
    "org.hibernate" % "hibernate-validator" % "5.1.2.Final",
    "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final",
    "org.jadira.usertype" % "usertype.jodatime" % "2.0.1",
    "com.mysema.querydsl" % "querydsl-jpa" % "3.4.3",
    "com.mysema.querydsl" % "querydsl-apt" % "3.4.3",
    "cglib" % "cglib" % "3.1",
    "c3p0" % "c3p0" % "0.9.1.2",
    "com.jason-goodwin" %% "authentikat-jwt" % "0.4.1",
    "com.cloudinary" % "cloudinary-http42" % "1.2.1"
  )

  def appName = "Kamoun_Application"

  // Settings for every service, i.e. for admin and web subprojects
  def serviceSettings(module: String) = moduleSettings(module) ++: Seq(
    /*
     includeFilter in (Assets, LessKeys.less) := "*.less",
     excludeFilter in (Assets, LessKeys.less) := "_*.less",
     pipelineStages := Seq(rjs, digest, gzip),
     RjsKeys.mainModule := s"main-$module"
     */
  )

  // Settings for every module, i.e. for every subproject
  def moduleSettings(module: String) = settings(module) ++: Seq(
    javaOptions in Test += s"-Dconfig.resource=application.conf"
  )

  // Common settings for every project
  def settings(theName: String) = Seq(
    name := theName,
    organization := "kamoun.org",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.4",
    doc in Compile <<= target.map(_ / "none"),
    scalacOptions in ThisBuild ++= Seq(
      "-target:jvm-1.7",
      "-encoding", "UTF-8",
      "-deprecation", // warning and location for usages of deprecated APIs
      "-feature", // warning and location for usages of features that should be imported explicitly
      "-unchecked", // additional warnings where generated code depends on assumptions
      "-language:reflectiveCalls",
      "-Xlint", // recommended additional warnings
      "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
      "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
      "-Ywarn-inaccessible",
      "-Ywarn-dead-code"
    )
  )
}