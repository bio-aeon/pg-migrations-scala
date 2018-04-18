package su.wps.pgmigrations

import com.dimafeng.testcontainers.Container
import org.junit.runner.Description
import org.specs2.specification.BeforeAfterAll

import scala.util.Try

trait ForAllTestContainer extends BeforeAfterAll {
  val container: Container

  implicit private val suiteDescription = Description.createSuiteDescription(this.getClass)

  def beforeAll(): Unit = {
    Try(container.starting())
    ()
  }

  def afterAll(): Unit = {
    Try(container.finished())
    ()
  }
}
