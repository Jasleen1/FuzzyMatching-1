package examples.generators.jubjub;

import java.util.ArrayList;

import circuit.eval.CircuitEvaluator;
import circuit.structure.CircuitGenerator;
import circuit.structure.Wire;
import circuit.structure.WireArray;

import examples.gadgets.jubjub.JubJubMulGadget;

public class JubJubCircuitGenerator extends CircuitGenerator {

  private Wire x;
  private Wire y;
  private Wire s;

  public JubJubCircuitGenerator(String circuitName) {
    super(circuitName);
  }

  @Override
  protected void buildCircuit() {

    x = createInputWire("x");
    y = createInputWire("y");
    s = createInputWire("s");

    JubJubMulGadget jubjubGadget = new JubJubMulGadget(x, y, s);
    Wire[] result = jubjubGadget.getOutputWires();
    makeOutputArray(result, "output of jubjub");
  }

  @Override
  public void generateSampleInput(CircuitEvaluator circuitEvaluator) {

    circuitEvaluator.setWireValue(x, 1);
    circuitEvaluator.setWireValue(y, 1);
    circuitEvaluator.setWireValue(s, 1);
  }

  public static void main(String[] args) throws Exception {
    JubJubCircuitGenerator generator;
    if (args.length != 0) {
      if  (args.length == 3) {
        generator = new JubJubCircuitGenerator("JubJub Input") {
          Wire inputX;
          Wire inputY;
          Wire inputS;
          @Override
          protected void buildCircuit() {
            inputX = createInputWire("input x");
            inputY = createInputWire("input y");
            inputS = createInputWire("input s");
            Wire[] digest = new JubJubMulGadget(inputX, inputY, inputS, "").getOutputWires();
            makeOutputArray(digest);
          }

          @Override
          public void generateSampleInput(CircuitEvaluator e) {
            e.setWireValue(inputX, Integer.parseInt(args[0].trim()));
            e.setWireValue(inputY , Integer.parseInt(args[1].trim()));
            e.setWireValue(inputS, Integer.parseInt(args[2].trim()));
          }
        };
      } else {
        throw new IllegalArgumentException("Must Have 3 inputs x,y and s");
      }
    } else {
      generator = new JubJubCircuitGenerator("jubjub example");
    }

    generator.generateCircuit();
    // generator.printCircuit();
    generator.evalCircuit();
    generator.prepFiles();
    generator.runLibsnark();
    CircuitEvaluator evaluator = generator.getCircuitEvaluator();

    ArrayList<Wire> outputWires = generator.getOutWires();
    System.out.println("********************************************************************************");
    if (args.length == 1) {
      System.out.print("OUTPUT OF CIRCUIT: JubJub for (" + args[0] + "," + args[1] + ")*"+ args[2]);
    } else {
      System.out.print("OUTPUT OF CIRCUIT: JubJub for " + "(1,1)*1:");
    }
		for (int i = 0; i < outputWires.size(); i++){
      Wire out = outputWires.get(i);
      System.out.print(evaluator.getWireValue(out).intValue());
		}
    System.out.println("********************************************************************************");
  }
}
