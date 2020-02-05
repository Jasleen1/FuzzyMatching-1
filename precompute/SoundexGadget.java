package examples.gadgets.soundex;

import circuit.operations.Gadget;
import circuit.structure.Wire;
import circuit.structure.CircuitGenerator;

public class SoundexGadget extends Gadget {

	/** First input word */
	private Wire[] a;

	/** List of letters that map to 1 */
	private Wire[] one_soundex_map;
	/** List of letters that map to 2 */
	private Wire[] two_soundex_map;
	/** List of letters that map to 3 */
	private Wire[] three_soundex_map;
	/** List of letters that map to 4 */
	private Wire[] four_soundex_map;
	/** List of letters that map to 5 */
	private Wire[] five_soundex_map;
	/** List of letters that map to 6 */
	private Wire[] six_soundex_map;

	/** Wire with value 0 */
	private Wire zero_val;
	/** Wire with value 1 */
	private Wire one_val;
	/** Wire with value 2 */
	private Wire two_val;
	/** Wire with value 3 */
	private Wire three_val;
	/** Wire with value 4 */
	private Wire four_val;
	/** Wire with value 5 */
	private Wire five_val;
	/** Wire with value 6 */
	private Wire six_val;

	/** Wire with letter H */
	private Wire h_val;
	/** Wire with letter W */
	private Wire w_val;

	/** Ouput of circuit */
	private Wire[] circuit_output;


	public SoundexGadget(Wire[] a, String... desc) {
		super(desc);

		//initalize class variables
		zero_val = generator.getZeroWire();
		one_val = generator.createConstantWire(1, "Constant Wire with value 1");
		two_val = generator.createConstantWire(2, "Constant Wire with value 2");
		three_val = generator.createConstantWire(3, "Constant Wire with value 3");
		four_val = generator.createConstantWire(4, "Constant Wire with value 4");
		five_val = generator.createConstantWire(5, "Constant Wire with value 5");
		six_val = generator.createConstantWire(6, "Constant Wire with value 6");

		h_val = generator.createConstantWire(72, "Constant Wire with value H");
		w_val = generator.createConstantWire(87, "Constant Wire with value W");

		//Contain ASCII encoding for Capital letters
		//Mappings from rule 2. https://en.wikipedia.org/wiki/Soundex#American_Soundex
		one_soundex_map = generator.createConstantWireArray(new long[]{66, 70, 80, 86}, "Letters Mapping to 1: B, F, P, V");
		two_soundex_map = generator.createConstantWireArray(new long[]{67, 71, 74, 75, 81, 83, 88, 90}, "Letters Mapping to 2: C, G, J, K, Q, S, X, Z");
		three_soundex_map = generator.createConstantWireArray(new long[]{68, 84}, "Letters Mapping to 3: D, T");
		four_soundex_map = generator.createConstantWireArray(new long[]{76}, "Letters Mapping to 4: L");
		five_soundex_map = generator.createConstantWireArray(new long[]{77, 78}, "Letters Mapping to 5: M, N");
		six_soundex_map = generator.createConstantWireArray(new long[]{82}, "Letters Mapping to 6: R");

		this.a = a;
		buildCircuit();
	}

	private void buildCircuit() {
		Wire[] a_output = transform(a);
		circuit_output = new Wire[4];

		//store soundex codes in output
		for (int i = 0; i < 4; i++) {
			circuit_output[i] = a_output[i];
		}
	}

