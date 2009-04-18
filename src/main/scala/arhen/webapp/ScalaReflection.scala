// From http://gist.github.com/87519
// XXX Really should mixin manually so this isn't on every object

package arhen.webapp

import scala.Console._
import scala.tools.nsc.util.NameTransformer._

object ScalaReflection {
  implicit def any2anyExtras(x: Any) = new AnyExtras(x)
}

class AnyExtras(x: Any) {
  def methods_ = println(methods.reduceLeft[String](_ + ", " + _))
  def methods__ = methods.foreach(println _)
  def fields_ = println(fields.reduceLeft[String](_ + ", " + _))
  def fields__ = fields.foreach(println _)
  
  def methods = wrapped.getClass
      .getDeclaredMethods
      .toList
      .map(m => decode(m.toString
                        .replaceFirst("\\).*", ")")
                        .replaceAll("[^(]+\\.", "")
                        .replace("()", "")))
      .filter(!_.startsWith("$tag"))
  
  def fields = wrapped.getClass
      .getDeclaredFields
      .toList
      .map(m => decode(m.toString.replaceFirst("^.*\\.", "")))

  private def wrapped: AnyRef = x match {
    case x: Byte => byte2Byte(x)
    case x: Short => short2Short(x)
    case x: Char => char2Character(x)
    case x: Int => int2Integer(x)
    case x: Long => long2Long(x)
    case x: Float => float2Float(x)
    case x: Double => double2Double(x)
    case x: Boolean => boolean2Boolean(x)
    case _ => x.asInstanceOf[AnyRef]
  }
}
