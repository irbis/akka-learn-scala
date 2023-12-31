package com.github.irbis.akkaiotapp

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.github.irbis.akkaiotapp.Device.{Passivate, RecordTemperature, TemperatureRecorded}
import com.github.irbis.akkaiotapp.DeviceManager.{DeviceRegistered, ReplyDeviceList, RequestAllTemperatures, RequestDeviceList, RequestTrackDevice, RespondAllTemperatures, Temperature, TemperatureNotAvailable}
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatest.wordspec.AnyWordSpecLike

class DeviceGroupSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  "Device Group actor" must {
    "be able to register a device actor" in {
      val probe = createTestProbe[DeviceRegistered]()
      val groupActor = spawn(DeviceGroup("group"))

      groupActor ! RequestTrackDevice("group", "device1", probe.ref)
      val registered1 = probe.receiveMessage()
      val deviceActor1 = registered1.device

      // another deviceId
      groupActor ! RequestTrackDevice("group", "device2", probe.ref)
      val registered2 = probe.receiveMessage()
      val deviceActor2 = registered2.device
      deviceActor1 should !==(deviceActor2)

      // check that the device actors are working
      val recordProbe = createTestProbe[TemperatureRecorded]()
      deviceActor1 ! RecordTemperature(requestId = 0, 1.0, recordProbe.ref)
      recordProbe.expectMessage(TemperatureRecorded(requestId = 0))
      deviceActor2 ! Device.RecordTemperature(requestId = 1, 2.0, recordProbe.ref)
      recordProbe.expectMessage(Device.TemperatureRecorded(requestId = 1))
    }
  }

  "ignore requests for wrong groupId" in {
    val probe = createTestProbe[DeviceRegistered]()
    val groupActor = spawn(DeviceGroup("groupId"))

    groupActor ! RequestTrackDevice("wrongGroup", "device1", probe.ref)
    probe.expectNoMessage(500.milliseconds)
  }

  "return same actor for same deviceId" in {
    val probe = createTestProbe[DeviceRegistered]()
    val groupActor = spawn(DeviceGroup("group"))

    groupActor ! RequestTrackDevice("group", "device1", probe.ref)
    val registered1 = probe.receiveMessage()

    // registering same again should be idempotent
    groupActor ! RequestTrackDevice("group", "device1", probe.ref)
    val registered2 = probe.receiveMessage()

    registered1.device should ===(registered2.device)
  }

  "be able to list active devices" in {
    val registeredProbe = createTestProbe[DeviceRegistered]()
    val groupActor = spawn(DeviceGroup("group"))

    groupActor ! RequestTrackDevice("group", "device1", registeredProbe.ref)
    registeredProbe.receiveMessage()

    groupActor ! RequestTrackDevice("group", "device2", registeredProbe.ref)
    registeredProbe.receiveMessage()

    val deviceListProbe = createTestProbe[ReplyDeviceList]()
    groupActor ! RequestDeviceList(requestId = 0, groupId = "group", deviceListProbe.ref)
    deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set("device1", "device2")))
  }

  "be able to list active devices after one shuts down" in {
    val registeredProbe = createTestProbe[DeviceRegistered]()
    val groupActor = spawn(DeviceGroup("group"))

    groupActor ! RequestTrackDevice("group", "device1", registeredProbe.ref)
    val registered1 = registeredProbe.receiveMessage()
    val toShutDown = registered1.device

    groupActor ! RequestTrackDevice("group", "device2", registeredProbe.ref)
    registeredProbe.receiveMessage()

    val deviceListProbe = createTestProbe[ReplyDeviceList]()
    groupActor ! RequestDeviceList(requestId = 0, groupId = "group", deviceListProbe.ref)
    deviceListProbe.expectMessage(ReplyDeviceList(requestId = 0, Set("device1", "device2")))

    toShutDown ! Passivate
    registeredProbe.expectTerminated(toShutDown, registeredProbe.remainingOrDefault)

    // using awaitAssert to retry because it might take longer for the groupActor
    // to see the Terminated, that order is undefined
    registeredProbe.awaitAssert {
      groupActor ! RequestDeviceList(requestId = 1, groupId = "group", deviceListProbe.ref)
      deviceListProbe.expectMessage(ReplyDeviceList(requestId = 1, Set("device2")))
    }
  }

  "be able to collect temperatures from all active devices" in {
    val registeredProbe = createTestProbe[DeviceRegistered]()
    val groupActor = spawn(DeviceGroup("group"))

    groupActor  ! RequestTrackDevice("group", "device1", registeredProbe.ref)
    val deviceActor1 = registeredProbe.receiveMessage().device

    groupActor  ! RequestTrackDevice("group", "device2", registeredProbe.ref)
    val deviceActor2 = registeredProbe.receiveMessage().device

    groupActor  ! RequestTrackDevice("group", "device3", registeredProbe.ref)
    registeredProbe.receiveMessage()

    // Check that the device actors are working
    val recordProbe = createTestProbe[TemperatureRecorded]()
    deviceActor1 ! RecordTemperature(requestId = 0, 1.0, recordProbe.ref)
    recordProbe.expectMessage(TemperatureRecorded(requestId = 0))
    deviceActor2 ! RecordTemperature(requestId = 1, 2.0, recordProbe.ref)
    recordProbe.expectMessage(TemperatureRecorded(requestId = 1))
    // No temperature for device3

    val allTempProbe = createTestProbe[RespondAllTemperatures]()
    groupActor ! RequestAllTemperatures(requestId = 0, groupId = "group", allTempProbe.ref)
    allTempProbe.expectMessage(
      RespondAllTemperatures(
        requestId = 0,
        temperatures = Map("device1" -> Temperature(1.0),
                           "device2" -> Temperature(2.0),
                           "device3" -> TemperatureNotAvailable)))
  }
}