	/**
	 * Takes in Wire array representing a word and returns Wire array
	 * with the Soundex code
	 * @param input Wire array representation of input string
	 * @return Wire array representation of soundex code
	 */
	private Wire[] transform(Wire[] input) {
		//all soundex codes are len 4
		//rule 4 from https://en.wikipedia.org/wiki/Soundex#American_Soundex
		Wire[] output = new Wire[4];

		//first letter always kept
		//rule 1 from https://en.wikipedia.org/wiki/Soundex#American_Soundex
		output[0] = input[0];

		Wire j_wire_index = zero_val; //highest index of the input word that has been translated
		generator.addDebugInstruction(j_wire_index, "j_wire_index");
		for (int i = 1; i < 4; i++) {

			//default value is 0
			//rule 1 and 4 from https://en.wikipedia.org/wiki/Soundex#American_Soundex
			output[i] = zero_val;
			Wire output_i_not_set = generator.getOneWire();
			generator.addDebugInstruction(output_i_not_set, "output_i_not_set: output[" + i + "] not set");
			Wire increase_j_wire_index = generator.getOneWire(); // can we increase j_wire_index
			generator.addDebugInstruction(increase_j_wire_index, "can we increase_j_wire_index");
			for (int j = 1; j < input.length; j++) {
				Wire j_wire = generator.createConstantWire((long)j, "j_wire");
				generator.addDebugInstruction(input, "input[" + j + "]");
				generator.addDebugInstruction(j_wire, "j_wire");
				generator.addDebugInstruction(j_wire_index, "j_wire_index");
				Wire letter_at_j_not_used = j_wire.isGreaterThan(j_wire_index, 128, "j_wire >= j_wire_index"); //TODO what should bitWidth be
				generator.addDebugInstruction(letter_at_j_not_used, "letter_at_j_not_used: j_wire >= j_wire_index");

				Wire letter_at_j_is_not_duplicate = input[j-1].isEqualTo(input[j]).isEqualTo(zero_val);
				int j1 = j-1;
				generator.addDebugInstruction(letter_at_j_is_not_duplicate, "letter_at_j_is_not_duplicate input[" + j1 + "] != input[" + j + "]");

				Wire output_i_min_1_is_one = output[i-1].isEqualTo(one_val)
						.or(output[i-1].isEqualTo(one_soundex_map[0])
								.or(output[i-1].isEqualTo(one_soundex_map[1]))
								.or(output[i-1].isEqualTo(one_soundex_map[2]))
								.or(output[i-1].isEqualTo(one_soundex_map[3])));
				int i1 = i-1;
				generator.addDebugInstruction(output_i_min_1_is_one, "output_i_min_1_is_one: output[" +i1+ "] == 1");
				//checking for two letters with the same number separated by 'H' or 'W'
				//rule 3. from https://en.wikipedia.org/wiki/Soundex#American_Soundex
				Wire no_one_hw_one = output_i_min_1_is_one.and(input[j-1].isEqualTo(h_val).or(input[j-1].isEqualTo(w_val))).isEqualTo(zero_val);
				generator.addDebugInstruction(no_one_hw_one, "no_one_hw_one no patter found");
				Wire update_output_cond_one = no_one_hw_one.and(letter_at_j_not_used).and(letter_at_j_is_not_duplicate);
				output[i] = eval_for_one(output[i], output_i_not_set, update_output_cond_one, input[j], i, j);
				output_i_not_set = output[i].isEqualTo(0);
				generator.addDebugInstruction(output_i_not_set, "output_i_not_set: output[" + i + "] not set");

				Wire output_i_min_1_is_two = output[i-1].isEqualTo(two_val)
						.or(output[i-1].isEqualTo(two_soundex_map[0])
								.or(output[i-1].isEqualTo(two_soundex_map[1]))
								.or(output[i-1].isEqualTo(two_soundex_map[2]))
								.or(output[i-1].isEqualTo(two_soundex_map[3]))
								.or(output[i-1].isEqualTo(two_soundex_map[4]))
								.or(output[i-1].isEqualTo(two_soundex_map[5]))
								.or(output[i-1].isEqualTo(two_soundex_map[6]))
								.or(output[i-1].isEqualTo(two_soundex_map[7])));
				generator.addDebugInstruction(output_i_min_1_is_two, "output_i_min_1_is_two: output[" + i1 + "] == 2");
				Wire no_two_hw_two = output_i_min_1_is_two.and(input[j-1].isEqualTo(h_val).or(input[j-1].isEqualTo(w_val))).isEqualTo(zero_val);
				generator.addDebugInstruction(no_two_hw_two, "no_two_hw_two no patter found");
				Wire update_output_cond_two = no_two_hw_two.and(letter_at_j_not_used).and(letter_at_j_is_not_duplicate);
				output[i] = eval_for_two(output[i], output_i_not_set, update_output_cond_two, input[j], i, j);
				output_i_not_set = output[i].isEqualTo(0);
				generator.addDebugInstruction(output_i_not_set, "output_i_not_set: output[" + i + "] not set");

				Wire output_i_min_1_is_three = output[i-1].isEqualTo(three_val)
						.or(output[i-1].isEqualTo(three_soundex_map[0]).or(output[i-1].isEqualTo(three_soundex_map[1])));
				generator.addDebugInstruction(output_i_min_1_is_three, "output_i_min_1_is_three: output[" + i1 + "] == 3");
				Wire no_three_hw_three = output_i_min_1_is_three.and(input[j-1].isEqualTo(h_val).or(input[j-1].isEqualTo(w_val))).isEqualTo(zero_val);
				generator.addDebugInstruction(no_three_hw_three, "no_three_hw_three no patter found");
				Wire update_output_cond_three = no_three_hw_three.and(letter_at_j_not_used).and(letter_at_j_is_not_duplicate);
				output[i] = eval_for_three(output[i], output_i_not_set, update_output_cond_three, input[j], i, j);
				output_i_not_set = output[i].isEqualTo(0);
				generator.addDebugInstruction(output_i_not_set, "output_i_not_set: output[" + i + "] not set");

				Wire output_i_min_1_is_four = output[i-1].isEqualTo(four_val).or(output[i-1].isEqualTo(four_soundex_map[0]));
				generator.addDebugInstruction(output_i_min_1_is_four, "output_i_min_1_is_four: output[" + i1 + "] == 4");
				Wire no_four_hw_four = output_i_min_1_is_four.and(input[j-1].isEqualTo(h_val).or(input[j-1].isEqualTo(w_val))).isEqualTo(zero_val);
				generator.addDebugInstruction(no_four_hw_four, "no_four_hw_four no patter found");
				Wire update_output_cond_four = no_four_hw_four.and(letter_at_j_not_used).and(letter_at_j_is_not_duplicate);
				output[i] = eval_for_four(output[i], output_i_not_set, update_output_cond_four, input[j], i, j);
				output_i_not_set = output[i].isEqualTo(0);
				generator.addDebugInstruction(output_i_not_set, "output_i_not_set: output[" + i + "] not set");

				Wire output_i_min_1_is_five = output[i-1].isEqualTo(five_val)
						.or(output[i-1].isEqualTo(five_soundex_map[0]).or(output[i-1].isEqualTo(five_soundex_map[1])));
				generator.addDebugInstruction(output_i_min_1_is_five, "output_i_min_1_is_five: output[" + i1 + "] == 5");
				Wire no_five_hw_five = output_i_min_1_is_five.and(input[j-1].isEqualTo(h_val).or(input[j-1].isEqualTo(w_val))).isEqualTo(zero_val);
				generator.addDebugInstruction(no_five_hw_five, "no_five_hw_five no patter found");
				Wire update_output_cond_five = no_five_hw_five.and(letter_at_j_not_used).and(letter_at_j_is_not_duplicate);
				output[i] = eval_for_five(output[i], output_i_not_set, update_output_cond_five, input[j], i, j);
				output_i_not_set = output[i].isEqualTo(0);
				generator.addDebugInstruction(output_i_not_set, "output_i_not_set: output[" + i + "] not set");

				Wire output_i_min_1_is_six = output[i-1].isEqualTo(six_val).or(output[i-1].isEqualTo(six_soundex_map[0]));
				generator.addDebugInstruction(output_i_min_1_is_six, "output_i_min_1_is_six: output[" + i1 + "] == 6");
				Wire no_six_hw_six = output_i_min_1_is_six.and(input[j-1].isEqualTo(h_val).or(input[j-1].isEqualTo(w_val))).isEqualTo(zero_val);
				generator.addDebugInstruction(no_six_hw_six, "no_six_hw_six no patter found");
				Wire update_output_cond_six = no_six_hw_six.and(letter_at_j_not_used).and(letter_at_j_is_not_duplicate);
				output[i] = eval_for_six(output[i], output_i_not_set, update_output_cond_six, input[j], i, j);
				output_i_not_set = output[i].isEqualTo(0);
				generator.addDebugInstruction(output_i_not_set, "output_i_not_set: output[" + i + "] not set");

				Wire will_set_j_wire_index = increase_j_wire_index.and(letter_at_j_not_used).and(j_wire, "increase j_wire_index " + j);
				generator.addDebugInstruction(will_set_j_wire_index, "will_set_j_wire_index");
				Wire tmp = will_set_j_wire_index.isEqualTo(0).and(j_wire_index);
				generator.addDebugInstruction(tmp, "tmp");
				j_wire_index = will_set_j_wire_index.orBitwise(tmp, 128);
				generator.addDebugInstruction(j_wire_index, "j_wire_index: increase_j_wire_index -> " + j);
				increase_j_wire_index = output_i_not_set;
				generator.addDebugInstruction(increase_j_wire_index, "increase_j_wire_index: output_i_not_set");
			}
			generator.addDebugInstruction(output[i], "---------------------------------------output[" + i + "]");

		}
		return output;
	}

