package examples.generators.soundex;

import java.util.ArrayList;

import circuit.eval.CircuitEvaluator;
import circuit.structure.CircuitGenerator;
import circuit.structure.Wire;

import examples.gadgets.soundex.SoundexGadget;

public class SoundexCircuitGenerator extends CircuitGenerator {

  private Wire[] a;
  private Wire[] b;
  private int dimension;

  public SoundexCircuitGenerator(String circuitName, int dimension) {
    super(circuitName);
    this.dimension = dimension;
  }

  @Override
  protected void buildCircuit() {

    a = createInputWireArray(dimension, "Input a");
    b = createInputWireArray(dimension, "Input b");

    SoundexGadget soundexGadget = new SoundexGadget(a, b);
    Wire[] result = soundexGadget.getOutputWires();
    makeOutputArray(result, "output of soundex a,b");
  }

  @Override
  public void generateSampleInput(CircuitEvaluator circuitEvaluator) {
    String string_a = "BCIDV";
    String string_b = "ALMRX";
    for (int i = 0; i < dimension; i++) {
      int int_a = string_a.charAt(i);
      int int_b = string_b.charAt(i);
      circuitEvaluator.setWireValue(a[i], int_a);
      circuitEvaluator.setWireValue(b[i], int_b);
    }
  }

  public static void main(String[] args) throws Exception {
    SoundexCircuitGenerator generator;
    if (args.length != 0) {
      if  (args.length == 2) {
        int dimension = Math.max(args[0].length(), args[1].length());
        generator = new SoundexCircuitGenerator("Soundex Input", dimension) {
          Wire[] inputWirea;
          Wire[] inputWireb;

          @Override
          protected void buildCircuit() {
            inputWirea = createInputWireArray(args[0].length());
            inputWireb = createInputWireArray(args[1].length());
            Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
            makeOutputArray(digest);
          }

          @Override
          public void generateSampleInput(CircuitEvaluator e) {
            for (int i = 0; i < args[0].length(); i++){
              int val = args[0].charAt(i);
              if (val < 65 || val > 90) {
                throw new IllegalArgumentException("Must Have all capital letters");
              }
              e.setWireValue(inputWirea[i], val);
            }
            for (int i = 0; i < args[1].length(); i++){
              int val = args[1].charAt(i);
              if (val < 65 || val > 90) {
                throw new IllegalArgumentException("Must Have all capital letters");
              }
              e.setWireValue(inputWireb[i], val);
            }
          }
        };
      } else {
        throw new IllegalArgumentException("Must Have 2 inputs");
      }
    } else {
      generator = new SoundexCircuitGenerator("soundex example", 5);
    }

    generator.generateCircuit();
    // generator.printCircuit();
    generator.evalCircuit();
    generator.prepFiles();
    generator.runLibsnark();
    CircuitEvaluator evaluator = generator.getCircuitEvaluator();

    ArrayList<Wire> outputWires = generator.getOutWires();
    System.out.println("********************************************************************************");
    if (args.length == 2) {
      System.out.print("OUTPUT OF CIRCUIT: Soundex for " + args[0] + ":");
    } else {
      System.out.print("OUTPUT OF CIRCUIT: Soundex for " + "BCIDV:");
    }
		for (int i = 0; i < outputWires.size() - 1; i++){
      Wire out = outputWires.get(i);
      if (i == 0 || i == 4) {
        if (i == 4) {
          if (args.length == 2) {
            System.out.print(" Soundex for " + args[1] + ":");
          } else {
            System.out.print(" Soundex for " + "ALMRX:");
          }
        }
        System.out.print((char)evaluator.getWireValue(out).intValue());
      } else {
        System.out.print(evaluator.getWireValue(out));
      }
		}
    System.out.println(" Soundex are equal: " + evaluator.getWireValue(outputWires.get(8)));
    System.out.println("********************************************************************************");
  }
}
