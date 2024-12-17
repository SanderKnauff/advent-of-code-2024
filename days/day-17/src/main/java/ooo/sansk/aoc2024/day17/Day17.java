package ooo.sansk.aoc2024.day17;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

class Day17 {
    private static final Pattern REGISTER_LINE_PATTERN = Pattern.compile("Register \\w: (?<register>\\d+)");

    String runPartOne(String input) {
        final var computer = readComputer(input);

        computer.runProgram();

        return String.join(",", computer.output.stream().map(BigInteger::toString).toList());
    }

    int runPartTwo(String input) {
        final var computer = readComputer(input);
        int iterations = -1;

        List<BigInteger> output = List.of();
        while (!output.equals(computer.program)) {
            iterations++;
            final var computerIteration = new Computer(BigInteger.valueOf(iterations), computer.registerB, computer.registerC, computer.program);
            computerIteration.runProgram();
            System.out.println(output);
            output = computerIteration.output;
        }

        return iterations;
    }

    Computer readComputer(String input) {
        final var parts = input.split("\\r?\\n\\r?\\n");
        final var registers = parts[0].split("\\r?\\n");

        final var a = readRegister(registers[0]);
        final var b = readRegister(registers[1]);
        final var c = readRegister(registers[2]);

        final var program = readProgram(parts[1]);

        return new Computer(a, b, c, program);
    }

    private List<BigInteger> readProgram(String input) {
        final var program = input.substring("Program: ".length());
        return Arrays.stream(program.split(","))
                .map(BigInteger::new)
                .toList();
    }

    BigInteger readRegister(String input) {
        final var matcher = REGISTER_LINE_PATTERN.matcher(input);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid input");
        }
        return new BigInteger(matcher.group("register"));
    }

    static class Computer {
        BigInteger registerA;
        BigInteger registerB;
        BigInteger registerC;
        BigInteger instructionPointer;
        List<BigInteger> program;
        List<BigInteger> output;

        public Computer(BigInteger registerA, BigInteger registerB, BigInteger registerC, BigInteger instructionPointer, List<BigInteger> program, List<BigInteger> output) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.instructionPointer = instructionPointer;
            this.program = program;
            this.output = output;
        }

        public Computer(BigInteger registerA, BigInteger registerB, BigInteger registerC, List<BigInteger> program) {
            this(registerA, registerB, registerC, BigInteger.ZERO, program, new ArrayList<>());
        }

        BigInteger getComboOperand(BigInteger operand) {
            if (operand.equals(BigInteger.ZERO) || operand.equals(BigInteger.ONE) || operand.equals(BigInteger.TWO) || operand.equals(BigInteger.valueOf(3))) {
                return operand;
            } else if (operand.equals(BigInteger.valueOf(4L))) {
                return this.registerA;
            } else if (operand.equals(BigInteger.valueOf(5L))) {
                return this.registerB;
            } else if (operand.equals(BigInteger.valueOf(6L))) {
                return this.registerC;
            } else {
                throw new IllegalArgumentException("Invalid operand " + operand);
            }
        }

        void runProgram() {
            while (instructionPointer.compareTo(BigInteger.valueOf(program.size())) < 0) {
                final var opcode = program.get(instructionPointer.intValue());
                final var literalOperand = program.get(instructionPointer.intValue() + 1);
                final var instruction = Instruction.ofOpcode(opcode);

                instruction.execute(this, literalOperand);
            }
        }
    }

    enum Instruction {
        ///  `A` Division
        ADV(0, (computer, operand) -> {
            final var comboOperand = computer.getComboOperand(operand);
            final var denominator = BigInteger.TWO.pow(comboOperand.intValue());
            computer.registerA = computer.registerA.divide(denominator);
            computer.instructionPointer = computer.instructionPointer.add(BigInteger.TWO);
        }),
        ///  `B` xor literal
        BXL(1, (computer, operand) -> {
            computer.registerB = computer.registerB.xor(operand);
            computer.instructionPointer = computer.instructionPointer.add(BigInteger.TWO);
        }),
        /// `B` ?
        BST(2, (computer, operand) -> {
            final var comboOperand = computer.getComboOperand(operand);
            computer.registerB = comboOperand.mod(BigInteger.valueOf(8));
            computer.instructionPointer = computer.instructionPointer.add(BigInteger.TWO);
        }),
        /// Jump Not Zero
        JNZ(3, (computer, operand) -> {
            if (computer.registerA.equals(BigInteger.ZERO)) {
                computer.instructionPointer = computer.instructionPointer.add(BigInteger.TWO);
                return;
            }
            computer.instructionPointer = operand;
        }),
        /// `B` xor `C`
        BXC(4, (computer, operand) -> {
            computer.registerB = computer.registerB.xor(computer.registerC);
            computer.instructionPointer = computer.instructionPointer.add(BigInteger.TWO);
        }),
        ///  Write to output
        OUT(5, (computer, operand) -> {
            final var comboOperand = computer.getComboOperand(operand);
            final var output = comboOperand.mod(BigInteger.valueOf(8));
            computer.output.add(output);
            computer.instructionPointer = computer.instructionPointer.add(BigInteger.TWO);
        }),
        ///  `B` Division
        BDV(6, (computer, operand) -> {
            final var comboOperand = computer.getComboOperand(operand);
            final var denominator = BigInteger.TWO.pow(comboOperand.intValue());
            computer.registerB = computer.registerA.divide(denominator);
            computer.instructionPointer = computer.instructionPointer.add(BigInteger.TWO);
        }),
        ///  `C` Division
        CDV(7, (computer, operand) -> {
            final var comboOperand = computer.getComboOperand(operand);
            final var denominator = BigInteger.TWO.pow(comboOperand.intValue());
            computer.registerC = computer.registerA.divide(denominator);
            computer.instructionPointer = computer.instructionPointer.add(BigInteger.TWO);
        });

        final int opcode;
        final BiConsumer<Computer, BigInteger> operation;

        Instruction(int opcode, BiConsumer<Computer, BigInteger> operation) {
            this.opcode = opcode;
            this.operation = operation;
        }

        void execute(Computer computer, BigInteger operand) {
            this.operation.accept(computer, operand);
        }

        public static Instruction ofOpcode(BigInteger opcode) {
            for (Instruction instruction : Instruction.values()) {
                if (opcode.equals(BigInteger.valueOf(instruction.opcode))) {
                    return instruction;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
