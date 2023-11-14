package com.github.irbis.akkaiotapp

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.github.irbis.akkaiotapp.DeviceManager.{DeviceRegistered, ReplyDeviceList, RequestDeviceList, RequestTrackDevice}
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatest.wordspec.AnyWordSpecLike

class DeviceManagerSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  "Device Manager actor" must {
    "be able to register a device actor into new device group" in {
      val probe = createTestProbe[DeviceRegistered]()
      val managerActor = spawn(DeviceManager())

      managerActor ! RequestTrackDevice("group1", "device1", probe.ref)
      probe.receiveMessage()

      val deviceListProbe = createTestProbe[ReplyDeviceList]()
      managerActor ! RequestDeviceList(requestId = 0, groupId = "group1", deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set("device1")))
    }

    "be able not to return a device group that does not exist" in {
      val deviceListProbe = createTestProbe[ReplyDeviceList]()
      val managerActor = spawn(DeviceManager())

      managerActor ! RequestDeviceList(requestId = 0, groupId = "group", replyTo = deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set.empty))
    }

    "be able not to duplicate groups while registering several devices in with same groupId" in {
      val deviceRegisteredProbe = createTestProbe[DeviceRegistered]()
      val managerActor = spawn(DeviceManager())

      managerActor ! RequestTrackDevice("group1", "device1", deviceRegisteredProbe.ref)
      val registered1 = deviceRegisteredProbe.receiveMessage()
      val deviceActor1 = registered1.device

      // another deviceId and same groupId
      managerActor ! RequestTrackDevice("group1", "device2", deviceRegisteredProbe.ref)
      val registered2 = deviceRegisteredProbe.receiveMessage()
      val deviceActor2 = registered2.device

      // check if devices were registered
      deviceActor1 should !==(deviceActor2)

      val deviceListProbe = createTestProbe[ReplyDeviceList]()
      managerActor ! RequestDeviceList(requestId = 0, groupId = "group1", deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set("device1", "device2")))
    }

    "return same actor for same deviceId and groupId" in {
      val deviceRegisteredProbe = createTestProbe[DeviceRegistered]()
      val managedActor = spawn(DeviceManager())

      managedActor ! RequestTrackDevice("group1", "device1", deviceRegisteredProbe.ref)
      val registered1 = deviceRegisteredProbe.receiveMessage()

      // registering same again should be idempotent
      managedActor ! RequestTrackDevice("group1", "device1", deviceRegisteredProbe.ref)
      val registered2 = deviceRegisteredProbe.receiveMessage()

      registered1.device should ===(registered2.device)
    }

    "return different actors from different groups with same deviceId" in {
      val deviceRegisteredProbe = createTestProbe[DeviceRegistered]()
      val managedActor = spawn(DeviceManager())

      managedActor ! RequestTrackDevice("group1", "device1", deviceRegisteredProbe.ref)
      val registeredInGroup1 = deviceRegisteredProbe.receiveMessage()

      managedActor ! RequestTrackDevice("group2", "device1", deviceRegisteredProbe.ref)
      val registeredInGroup2 = deviceRegisteredProbe.receiveMessage()

      registeredInGroup1.device should !==(registeredInGroup2.device)
    }

    "be able to create different groups while registering devices with different groupId" in {
      val deviceRegisteredProbe = createTestProbe[DeviceRegistered]()
      val managerActor = spawn(DeviceManager())

      managerActor ! RequestTrackDevice("group1", "device1", deviceRegisteredProbe.ref)
      deviceRegisteredProbe.receiveMessage()

      managerActor ! RequestTrackDevice("group2", "device1", deviceRegisteredProbe.ref)
      deviceRegisteredProbe.receiveMessage()

      val deviceListProbe = createTestProbe[ReplyDeviceList]()
      managerActor ! RequestDeviceList(requestId = 0, groupId = "group1", deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set("device1")))

      managerActor ! RequestDeviceList(requestId = 0, groupId = "group2", deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set("device1")))
    }
  }
}
