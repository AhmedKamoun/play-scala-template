
val appName = "SCALA_PLAY_SPRING_HIBERNATE"

val appVersion = "1.0-SNAPSHOT"

val scalaAppVersion = "2.11.4"

libraryDependencies ++= Seq(
  // Add your project dependencies here,
  "mysql" % "mysql-connector-java" % "5.1.10",
  "org.springframework" % "spring-context" % "4.0.7.RELEASE",
  "org.springframework" % "spring-orm" % "4.0.7.RELEASE",
  "org.springframework" % "spring-jdbc" % "4.0.7.RELEASE",
  "org.springframework" % "spring-tx" % "4.0.7.RELEASE",
  "org.springframework" % "spring-test" % "4.0.7.RELEASE" % "test",
  "org.springframework.data" % "spring-data-jpa" % "1.7.0.RELEASE",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.6.Final",
  "org.hibernate" % "hibernate-core" % "4.3.6.Final",
  "org.hibernate" % "hibernate-ehcache" % "4.3.6.Final",
  "org.hibernate" % "hibernate-validator" % "5.1.2.Final",
  "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final",
  "org.jadira.usertype" % "usertype.jodatime" % "2.0.1",
  "com.mysema.querydsl" % "querydsl-jpa" % "3.4.3",
  "com.mysema.querydsl" % "querydsl-apt" % "3.4.3",
  "cglib" % "cglib" % "3.1",
  "c3p0" % "c3p0" % "0.9.1.2"
)

// Scala Compiler Options
scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint", // recommended additional warnings
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

pipelineStages := Seq(digest, gzip)

lazy val root = (project in file("."))
  .settings(
    name := appName,
    version := appVersion,
    scalaVersion := scalaAppVersion
  ).enablePlugins(PlayScala)