	/**
	 * Calculates the soundex mapping for a character in the input
	 * 	if mapping should be 1 it returns 1
	 * 	else returns origin value of output
	 * @param output Current value of the output
	 * @param output_i_not_set If the output is set to anything
	 * @param update_output_cond Combination of various conditions that must be true for output to be able to be updated
	 * @param input input character to map
	 * @param i index in output (used for debug statements)
	 * @param j index in input (used for debug statements)
	 * @return Wire that the output should be set to
	 */
	private Wire eval_for_one(Wire output, Wire output_i_not_set, Wire update_output_cond, Wire input, int i, int j){
		Wire letter_at_j_is_B = one_soundex_map[0].isEqualTo(input, "input[" + j + "] == 66 (B)");
		generator.addDebugInstruction(letter_at_j_is_B,  "input[" + j + "] == 66 (B)");
		Wire letter_at_j_is_F = one_soundex_map[1].isEqualTo(input, "input[" + j + "] == 70 (F)");
		generator.addDebugInstruction(letter_at_j_is_F, "input[" + j + "] == 70 (F)");
		Wire letter_at_j_is_P = one_soundex_map[2].isEqualTo(input, "input[" + j + "] == 80 (P)");
		generator.addDebugInstruction(letter_at_j_is_P, "input[" + j + "] == 80 (P)");
		Wire letter_at_j_is_V = one_soundex_map[3].isEqualTo(input, "input[" + j + "] == 86 (V)");
		generator.addDebugInstruction(letter_at_j_is_V, "input[" + j + "] == 86 (V)");

		//check if input mapped to B, F, P, or V
		Wire letter_at_j_is_1 = letter_at_j_is_B.or(letter_at_j_is_F).or(letter_at_j_is_P).or(letter_at_j_is_V);
		generator.addDebugInstruction(letter_at_j_is_1, "letter_at_j_is_1: input[" + j + "] = B | F | P | V");

		//check if we can update output
		Wire  will_set_output_i_to_1 = output_i_not_set.and(letter_at_j_is_1).and(update_output_cond).and(one_val, "Set output[" + i + "] to 1");
		generator.addDebugInstruction(will_set_output_i_to_1, "will_set_output_i_to_1");

		output = will_set_output_i_to_1.orBitwise(output, 128);
		generator.addDebugInstruction(output, "output[" + i + "] = output_i_not_set & letter_at_j_is_1 & update_output_cond -> 1");
		return output;
	}

