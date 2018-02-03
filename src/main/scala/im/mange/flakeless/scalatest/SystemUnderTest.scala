package im.mange.flakeless.scalatest

import im.mange.flakeless.Flakeless
import im.mange.flakeless.reports.CurrentTestReport

trait SystemUnderTest {
  val baseUrl: String
  val browser: Flakeless
  def resetBeforeTest(): Unit
  def reportFailure(t: Throwable): Unit = { CurrentTestReport(browser) }
}
