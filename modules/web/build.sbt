Common.serviceSettings("web")

// Add here the specific settings for this module

libraryDependencies ++= Common.commonDependencies ++: Seq(
  // Add here the specific dependencies for this module:

)

libraryDependencies += filters

libraryDependencies += ws