	/**
	 * Calculates the soundex mapping for a character in the input
	 * 	if mapping should be 2 it returns 2
	 * 	else returns origin value of output
	 * @param output Current value of the output
	 * @param output_i_not_set If the output is set to anything
	 * @param update_output_cond Combination of various conditions that must be true for output to be able to be updated
	 * @param input input character to map
	 * @param i index in output (used for debug statements)
	 * @param j index in input (used for debug statements)
	 * @return Wire that the output should be set to
	 */
	private Wire eval_for_two(Wire output, Wire output_i_not_set, Wire update_output_cond, Wire input, int i, int j) {
		Wire letter_at_j_is_C = two_soundex_map[0].isEqualTo(input, "input[" + j + "] == 67 (C)");
		generator.addDebugInstruction(letter_at_j_is_C, "input[" + j + "] == 67 (C)");
		Wire letter_at_j_is_G = two_soundex_map[1].isEqualTo(input, "input[" + j + "] == 71 (G)");
		generator.addDebugInstruction(letter_at_j_is_G, "input[" + j + "] == 71 (G)");
		Wire letter_at_j_is_J = two_soundex_map[2].isEqualTo(input, "input[" + j + "] == 74 (J)");
		generator.addDebugInstruction(letter_at_j_is_J, "input[" + j + "] == 74 (J)");
		Wire letter_at_j_is_K = two_soundex_map[3].isEqualTo(input, "input[" + j + "] == 75 (K)");
		generator.addDebugInstruction(letter_at_j_is_K, "input[" + j + "] == 75 (K)");
		Wire letter_at_j_is_Q = two_soundex_map[4].isEqualTo(input, "input[" + j + "] == 81 (Q)");
		generator.addDebugInstruction(letter_at_j_is_Q, "input[" + j + "] == 81 (Q)");
		Wire letter_at_j_is_S = two_soundex_map[5].isEqualTo(input, "input[" + j + "] == 83 (S)");
		generator.addDebugInstruction(letter_at_j_is_S, "input[" + j + "] == 83 (S)");
		Wire letter_at_j_is_X = two_soundex_map[6].isEqualTo(input, "input[" + j + "] == 88 (X)");
		generator.addDebugInstruction(letter_at_j_is_X, "input[" + j + "] == 88 (X)");
		Wire letter_at_j_is_Z = two_soundex_map[7].isEqualTo(input, "input[" + j + "] == 90 (Z)");
		generator.addDebugInstruction(letter_at_j_is_Z, "input[" + j + "] == 90 (Z)");

		Wire letter_at_j_is_2 = letter_at_j_is_C
				.or(letter_at_j_is_G)
				.or(letter_at_j_is_J)
				.or(letter_at_j_is_K)
				.or(letter_at_j_is_Q)
				.or(letter_at_j_is_S)
				.or(letter_at_j_is_X)
				.or(letter_at_j_is_Z);
		generator.addDebugInstruction(letter_at_j_is_2, "letter_at_j_is_2: input[" + j + "] = C | G | J | K | Q | S | X | Z");
		Wire will_set_output_i_to_2 = output_i_not_set.and(letter_at_j_is_2).and(update_output_cond).and(two_val, "Set output[" + i + "] to 2");
		generator.addDebugInstruction(will_set_output_i_to_2, "will_set_output_i_to_2");

		output = will_set_output_i_to_2.orBitwise(output, 128);
		generator.addDebugInstruction(output, "output[" + i + "] = output_i_not_set & letter_at_j_is_2 & update_output_cond -> 2");
		return output;
	}

