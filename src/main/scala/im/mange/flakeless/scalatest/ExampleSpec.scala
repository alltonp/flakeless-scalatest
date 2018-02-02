package im.mange.flakeless.scalatest

import im.mange.flakeless.{Flakeless, Goto}
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.scalatest.refspec.RefSpec

object Moo {
  val moo = SystemUnderTestPool(
    List(new SystemUnderTest {
            override val baseUrl =  "http://www.google.co.uk"
            override val browser = Flakeless(new PhantomJSDriver())
            override def reset() = {}
            override def reportFailure(t: Throwable) = {}
          }
    )
  )
}

abstract class WebSpecification extends RefSpec with FlakelessSpec {
  val systemUnderTestPool = Moo.moo
}

//TODO: move to example dir
class ExampleSpec extends WebSpecification {
  def `blah blah` = on(sut => {
    Goto(sut.browser, sut.baseUrl)
    println("I've run")
  })
}
