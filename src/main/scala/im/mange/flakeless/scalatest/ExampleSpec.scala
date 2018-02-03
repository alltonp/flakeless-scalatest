package im.mange.flakeless.scalatest

import im.mange.flakeless.{Flakeless, Goto}
import org.scalatest.refspec.RefSpec

//TODO: application and browser creation should probably be lazy vals ...
//TODO: move to example dir
object Moo {
//  import org.openqa.selenium.phantomjs.PhantomJSDriver

  val moo = SystemUnderTestPool(
    List(new SystemUnderTest {
            override val baseUrl = "http://www.google.co.uk"
//            override val browser = Flakeless(new PhantomJSDriver())
            override val browser = Flakeless(null)
            override def reset() = {}
          }
    )
  )
}

abstract class WebSpec extends RefSpec with FlakelessSpec {
  val systemUnderTestPool = Moo.moo
}

class ExampleSpec extends WebSpec {
  def `blah blah` = using(sut => {
    Goto(sut.browser, sut.baseUrl)
    println("I've run")
  })
}
