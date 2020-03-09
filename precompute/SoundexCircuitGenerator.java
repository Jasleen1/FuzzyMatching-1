package examples.generators.soundex;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import circuit.eval.CircuitEvaluator;
import circuit.structure.CircuitGenerator;
import circuit.structure.Wire;

import examples.gadgets.soundex.SoundexGadget;

public class SoundexCircuitGenerator extends CircuitGenerator {

  private Wire[] a;
  private Wire[] b;
  private int inputLen;
  private int dataLen;
  private String string_a;
  List<String> data;

  public SoundexCircuitGenerator(String circuitName, String dataFile, int inputLen, String inputA) throws IOException {
    super(circuitName);
    this.inputLen = inputLen;
    data =  Files.readAllLines(Paths.get(dataFile), StandardCharsets.UTF_8);
    // System.out.println(data);
    string_a = inputA;
    dataLen = data.size();
  }

  @Override
  protected void buildCircuit(){

    a = createInputWireArray(inputLen, "Input a");
    b = createInputWireArray(dataLen, "Input b");
    SoundexGadget soundexGadget = new SoundexGadget(a, "Compute Soundex for input a");
    Wire[] aSoundex = soundexGadget.getOutputWires();
    Wire[] result = new Wire[5];
    Wire joinedSoundex = getZeroWire();
    for (int i = 0; i < 4; i++) {
      result[i] = aSoundex[i];
      addDebugInstruction(result[i], "result[" + i + "]");
      if (i < 3)
        joinedSoundex = aSoundex[i].shiftLeft(32, (3-i)*8).orBitwise(joinedSoundex, 32);
    }
    addDebugInstruction(joinedSoundex, "joinedSoundex is");
    for (int i = 0; i < data.size(); i++) {
      Wire soundexEq = joinedSoundex.isEqualTo(b[i]);
      addZeroAssertion(soundexEq);
      addDebugInstruction(soundexEq, "soundexCode and " + data.get(i) + " are equal");
    }
    result[4] = getZeroWire();

    makeOutputArray(result, "output of soundex");
  }

  @Override
  public void generateSampleInput(CircuitEvaluator circuitEvaluator) {
    for (int i = 0; i < string_a.length(); i++) {
      int int_a = string_a.charAt(i);
      circuitEvaluator.setWireValue(a[i], int_a);
    }

    for (int i = 0; i < data.size(); i++) {
      int int_b = 0;
      for (int j = 0; j < 4; j++) {
        if (j == 0)
          int_b = data.get(i).charAt(j) << ((3-j)*8) | int_b;
        else
          int_b = (Character.getNumericValue(data.get(i).charAt(j)) << ((3-j)*8)) | int_b;
      }
      circuitEvaluator.setWireValue(b[i], int_b);
    }
  }

  public static void main(String[] args) throws Exception {
    SoundexCircuitGenerator generator;

    int INPUT_LEN = 256;
    String dataFile = "/workspace/jsnark/JsnarkCircuitBuilder/src/examples/gadgets/soundex/data.txt";
    String inputA = "CAT";

    if (args.length != 0) {
      if  (args.length == 2) {
        dataFile = args[0];
        if (args[1].length() > INPUT_LEN) {
          throw new IllegalArgumentException("Max argument length of " + INPUT_LEN);
        }
        inputA = args[1];
      } else {
        throw new IllegalArgumentException("Must Have 2 inputs");
      }
    }
    generator = new SoundexCircuitGenerator("soundex example", dataFile, inputA.length(), inputA);


    generator.generateCircuit();
    generator.evalCircuit();
    generator.prepFiles();
    generator.runLibsnark();
    CircuitEvaluator evaluator = generator.getCircuitEvaluator();

    ArrayList<Wire> outputWires = generator.getOutWires();
    // generator.printCircuit();

    System.out.println("********************************************************************************");
    if (args.length == 2) {
      System.out.print("OUTPUT OF CIRCUIT using file " + dataFile + ": Soundex for " + args[1] + ":");
    } else {
      System.out.print("OUTPUT OF CIRCUIT using file " + dataFile + ": Soundex for " + "CAT:");
    }
		for (int i = 0; i < outputWires.size() - 1; i++) {
      Wire out = outputWires.get(i);
      if (i == 0) {
        System.out.print((char)evaluator.getWireValue(out).intValue());
      } else {
        System.out.print(evaluator.getWireValue(out));
      }
		}
    // String res = "";
    // if (evaluator.getWireValue(outputWires.get(5)) == 0) {
    //   res = " not";
    // }
    System.out.println(" Soundex value found " + evaluator.getWireValue(outputWires.get(4)));
    System.out.println("********************************************************************************");
  }
}
