// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package readserial

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec


/** 
  *read serial tester
  */
class ReadSerialTester extends AnyFlatSpec with ChiselScalatestTester {

  "ReadSerial" should "work" in {
    test(new ReadSerial).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

 dut.io.reset_n.poke(true.B)
      dut.io.rxd.poke(1.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      dut.io.reset_n.poke(false.B)
      dut.clock.step(1)
      dut.io.rxd.poke(0.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      for (i <- 7 to 0 by -1) {
        val bit = (0xB2 >> i) & 1
        dut.io.rxd.poke(bit.U(1.W))
        dut.clock.step(1)
      }



      dut.io.valid.expect(1.U)
      dut.io.data.expect(0xB2.U)
      dut.io.rxd.poke(1.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      dut.io.rxd.poke(0.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)

      for (i <- 7 to 0 by -1) {
        val bit = (0xFF >> i) & 1
        dut.io.rxd.poke(bit.U(1.W))
        dut.clock.step(1)
      }
      dut.io.valid.expect(1.U)
      dut.io.data.expect(0xFF.U)
      dut.io.rxd.poke(1.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      dut.io.rxd.poke(0.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      for (i <- 7 to 0 by -1) {
        val bit = (0x00 >> i) & 1
        dut.io.rxd.poke(bit.U(1.W))
        dut.clock.step(1)
      }

      dut.io.valid.expect(1.U)
      dut.io.data.expect(0x00.U)
      dut.io.rxd.poke(1.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      dut.io.rxd.poke(0.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)

      for (i <- 7 to 0 by -1) {
        val bit = (0x55 >> i) & 1
        dut.io.rxd.poke(bit.U(1.W))
        dut.clock.step(1)
      }
      dut.io.valid.expect(1.U)
      dut.io.data.expect(0x55.U)
      dut.io.rxd.poke(1.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      dut.io.rxd.poke(0.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      for (i <- 7 to 0 by -1) {
        val bit = (0x80 >> i) & 1
        dut.io.rxd.poke(bit.U(1.W))
        dut.clock.step(1)
      }
      dut.io.valid.expect(1.U)
      dut.io.data.expect(0x80.U)
      dut.io.rxd.poke(1.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      // Test reset during an unfinished transmission
      dut.io.rxd.poke(0.U(1.W))
      dut.clock.step(1)
      dut.io.rxd.poke(1.U(1.W))
      dut.clock.step(1)
      dut.io.rxd.poke(0.U(1.W))
      dut.clock.step(1)
      dut.io.reset_n.poke(true.B)
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      dut.io.reset_n.poke(false.B)
      dut.io.rxd.poke(1.U(1.W))
      dut.clock.step(1)
      dut.io.rxd.poke(0.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      for (i <- 7 to 0 by -1) {
        val bit = (0xA5 >> i) & 1
        dut.io.rxd.poke(bit.U(1.W))
        dut.clock.step(1)
      }
      dut.io.valid.expect(1.U)
      dut.io.data.expect(0xA5.U)
      dut.io.rxd.poke(1.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
      dut.io.rxd.poke(0.U(1.W))
      dut.clock.step(1)
      for (i <- 7 to 0 by -1) {
        val bit = (0x3C >> i) & 1
        dut.io.rxd.poke(bit.U(1.W))
        dut.clock.step(1)
      }
      dut.io.valid.expect(1.U)
      dut.io.data.expect(0x3C.U)
      dut.io.rxd.poke(0.U(1.W))
      dut.clock.step(1)
      dut.io.valid.expect(0.U)
        }
    } 
}

