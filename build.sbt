import Dependencies._

name := "forex"
version := "1.0.1"

scalaVersion := "2.13.1"
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

libraryDependencies ++= Seq(
  //compilerPlugin(Libraries.kindProjector),
  Libraries.cats,
  Libraries.catsEffect,
  Libraries.fs2,
  Libraries.http4sDsl,
  Libraries.http4sServer,
  Libraries.http4sCirce,
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
  Libraries.scalaTest        % Test,
  Libraries.scalaCheck       % Test,
  Libraries.catsScalaCheck   % Test
)
