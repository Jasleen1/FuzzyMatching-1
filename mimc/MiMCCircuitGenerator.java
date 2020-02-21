package examples.generators.mimc;

import java.util.ArrayList;

import circuit.eval.CircuitEvaluator;
import circuit.structure.CircuitGenerator;
import circuit.structure.Wire;

import examples.gadgets.mimc.MiMCGadget;

public class MiMCCircuitGenerator extends CircuitGenerator {

  private Wire plaintext;
  private Wire k;

  public MiMCCircuitGenerator(String circuitName, int dimension) {
    super(circuitName);
  }

  @Override
  protected void buildCircuit() {

    plaintext = createInputWire("plaintext");
    k = createInputWire("k");

    MiMCGadget mimcGadget = new MiMCGadget(plaintext, k);
    Wire[] result = mimcGadget.getOutputWires();
    makeOutputArray(result, "output of mimc plaintext");
  }

  @Override
  public void generateSampleInput(CircuitEvaluator circuitEvaluator) {
    int ex_plaintext = 123;
    int ex_k = 1;
    circuitEvaluator.setWireValue(plaintext, ex_plaintext);
    circuitEvaluator.setWireValue(k, ex_k);
  }

  public static void main(String[] args) throws Exception {
    MiMCCircuitGenerator generator;
    if (args.length != 0) {
      if  (args.length == 2) {
        int dimension = args[0].length();
        generator = new MiMCCircuitGenerator("MiMC Input", dimension) {
          Wire inputWire;
          Wire kWire;
          @Override
          protected void buildCircuit() {
            inputWire = createInputWire("input plaintext");
            kWire = createInputWire("input k");
            Wire[] digest = new MiMCGadget(inputWire, kWire, "").getOutputWires();
            makeOutputArray(digest);
          }

          @Override
          public void generateSampleInput(CircuitEvaluator e) {
            e.setWireValue(inputWire, Integer.parseInt(args[0].trim()));
            e.setWireValue(kWire, Integer.parseInt(args[1].trim()));
          }
        };
      } else {
        throw new IllegalArgumentException("Must Have 2 inputs plaintext and k");
      }
    } else {
      generator = new MiMCCircuitGenerator("mimc example", 5);
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
      System.out.print("OUTPUT OF CIRCUIT: MiMC for " + args[0] + ":");
    } else {
      System.out.print("OUTPUT OF CIRCUIT: MiMC for " + "x = 123, k = 1:");
    }
		for (int i = 0; i < outputWires.size(); i++){
      Wire out = outputWires.get(i);
      System.out.print(evaluator.getWireValue(out).intValue());
		}
    System.out.println("********************************************************************************");
  }
}
