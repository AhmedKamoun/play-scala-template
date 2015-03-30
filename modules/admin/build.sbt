Common.serviceSettings("admin")

// Add here the specific settings for this module

libraryDependencies ++= Common.commonDependencies ++: Seq(
  // Add here the specific dependencies for this module:
  // jdbc,
  // anorm
)

libraryDependencies += filters