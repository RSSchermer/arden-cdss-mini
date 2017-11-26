name := """arden-cdss-mini"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, LauncherJarPlugin)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",
  "org.xerial" % "sqlite-jdbc" % "3.16.1",
  cache,
  javaWs,
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "bootstrap" % "3.3.5"
)

PlayKeys.externalizeResources := false

mappings in Universal ++= (baseDirectory.value / "db" * "*" get) map (x => x -> ("db/" + x.getName))
mappings in Universal ++= (baseDirectory.value / "mlm" ***).get pair relativeTo(baseDirectory.value)
