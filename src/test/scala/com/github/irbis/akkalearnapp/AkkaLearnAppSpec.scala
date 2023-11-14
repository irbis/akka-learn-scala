package com.github.irbis.akkalearnapp

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.github.irbis.akkalearnapp.GreeterActor.{GreetMessage, GreetedMessage}
import org.scalatest.wordspec.AnyWordSpecLike

class AkkaLearnAppSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  "A GreeterActor" must {
    "reply to greeted" in {
      val replyProbe = createTestProbe[GreetedMessage]()
      val underTest = spawn(GreeterActor())

      underTest ! GreetMessage("Santa", replyProbe.ref)
      replyProbe.expectMessage(GreetedMessage("Santa", underTest.ref))
    }
  }

}