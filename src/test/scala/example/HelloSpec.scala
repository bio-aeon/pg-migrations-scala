package example

import org.specs2.mutable.Specification

class HelloSpec extends Specification {
  "The Hello object should" >> {
    Hello.greeting mustEqual "hello"
  }
}
