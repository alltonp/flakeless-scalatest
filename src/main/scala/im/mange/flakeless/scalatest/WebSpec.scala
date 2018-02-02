package im.mange.flakeless.scalatest

import im.mange.flakeless.Flakeless
import org.scalatest.{FreeSpec, Matchers, Outcome, TestSuite}
import org.scalatest.refspec.RefSpec

trait WebSpec extends TestSuite {
  private val _currentTestName = new ThreadLocal[String]
  private val suite = this.suiteId.split("\\.").reverse.head

  override def withFixture(test: NoArgTest): Outcome = {
    _currentTestName.set(test.name)
    val outcome = super.withFixture(test)
    _currentTestName.set(null)
    outcome
  }

  protected def currentTestName: String = {
    val testName = _currentTestName.get()
    assert(testName != null, "currentTestName should only be called in a test")
    testName
  }

  def testInBrowser(testBody: Flakeless => Unit): Unit = {
    println(suite + ":" + currentTestName)
//   val flakeless = Flakeless(null)
//   testBody(flakeless)
    testBody(null)
 }
}

//TOODO: move to example dir
class ExampleSpec extends RefSpec with WebSpec {
  def `blah blah` = testInBrowser(flakeless => {
    //flakeless
    //    true mustBe true
    println("I've run")
  })
}