	/**
	 * Calculates the soundex mapping for a character in the input
	 * 	if mapping should be 3 it returns 3
	 * 	else returns origin value of output
	 * @param output Current value of the output
	 * @param output_i_not_set If the output is set to anything
	 * @param update_output_cond Combination of various conditions that must be true for output to be able to be updated
	 * @param input input character to map
	 * @param i index in output (used for debug statements)
	 * @param j index in input (used for debug statements)
	 * @return Wire that the output should be set to
	 */
	private Wire eval_for_three(Wire output, Wire output_i_not_set, Wire update_output_cond, Wire input, int i, int j) {
		Wire letter_at_j_is_D = three_soundex_map[0].isEqualTo(input, "input[" + j + "] == 68 (D)");
		generator.addDebugInstruction(letter_at_j_is_D, "input[" + j + "] == 68 (D)");
		Wire letter_at_j_is_T = three_soundex_map[1].isEqualTo(input, "input[" + j + "] == 84 (T)");
		generator.addDebugInstruction(letter_at_j_is_T, "input[" + j + "] == 84 (T)");

		Wire letter_at_j_is_3 = letter_at_j_is_D.or(letter_at_j_is_T);
		generator.addDebugInstruction(letter_at_j_is_3, "letter_at_j_is_3: input[" + j + "] = D | T");
		Wire will_set_output_i_to_3 = output_i_not_set.and(letter_at_j_is_3).and(update_output_cond).and(three_val, "Set output[" + i + "] to 3");
		generator.addDebugInstruction(will_set_output_i_to_3, "will_set_output_i_to_3");

		output = will_set_output_i_to_3.orBitwise(output, 128);
		generator.addDebugInstruction(output, "output[" + i + "] = output_i_not_set & letter_at_j_is_3 & update_output_cond -> 3");

		return output;
	}

