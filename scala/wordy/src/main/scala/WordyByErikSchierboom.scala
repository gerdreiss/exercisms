// shamelessly stolen from https://exercism.org/tracks/scala/exercises/wordy/solutions/ErikSchierboom

import scala.util.parsing.combinator._

sealed trait Expression {
  def solve: Int
}
case class Operand(n: Int) extends Expression {
  def solve: Int = n
}
case class Sum(e1: Expression, e2: Expression) extends Expression {
  def solve: Int = e1.solve + e2.solve
}
case class Subtract(e1: Expression, e2: Expression) extends Expression {
  def solve: Int = e1.solve - e2.solve
}
case class Multiply(e1: Expression, e2: Expression) extends Expression {
  def solve: Int = e1.solve * e2.solve
}
case class Divide(e1: Expression, e2: Expression) extends Expression {
  def solve: Int = e1.solve / e2.solve
}
case class Power(e1: Expression, e2: Expression) extends Expression {
  def solve: Int = Math.pow(e1.solve, e2.solve).toInt
}
object WordProblem extends RegexParsers {

  def operand: Parser[Operand] = """(0|-?[1-9]\d*)""".r ^^ { str => Operand(str.toInt) }
  def operator: Parser[String] = "plus" | "minus" | "multiplied by" | "divided by" | "raised to the"
  def expression: Parser[Expression] = operand | operation

  def operation: Parser[Expression] =
    expression ~ (operator ~ expression).* ^^ {
      case expr ~ list =>
        list.foldLeft(expr) {
          case (left, "plus" ~ right) => Sum(left, right)
          case (left, "minus" ~ right) => Subtract(left, right)
          case (left, "multiplied by" ~ right) => Multiply(left, right)
          case (left, "divided by" ~ right) => Divide(left, right)
          case (left, "raised to the" ~ right) => Power(left, right)
        }
    }

  def equation: Parser[Expression] = // this last part below had to be fixed to make it work
    "What is " ~> operation <~ """((st|nd|rd|th) power)?\?""".r // fixed from "?"

  def apply(input: String): Option[Int] = {
    parse(equation, input) match {
      case Success(expression, _) => Some(expression.solve)
      case _ => None
    }
  }
}
