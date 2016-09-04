package io.prometheus.client.scala

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object Counter {
  def create(name: String)(labels: String*): Any = macro createCounterImpl

  def lookup(name: String)(labels: String*): Any = macro lookupCounterImpl

  def createCounterImpl(c: whitebox.Context)(name: c.Tree)(labels: c.Tree*): c.Tree = {
    import c.universe._

    val className = TypeName(s"Counter${labels.size}")

    q"""
     import _root_.io.prometheus.client.scala.internal.counter._
     new $className[..${List(name) ++ labels}]($name)(..$labels)
    """
  }

  def lookupCounterImpl(c: whitebox.Context)(name: c.Tree)(labels: c.Tree*): c.Tree = {
    import c.universe._

    val className = TypeName(s"Counter${labels.size}")

    q"""
     import _root_.io.prometheus.client.scala.internal.counter._
     implicitly[$className[..${List(name) ++ labels}]]
    """
  }
}
