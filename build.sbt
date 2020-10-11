import Dependencies._

name := "forex"
version := "1.0.1"

testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))

scalaVersion := "2.13.3"
scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-explaintypes",
  "-unchecked",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:existentials",
  //"-Xfatal-warnings",
  "-Xlint:-infer-any,_",
  "-Ywarn-value-discard",
  "-Ywarn-numeric-widen",
  "-Ywarn-extra-implicit"
)

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

val SilencerVersion = "1.7.1"

libraryDependencies ++= Seq(
  Libraries.cats,
  Libraries.catsEffect,
  Libraries.fs2,
  Libraries.http4sDsl,
  Libraries.http4sServer,
  Libraries.http4sCirce,
  Libraries.circeOptics,
  Libraries.http4sClient,
  Libraries.circeCore,
  Libraries.circeGeneric,
  Libraries.circeGenericExt,
  Libraries.circeParser,
  Libraries.cacheCaffeine,
  Libraries.cacheCirce,
  Libraries.logstage,
  Libraries.pureConfig,
  Libraries.logback,
  Libraries.zioCore,
  Libraries.zioStreams,
  Libraries.zioTestCore,
  Libraries.zioTestSbt,
  Libraries.zioCats,
  Libraries.circeLiteral   % Test,
  Libraries.scalaTest      % Test,
  Libraries.scalaCheck     % Test,
  Libraries.catsScalaCheck % Test,
  ("com.github.ghik" % "silencer-lib" % SilencerVersion % "provided")
    .cross(CrossVersion.full),
  // plugins
  compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  compilerPlugin(
    ("org.typelevel" % "kind-projector" % "0.11.0").cross(CrossVersion.full)
  ),
  compilerPlugin(
    ("com.github.ghik" % "silencer-plugin" % SilencerVersion)
      .cross(CrossVersion.full)
  )
)
