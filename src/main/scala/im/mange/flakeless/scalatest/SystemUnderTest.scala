package im.mange.flakeless.scalatest

import im.mange.flakeless.Flakeless

trait SystemUnderTest {
  val baseUrl: String
  val browser: Flakeless
  def reset(): Unit
  def reportFailure(t: Throwable): Unit
}
