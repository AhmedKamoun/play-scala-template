import play.PlayScala

Common.appSettings

lazy val dom = (project in file("modules/core_dom")).enablePlugins(PlayScala)

lazy val web = (project in file("modules/web")).enablePlugins(PlayScala).dependsOn(dom)

lazy val root = (project in file(".")).enablePlugins(PlayScala).aggregate(dom, web).dependsOn(dom, web)

libraryDependencies ++= Common.commonDependencies
