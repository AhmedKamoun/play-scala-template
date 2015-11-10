// Comment to get more information during initialization
logLevel := Level.Debug

// The Typesafe repository
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.10")

//sbt-web plugin for checksum files: https://github.com/sbt/sbt-digest
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.0")

//sbt-web plugin for gzip compressing web assets.https://github.com/sbt/sbt-gzip
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

