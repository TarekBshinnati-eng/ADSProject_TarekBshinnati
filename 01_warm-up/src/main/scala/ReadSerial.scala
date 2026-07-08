// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package readserial

import chisel3._
import chisel3.util._


/** controller class */
class Controller extends Module{
  
  val io = IO(new Bundle {
     val reset_n = Input(Bool())
     val rxd = Input(UInt(1.W))
     val cnt_s = Input(Bool())
     val cnt_en = Output(Bool())
     val valid = Output(UInt(1.W))
    })
    val idle :: recieving :: done :: Nil = Enum(3)
    val state = RegInit(idle)
    io.cnt_en := false.B
    io.valid := 0.U
  when (io.reset_n){
   io.valid := 0.U
   io.cnt_en := false.B
   state:= idle
   }
   .otherwise{
   switch(state){
    is(idle){
   io.valid := 0.U
   io.cnt_en := false.B
   when (io.rxd === 0.U){
  state:= recieving
    }
    }
   is(recieving){
      io.valid := 0.U
   io.cnt_en := true.B
    when (io.cnt_s){state:=done}
   }
   is(done){
    io.cnt_en :=false.B
    io.valid := 1.U
    when(io.rxd===0.U){state:= recieving}.otherwise{state:= idle}
   }
   }
   
   
   
   
   
   
   
   }

}


/** counter class */
class Counter extends Module{
  
  val io = IO(new Bundle {
   val cnt_en = Input(Bool())
   val reset_n = Input(Bool())
   val cnt_s = Output (Bool())

    })
    val counter = RegInit(8.U(4.W))
  when (io.reset_n){
   counter := 8.U
  } .elsewhen (!io.cnt_en){
    counter := 8.U
  } .otherwise {
   counter:= counter -1.U
  }
   io.cnt_s := (io.cnt_en && (counter===1.U))
  // state machine
  /* 
   * TODO: Describe functionality if the counter as a state machine
   */


}

/** shift register class */
class ShiftRegister extends Module{
  
  val io = IO(new Bundle {
    val rxd = Input(UInt(1.W))
    val cnt_en = Input(Bool())
    val reset_n = Input(Bool())
    val data = Output(UInt(8.W))
    })

   val Sreg = RegInit(0.U(8.W))
   when (io.reset_n){Sreg:=0.U} .elsewhen (io.cnt_en){ Sreg := (Sreg<<1)+io.rxd}

   io.data := Sreg
 
}

/** 
  * The last warm-up task deals with a more complex component. Your goal is to design a serial receiver.
  * It scans an input line (“serial bus”) named rxd for serial transmissions of data bytes. A transmission 
  * begins with a start bit ‘0’ followed by 8 data bits. The most significant bit (MSB) is transmitted first. 
  * There is no parity bit and no stop bit. After the last data bit has been transferred a new transmission 
  * (beginning with a start bit, ‘0’) may immediately follow. If there is no new transmission the bus line 
  * goes high (‘1’, this is considered the “idle” bus signal). In this case the receiver waits until the next 
  * transmission begins. The outputs of the design are an 8-bit parallel data signal and a valid signal. 
  * The valid signal goes high (‘1’) for one clock cycle after the last serial bit has been transmitted, 
  * indicating that a new data byte is ready.
  */
class ReadSerial extends Module{
  
  val io = IO(new Bundle {
     val reset_n = Input(Bool())
     val rxd = Input(UInt(1.W))

     val data = Output(UInt(8.W))
     val valid = Output(UInt(1.W))

    })


  val ControllerSR = Module(new Controller)
  val CounterSR = Module(new Counter)
  val SReg = Module(new ShiftRegister)

 ControllerSR.io.rxd := io.rxd
 SReg.io.rxd:= io.rxd

 ControllerSR.io.reset_n:=io.reset_n
 CounterSR.io.reset_n:=io.reset_n
 SReg.io.reset_n:=io.reset_n

CounterSR.io.cnt_en:=ControllerSR.io.cnt_en
ControllerSR.io.cnt_s:= CounterSR.io.cnt_s
SReg.io.cnt_en:=ControllerSR.io.cnt_en
 io.data := SReg.io.data
 io.valid:= ControllerSR.io.valid
  // global I/O 
  /* 
   * TODO: Describe output behaviour based on the input values and the internal signals
   */

}
