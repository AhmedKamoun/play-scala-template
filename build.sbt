import play.PlayScala

Common.appSettings

lazy val core_dom = (project in file("modules/core_dom")).enablePlugins(PlayScala)

lazy val web = (project in file("modules/web")).enablePlugins(PlayScala).dependsOn(core_dom)

lazy val root = (project in file(".")).enablePlugins(PlayScala).aggregate(core_dom, web).dependsOn(core_dom, web)

libraryDependencies ++= Common.commonDependencies
