// When uncommented this line will be identified as an unused import by -Ywarn-unused-import
// import scala.MatchError

object BadThings {
  // When uncommented, this line will be identified as inferring any by the -Xlint scalac switches when running `compile`.
  // val xLintExample = Seq(1, 2, "3")

  // When uncommented this line will be identified as a compiler error because of -deprecation and -Xfatal-warnings when running `compile`.
  // val deprecationExample = Seq(1, 2, 3).toSet()

  // When uncommented, this line will be identifed as an unintentional string concatenation by WartRemover when running `lint:compile`.
  // val wartRemoverExample = Seq(1, 2) + 3

  // When uncommented this line will be identified as a bad idea by HairyFotr Linter when running `lint:compile`.
  // val hairyFotrLinterExample = Array(1, 2, 3).toString

  // When uncommented and an 'n' added to the method name Scalastyle will identify this as writing to stdout when running `scalastyle`.
  // printl("Typically we don't want to ship writing to standard out in production code.")
}
