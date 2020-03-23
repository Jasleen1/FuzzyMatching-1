package examples.tests.soundex;

import junit.framework.TestCase;

import org.junit.Test;

import util.Util;
import circuit.config.Config;
import circuit.eval.CircuitEvaluator;
import circuit.structure.CircuitGenerator;
import circuit.structure.Wire;
import examples.gadgets.soundex.SoundexGadget;
import java.math.BigInteger;
import java.util.ArrayList;

public class Soundex_Test extends TestCase {

	@Test
	public void testCase1() {
		//Same code

		String inputStra = "BFPV";
    String inputStrb = "BVBF";

		CircuitGenerator generator = new CircuitGenerator("Same codes test: BFPV BVBF") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(66));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(66));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(1));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase2() {
		//Different code

		String inputStra = "BFPV";
    String inputStrb = "BVXF";

		CircuitGenerator generator = new CircuitGenerator("Different codes test: BFPV BVXF") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(66));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(66));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(2));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(1));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(0));
	}

	@Test
	public void testCase3() {
		//Vowel test 1 and short word test

		String inputStra = "CAT";
    String inputStrb = "COT";

		CircuitGenerator generator = new CircuitGenerator("Vowel test 1 and short word test: CAT COT") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(3));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(3));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(0));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase4() {
		//Vowel test 2  and short word test

		String inputStra = "CATI";
    String inputStrb = "COT";

		CircuitGenerator generator = new CircuitGenerator("Vowel test 2  and short word test: CATI COT") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(3));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(3));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(0));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase5() {
		//Long word test 1

		String inputStra = "CATHERINES";
    String inputStrb = "CATHERINEZ";

		CircuitGenerator generator = new CircuitGenerator("Long word test 1: CATHERINES CATHERINEZ") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(3));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(6));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(5));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(3));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(6));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(5));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase6() {
		//Long word test 2

		String inputStra = "QWERTYUIOPQWERTYUIOP";
    String inputStrb = "QWERTYUIOP";

		CircuitGenerator generator = new CircuitGenerator("Long word test 2: QWERTYUIOPQWERTYUIOP QWERTYUIOP") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(81));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(6));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(3));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(81));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(6));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(3));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(1));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase7() {
		//Duplicate letters test 1

		String inputStra = "APPLE";
    String inputStrb = "APPPLE";

		CircuitGenerator generator = new CircuitGenerator("Duplicate letters test 1: APPLE APPPLE") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(65));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(4));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(65));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(4));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(0));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase8() {
		//Duplicate letters test 2

		String inputStra = "APPLE";
    String inputStrb = "APLE";

		CircuitGenerator generator = new CircuitGenerator("Duplicate letters test 2: APPLE APLE") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(65));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(4));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(65));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(4));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(0));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase9() {
		//Duplicate letters test 3

		String inputStra = "PFLE";
    String inputStrb = "PPFLE";

		CircuitGenerator generator = new CircuitGenerator("Duplicate letters test 3: PFLE PPFLE") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(80));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(4));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(80));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(4));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(0));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase10() {
		//H seperators

		String inputStra = "CVHF";
    String inputStrb = "CV";

		CircuitGenerator generator = new CircuitGenerator("H seperators CVHF CV") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(0));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase11() {
		//W seperators

		String inputStra = "CSWZ";
    String inputStrb = "CG";

		CircuitGenerator generator = new CircuitGenerator("W seperators: CSWZ CG") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(2));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(2));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(0));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase12() {
		//Vowel seperators

		String inputStra = "CVAF";
    String inputStrb = "CVF";

		CircuitGenerator generator = new CircuitGenerator("Vowel seperators: CVAF CVF") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(0));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase13() {
		//H seperators first letter

		String inputStra = "DHTP";
    String inputStrb = "DP";

		CircuitGenerator generator = new CircuitGenerator("H seperators first letter: DHTP DP") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(68));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(68));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(0));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(0));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase14() {
		//seperators 2

		String inputStra = "CLHLMWNRHR";
    String inputStrb = "CLNR";

		CircuitGenerator generator = new CircuitGenerator("Seperators 2: CLHLMWNRHR CLNR") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(4));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(5));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(6));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(67));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(4));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(5));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(6));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(1));
	}

	@Test
	public void testCase15() {
		//Test all letters 1

		String inputStra = "ZABCD";
    String inputStrb = "YEFGHIJ";

		CircuitGenerator generator = new CircuitGenerator("Test all letters 1: AABCD AEFGHIJ") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(90));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(2));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(3));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(89));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(2));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(2));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(0));
	}

	@Test
	public void testCase16() {
		//Test all letters 2

		String inputStra = "XKLM";
    String inputStrb = "WNOPQ";

		CircuitGenerator generator = new CircuitGenerator("Test all letters 2: XKLM WNOPQ") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(88));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(2));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(4));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(5));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(87));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(5));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(2));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(0));
	}

	@Test
	public void testCase17() {
		//Test all letters 3

		String inputStra = "VRST";
    String inputStrb = "GUVWXYZ";

		CircuitGenerator generator = new CircuitGenerator("Test all letters 3: VRST GUVWXYZ") {

			Wire[] inputWirea;
			Wire[] inputWireb;

			@Override
			protected void buildCircuit() {
				inputWirea = createInputWireArray(inputStra.length());
        inputWireb = createInputWireArray(inputStrb.length());
				Wire[] digest = new SoundexGadget(inputWirea, inputWireb, "").getOutputWires();
				makeOutputArray(digest);
			}

			@Override
			public void generateSampleInput(CircuitEvaluator e) {
				for (int i = 0; i < inputStra.length(); i++){
					int val =  inputStra.charAt(i);
					e.setWireValue(inputWirea[i], val);
				}
				for (int i = 0; i < inputStrb.length(); i++){
					int val =  inputStrb.charAt(i);
					e.setWireValue(inputWireb[i], val);
				}
			}
		};

		generator.generateCircuit();
		CircuitEvaluator evaluator = new CircuitEvaluator(generator);
		generator.generateSampleInput(evaluator);
		evaluator.evaluate();
		// generator.evalCircuit();

		ArrayList<Wire> outputWires = generator.getOutWires();
		for (Wire o: outputWires){
			System.out.println(evaluator.getWireValue(o) + " ");
		}

		assertEquals(evaluator.getWireValue(outputWires.get(0)), BigInteger.valueOf(86));
		assertEquals(evaluator.getWireValue(outputWires.get(1)), BigInteger.valueOf(6));
		assertEquals(evaluator.getWireValue(outputWires.get(2)), BigInteger.valueOf(2));
		assertEquals(evaluator.getWireValue(outputWires.get(3)), BigInteger.valueOf(3));
		assertEquals(evaluator.getWireValue(outputWires.get(4)), BigInteger.valueOf(71));
		assertEquals(evaluator.getWireValue(outputWires.get(5)), BigInteger.valueOf(1));
		assertEquals(evaluator.getWireValue(outputWires.get(6)), BigInteger.valueOf(2));
		assertEquals(evaluator.getWireValue(outputWires.get(7)), BigInteger.valueOf(2));

		assertEquals(evaluator.getWireValue(outputWires.get(8)), BigInteger.valueOf(0));
	}

}
