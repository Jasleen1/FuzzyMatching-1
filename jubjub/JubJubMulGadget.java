package examples.gadgets.jubjub;

import circuit.operations.Gadget;
import circuit.structure.Wire;
import circuit.structure.WireArray;
import circuit.structure.CircuitGenerator;
import examples.gadgets.jubjub.JubJubAddGadget;

public class JubJubMulGadget extends Gadget {

	/** x coordinate of point */
	private Wire x;

	/** y coordinate of point */
	private Wire y;

	/** scalar */
	private WireArray s;

	/** -(10240/10241) **/
	private Wire d;

	/** output of circuit [x,y] **/
	private Wire[] output;

	private int bitwidth = 256;

	//https://en.wikipedia.org/wiki/Elliptic_curve_point_multiplication
	public JubJubMulGadget(Wire x, Wire y, Wire s, String... desc) {
		super(desc);
		this.x = x;
		this.y = y;
		this.s = s.getBitWires(256, "");
		// this.d = generator.createConstantWire(-(10240/10241));
		buildCircuit();
	}

	private void buildCircuit() {
		output = new Wire[2];
		Wire[] N = new Wire[]{x, y};

		Wire[] Q = new Wire[]{generator.getZeroWire(), generator.getZeroWire()};

		for (int i = 0; i < bitwidth; i++) {
			Wire bit_at_i_is_1 = s.get(i).and(generator.getOneWire());
			Wire[] add_result = new JubJubAddGadget(N[0], N[1], Q[0], Q[1], "").getOutputWires();
			Wire[] Q_value = new Wire[]{
				bit_at_i_is_1.and(add_result[0]),
				bit_at_i_is_1.and(add_result[1])
			};
			Wire[] tmp = new Wire[]{
				Q_value[0].isEqualTo(0).and(Q[0]),
				Q_value[1].isEqualTo(0).and(Q[1]),
			};
			Q = new Wire[]{
				Q_value[0].orBitwise(tmp[0],bitwidth),
				Q_value[1].orBitwise(tmp[1],bitwidth)
			};
			N = new JubJubAddGadget(N[0], N[1], N[0], N[1], "").getOutputWires();
		}
		output = Q;
	}


	@Override
	public Wire[] getOutputWires() {
		return new Wire[] { output[0], output[1] };
	}
}
