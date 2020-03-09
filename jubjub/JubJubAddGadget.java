package examples.gadgets.jubjub;

import circuit.operations.Gadget;
import circuit.structure.Wire;
import circuit.structure.CircuitGenerator;

public class JubJubAddGadget extends Gadget {

	/** x coordinate of first point */
	private Wire x1;

	/** y coordinate of first point */
	private Wire y1;

	/** x coordinate of second point */
	private Wire x2;

	/** y coordinate of second point */
	private Wire y2;

	/** (-10240/10241) **/
	private Wire d;

	/** output of circuit [x,y] **/
	private Wire[] output;

	public JubJubAddGadget(Wire x1, Wire y1, Wire x2, Wire y2, String... desc) {
		super(desc);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.d = generator.createConstantWire(-10240).mul(generator.createConstantWire(10241).invBits(256));
		buildCircuit();
	}

	private void buildCircuit() {
		output = new Wire[2];
		Wire x_prod = x1.mul(x2);
		Wire y_prod = y1.mul(y2);

		Wire d_prod = d.mul(x1).mul(x2).mul(y1).mul(y2);

		Wire x1y2 = x1.mul(y2);
		Wire y1x2 = x2.mul(y1);

		// x
		output[0] = x1y2.add(y1x2).mul(generator.getOneWire().invBits(256).add(d_prod));
		// y
		output[1] = y_prod.add(x_prod).mul(generator.getOneWire().invBits(256).sub(d_prod));
	}


	@Override
	public Wire[] getOutputWires() {
		return new Wire[] { output[0],  output[1]};
	}
}
