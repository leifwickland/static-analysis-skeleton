scalaVersion := "2.11.6"

// Apply default Scalariform formatting. 
// Reformat at every compile. 
// c.f. https://github.com/sbt/sbt-scalariform#advanced-configuration for more options.
scalariformSettings

scalastyleFailOnError := true

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8", // yes, this is 2 args
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

// A configuration which is like 'compile' except it performs additional static analysis.
// Execute static analysis via `lint:compile`
val LintTarget = config("lint").extend(Compile)

addMainSourcesToLintTarget 

addSlowScalacSwitchesToLintTarget

addWartRemoverToLintTarget

removeWartRemoverFromCompileTarget 

addFoursquareLinterToLintTarget 

removeFoursquareLinterFromCompileTarget 

def addMainSourcesToLintTarget = {
  inConfig(LintTarget) {
    // I posted http://stackoverflow.com/questions/27575140/ and got back the bit below as the magic necessary
    // to create a separate lint target which we can run slow static analysis on.
    Defaults.compileSettings ++ Seq(
      sources in LintTarget := {
        val lintSources = (sources in LintTarget).value
        lintSources ++ (sources in Compile).value
      }
    )
  }
}

def addSlowScalacSwitchesToLintTarget = {
  inConfig(LintTarget) {
    // In addition to everything we normally do when we compile, we can add additional scalac switches which are
    // normally too time consuming to run.
    scalacOptions in LintTarget ++= Seq(
      // As it says on the tin, detects unused imports. This is too slow to always include in the build.
      "-Ywarn-unused-import",
      //This produces errors you don't want in development, but is useful.
      "-Ywarn-dead-code"
    )
  }
}

def addWartRemoverToLintTarget = {
  import wartremover._
  import Wart._
  // I didn't simply include WartRemove in the build all the time because it roughly tripled compile time.
  inConfig(LintTarget) {
    wartremoverErrors ++= Seq(
      // Ban inferring Any, Serializable, and Product because such inferrence usually indicates a code error.
      Wart.Any,
      Wart.Serializable,
      Wart.Product,
      // Ban calling partial methods because they behave surprisingingly 
      Wart.ListOps,
      Wart.OptionPartial,
      Wart.EitherProjectionPartial,
      // Ban applying Scala's implicit any2String because it usually indicates a code error.
      Wart.Any2StringAdd
    )
  }
}

def removeWartRemoverFromCompileTarget = {
  // WartRemover's sbt plugin calls addCompilerPlugin which always adds directly to the Compile configuration.
  // The bit below removes all switches that could be passed to scalac about WartRemover during a non-lint compile.
  scalacOptions in Compile := (scalacOptions in Compile).value filterNot { switch =>
    switch.startsWith("-P:wartremover:") ||
    "^-Xplugin:.*/org[.]brianmckenna/.*wartremover.*[.]jar$".r.pattern.matcher(switch).find
  }
}

def addFoursquareLinterToLintTarget = {
  Seq(
    resolvers += "Linter Repository" at "https://hairyfotr.github.io/linteRepo/releases",
    addCompilerPlugin("com.foursquare.lint" %% "linter" % "0.1.9"),
    // See https://github.com/HairyFotr/linter#list-of-implemented-checks for a list of checks that foursquare linter
    // implements
    // By default linter enables all checks.
    // I don't mind using match on boolean variables.
    scalacOptions in LintTarget += "-P:linter:disable:PreferIfToBooleanMatch"
  )
}

def removeFoursquareLinterFromCompileTarget = {
  // We call addCompilerPlugin in project/plugins.sbt to add a depenency on the foursquare linter so that sbt magically
  // manages the JAR for us.  Unfortunately, addCompilerPlugin also adds a switch to scalacOptions in the Compile config
  // to load the plugin.
  // The bit below removes all switches that could be passed to scalac about Foursquare Linter during a non-lint compile.
  scalacOptions in Compile := (scalacOptions in Compile).value filterNot { switch =>
    switch.startsWith("-P:linter:") ||
      "^-Xplugin:.*/com[.]foursquare[.]lint/.*linter.*[.]jar$".r.pattern.matcher(switch).find
  }
}