	/**
	 * Calculates the soundex mapping for a character in the input
	 * 	if mapping should be 4 it returns 4
	 * 	else returns origin value of output
	 * @param output Current value of the output
	 * @param output_i_not_set If the output is set to anything
	 * @param update_output_cond Combination of various conditions that must be true for output to be able to be updated
	 * @param input input character to map
	 * @param i index in output (used for debug statements)
	 * @param j index in input (used for debug statements)
	 * @return Wire that the output should be set to
	 */
	private Wire eval_for_four(Wire output, Wire output_i_not_set, Wire update_output_cond, Wire input, int i, int j) {
		Wire letter_at_j_is_L = four_soundex_map[0].isEqualTo(input, "input[" + j + "] == 76 (L)");
		generator.addDebugInstruction(letter_at_j_is_L, "input[" + j + "] == 76 (L)");

		Wire letter_at_j_is_4 = letter_at_j_is_L;
		generator.addDebugInstruction(letter_at_j_is_4, "letter_at_j_is_4: input[" + j + "] = L");
		Wire will_set_output_i_to_4 = output_i_not_set.and(letter_at_j_is_4).and(update_output_cond).and(four_val, "Set output[" + i + "] to 4");
		generator.addDebugInstruction(will_set_output_i_to_4, "will_set_output_i_to_4");

		output = will_set_output_i_to_4.orBitwise(output, 128);
		generator.addDebugInstruction(output, "output[" + i + "] = output_i_not_set & letter_at_j_is_4 & update_output_cond -> 4");

		return output;
	}

	/**
	 * Calculates the soundex mapping for a character in the input
	 * 	if mapping should be 5 it returns 5
	 * 	else returns origin value of output
	 * @param output Current value of the output
	 * @param output_i_not_set If the output is set to anything
	 * @param update_output_cond Combination of various conditions that must be true for output to be able to be updated
	 * @param input input character to map
	 * @param i index in output (used for debug statements)
	 * @param j index in input (used for debug statements)
	 * @return Wire that the output should be set to
	 */
	private Wire eval_for_five(Wire output, Wire output_i_not_set, Wire update_output_cond, Wire input, int i, int j) {
		Wire letter_at_j_is_M = five_soundex_map[0].isEqualTo(input, "input[" + j + "] == 77 (M)");
		generator.addDebugInstruction(letter_at_j_is_M, "input[" + j + "] == 77 (M)");
		Wire letter_at_j_is_N = five_soundex_map[1].isEqualTo(input, "input[" + j + "] == 78 (N)");
		generator.addDebugInstruction(letter_at_j_is_N, "input[" + j + "] == 78 (N)");

		Wire letter_at_j_is_5 = letter_at_j_is_M.or(letter_at_j_is_N);
		generator.addDebugInstruction(letter_at_j_is_5, "letter_at_j_is_3: input[" + j + "] = M | N");
		Wire will_set_output_i_to_5 = output_i_not_set.and(letter_at_j_is_5).and(update_output_cond).and(five_val, "Set output[" + i + "] to 5");
		generator.addDebugInstruction(will_set_output_i_to_5, "will_set_output_i_to_5");

		output = will_set_output_i_to_5.orBitwise(output, 128);
		generator.addDebugInstruction(output, "output[" + i + "] = output_i_not_set & letter_at_j_is_5 & update_output_cond -> 5");

		return output;
	}

	/**
	 * Calculates the soundex mapping for a character in the input
	 * 	if mapping should be 6 it returns 6
	 * 	else returns origin value of output
	 * @param output Current value of the output
	 * @param output_i_not_set If the output is set to anything
	 * @param update_output_cond Combination of various conditions that must be true for output to be able to be updated
	 * @param input input character to map
	 * @param i index in output (used for debug statements)
	 * @param j index in input (used for debug statements)
	 * @return Wire that the output should be set to
	 */
	private Wire eval_for_six(Wire output, Wire output_i_not_set, Wire update_output_cond, Wire input, int i, int j) {
		Wire letter_at_j_is_R = six_soundex_map[0].isEqualTo(input, "input[" + j + "] == 82 (R)");
		generator.addDebugInstruction(letter_at_j_is_R, "input[" + j + "] == 82 (R)");

		Wire letter_at_j_is_6 = letter_at_j_is_R;
		generator.addDebugInstruction(letter_at_j_is_6, "letter_at_j_is_6: input[" + j + "] = R");
		Wire will_set_output_i_to_6 = output_i_not_set.and(letter_at_j_is_6).and(update_output_cond).and(six_val, "Set output[" + i + "] to 6");
		generator.addDebugInstruction(will_set_output_i_to_6, "will_set_output_i_to_6");

		output = will_set_output_i_to_6.orBitwise(output, 128);
		generator.addDebugInstruction(output, "output[" + i + "] = output_i_not_set & letter_at_j_is_6 & update_output_cond -> 6");

		return output;
	}

	@Override
	public Wire[] getOutputWires() {
		return circuit_output;
	}
}
