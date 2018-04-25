package im.mange.flakeless.scalatest

import java.util.concurrent.{LinkedBlockingQueue, TimeUnit}
import collection.mutable.ListBuffer

case class SystemUnderTestPool[T](private val suts: List[T])  {
  private val all = new ListBuffer[T]
  private val available = new LinkedBlockingQueue[T]

  suts.foreach(add)

  def write(systemUnderTest: T) {
    available.put(systemUnderTest)
  }

  def take(): Option[T] = {
    Option(available.poll(60000, TimeUnit.MILLISECONDS))
  }

  def status = s"all: $all\navailable: $all"

  private def add(sut: T) {
    all.append(sut)
    available.put(sut)
  }
}