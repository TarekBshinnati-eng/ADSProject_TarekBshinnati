// ADS I Class Project
// Pipelined RISC-V Core - ID Stage
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)

/*
Instruction Decode (ID) Stage: decoding and operand fetch

Extracted Fields from 32-bit Instruction (see RISC-V specification for reference):
    opcode: instruction format identifier
    funct3: selects variant within instruction format
    funct7: further specifies operation type (R-type only)
    rd: destination register address
    rs1: first source register address
    rs2: second source register address
    imm: 12-bit immediate value (I-type, sign-extended)

Register File Interfaces:
    regFileReq_A, regFileResp_A: read port for rs1 operand
    regFileReq_B, regFileResp_B: read port for rs2 operand

Internal Signals:
    Combinational decoders for instructions

Functionality:
    Decode opcode to determine instruction and identify operation (ADD, SUB, XOR, ...)
    Output: uop (operation code), rd, operandA (from rs1), operandB (rs2 or immediate)

Outputs:
    uop: micro-operation code (identifies instruction type)
    rd: destination register index
    operandA: first operand
    operandB: second operand 
    XcptInvalid: exception flag for invalid instructions
*/

package core_tile

import chisel3._
import chisel3.util._
import uopc._

// -----------------------------------------
// Decode Stage
// -----------------------------------------
class ID extends Module {
    val io = IO(new Bundle{
    val instr = Input(UInt(32.W))
    val regFileReq_A  = Output(new regFileReadReq)
    val regFileResp_A = Input(new regFileReadResp)
    val regFileReq_B  = Output(new regFileReadReq)
    val regFileResp_B = Input(new regFileReadResp)
    val uop = Output(uopc())
    val rd = Output(UInt(5.W))
    val operandA = Output(UInt(32.W))
    val operandB = Output(UInt(32.W))
    val XcptInvalid = Output(Bool())
    })
    val opcode = io.instr(6, 0)
  val rd     = io.instr(11, 7)
  val funct3 = io.instr(14, 12)
  val rs1    = io.instr(19, 15)
  val rs2    = io.instr(24, 20)
  val funct7 = io.instr(31, 25)

  val imm = Cat(Fill(20, io.instr(31)), io.instr(31, 20))
  io.regFileReq_A.addr := rs1
  io.regFileReq_B.addr := rs2
  io.uop := uopc.NOP
  io.rd  := 0.U
  io.operandA := io.regFileResp_A.data
  io.operandB := io.regFileResp_B.data
  io.XcptInvalid := true.B

  switch(opcode) {

    is("b0110011".U) {

      switch(funct3) {
        is("b000".U) {
          when(funct7 === "b0000000".U) {
            io.uop := uopc.ADD
            io.XcptInvalid := false.B
          }.elsewhen(funct7 === "b0100000".U) {
            io.uop := uopc.SUB
            io.XcptInvalid := false.B
          }
        }
        is("b001".U) {
          when(funct7 === "b0000000".U) {
            io.uop := uopc.SLL
            io.XcptInvalid := false.B
          }
        }
        is("b010".U) {
          when(funct7 === "b0000000".U) {
            io.uop := uopc.SLT
            io.XcptInvalid := false.B
          }
        }
        is("b011".U) {
          when(funct7 === "b0000000".U) {
            io.uop := uopc.SLTU
            io.XcptInvalid := false.B
          }
        }
        is("b100".U) {
          when(funct7 === "b0000000".U) {
            io.uop := uopc.XOR
            io.XcptInvalid := false.B
          }
        }
        is("b101".U) {
          when(funct7 === "b0000000".U) {
            io.uop := uopc.SRL
            io.XcptInvalid := false.B
          }.elsewhen(funct7 === "b0100000".U) {
            io.uop := uopc.SRA
            io.XcptInvalid := false.B
          }
        }
        is("b110".U) {
          when(funct7 === "b0000000".U) {
            io.uop := uopc.OR
            io.XcptInvalid := false.B
          }
        }
        is("b111".U) {
          when(funct7 === "b0000000".U) {
            io.uop := uopc.AND
            io.XcptInvalid := false.B
          }
        }
      }
    }

    is("b0010011".U) {
      io.operandB := imm

      switch(funct3) {
        is("b000".U) {
          io.uop := uopc.ADDI
          io.XcptInvalid := false.B
        }
        is("b001".U) {
          when(funct7 === "b0000000".U) {
            io.uop := uopc.SLLI
            io.XcptInvalid := false.B
          }
        }
        is("b010".U) {
          io.uop := uopc.SLTI
          io.XcptInvalid := false.B
        }
        is("b011".U) {
          io.uop := uopc.SLTIU
          io.XcptInvalid := false.B
        }
        is("b100".U) {
          io.uop := uopc.XORI
          io.XcptInvalid := false.B
        }
        is("b101".U) {
          when(funct7 === "b0000000".U) {
            io.uop := uopc.SRLI
            io.XcptInvalid := false.B
          }.elsewhen(funct7 === "b0100000".U) {
            io.uop := uopc.SRAI
            io.XcptInvalid := false.B
          }
        }
        is("b110".U) {
          io.uop := uopc.ORI
          io.XcptInvalid := false.B
        }
        is("b111".U) {
          io.uop := uopc.ANDI
          io.XcptInvalid := false.B
        }
      }
    }
  }
 when(!io.XcptInvalid){
    io.rd :=rd
 } 

}