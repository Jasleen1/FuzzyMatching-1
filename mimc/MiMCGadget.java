package examples.gadgets.mimc;

import circuit.operations.Gadget;
import circuit.structure.Wire;
import circuit.structure.CircuitGenerator;

public class MiMCGadget extends Gadget {

	/** Input plaintext */
	private Wire plaintext;

	/** Ouput of circuit */
	private Wire output;

	/** K **/
	private Wire k;

	/** C */
	private Wire[] c;

	/** r */
	private int r;

	public MiMCGadget(Wire plaintext, Wire k, String... desc) {
		super(desc);
		this.plaintext = plaintext;
		this.k = k;

		this.c = generator.createConstantWireArray(new long[]{70, 80}, "c");
		this.r = 2;
		buildCircuit();
	}

	private void buildCircuit() {
		output = plaintext.add(k);
		output = output.mul(output).mul(output);
		for (int i = 0; i < r; i++) {
			output = output.add(c[i]).add(k);
		}
		output = output.add(k);
	}

	@Override
	public Wire[] getOutputWires() {
		return new Wire[] { output };
	}
}
