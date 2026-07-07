// ADS I Class Project
// Chisel Introduction
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 18/10/2022 by Tobias Jauch (@tojauch)

package adder

import chisel3._
import chisel3.util._


/** 
  * Half Adder Class 
  * 
  * Your task is to implement a basic half adder as presented in the lecture.
  * Each signal should only be one bit wide (inputs and outputs).
  * There should be no delay between input and output signals, we want to have
  * a combinational behaviour of the component.
  */
class HalfAdder extends Module{
  
  val io = IO(new Bundle {
   val a  = Input(UInt(1.W))
   val b  = Input(UInt(1.W))
   val s  = Output(UInt(1.W))
   val co = Output(UInt(1.W))
   
    })
  val sum = Wire(UInt(2.W))

  sum := io.a + io.b
  io.s := sum(0)
  io.co := sum(1)
}

/** 
  * Full Adder Class 
  * 
  * Your task is to implement a basic full adder. The component's behaviour should 
  * match the characteristics presented in the lecture. In addition, you are only allowed 
  * to use two half adders (use the class that you already implemented) and basic logic 
  * operators (AND, OR, ...).
  * Each signal should only be one bit wide (inputs and outputs).
  * There should be no delay between input and output signals, we want to have
  * a combinational behaviour of the component.
  */

class FullAdder extends Module{

  val io = IO(new Bundle {
   val a  = Input(UInt(1.W))
   val b  = Input(UInt(1.W))
   val ci = Input(UInt(1.W))
   val s  = Output(UInt(1.W))
   val co = Output(UInt(1.W))
    })


  val HalfAdder1(new HalfAdder)
  val HalfAdder2(new HalfAdder)

  HalfAdder1.io.a:= io.a
  HalfAdder1.io.b:= io.b
  HalfAdder2.io.a := HalfAdder1.io.co
  HalfAdder2.io.b := HalfAdder1.io.s

  io.s := HalfAdder2.io.s
  io.co := HalfAdder1.io.co | HalfAdder2.io.co
}

/** 
  * 4-bit Adder class 
  * 
  * Your task is to implement a 4-bit ripple-carry-adder. The component's behaviour should 
  * match the characteristics presented in the lecture.  Remember: An n-bit adder can be 
  * build using one half adder and n-1 full adders.
  * The inputs and the result should all be 4-bit wide, the carry-out only needs one bit.
  * There should be no delay between input and output signals, we want to have
  * a combinational behaviour of the component.
  */
class FourBitAdder extends Module{

  val io = IO(new Bundle {
   val a  = Input(UInt(4.W))
   val b  = Input(UInt(4.W))
   val s  = Output(UInt(4.W))
   val co = Output(UInt(1.W))
    })
  val HAdder(new HalfAdder)
  val FAdder1(new FullAdder)
  val FAdder2(new FullAdder)
  val FAdder3(new FullAdder)
  //LSB
  HAdder.io.a:= io.a(0)
  HAdder.io.b:= io.b(0)
  //bit 1
  FAdder1.io.a:= io.a(1)
  FAdder1.io.b:= io.b(1)
  FAdder1.io.ci:= io.Hadder.co
  //bit 2  
  FAdder2.io.a:= io.a(2)
  FAdder2.io.b:= io.b(2)
  FAdder2.io.ci:= io.FAdder1.co
  //bit 3 
  FAdder3.io.a:= io.a(3)
  FAdder3.io.b:= io.b(3)
  FAdder3.io.ci:= io.FAdder2.co  
  io.s := cat(HAdder.io.s,FAdder1.io.s, FAdder2.io.s,FAdder3.io.s)
  io.co := FAdder3.io.co
}
