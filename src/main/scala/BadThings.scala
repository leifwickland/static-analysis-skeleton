// When uncommented this line will be identified as an unused import by -Ywarn-unused-import
// import scala.MatchError

object BadThings {
  // When uncommented, this line will be identified as inferring any by the -Xlint scalac switches
  // val xLintExample = Seq(1 -> 2, 2 -> "3")

  // When uncommented this line will be identified as a compiler error because of -deprecation and -Xfatal-warnings
  // val deprecationExample = Seq(1, 2, 3).toSet()

  // When uncommented, this line will be identifed as an unintentional string concatenation by WartRemover
  // val wartRemoverExample = Seq(1, 2) + 3

  // When uncommented this line will be identified as a bad idea by Foursquare Linter
  // val fourSquareLinterExample = Array(1,2,3).toString

  // When uncommented and an 'n' added to the method name Scalastyle will identify this as writing to stdout
  // printl("Typically we don't want to ship writing to standard out in production code.")
}
