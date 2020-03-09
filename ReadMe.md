# Build Jsnark Image
- install jsnark
- compile Soundex circuit
- run Soundex test cases

`docker build -t jsnark .`

# Soundex
Phoenetic encoding algorithm for English pronuncation of names.
https://en.wikipedia.org/wiki/Soundex#American_Soundex

## Execute Soundex Circuit
- calculate soundex code for words and output if soundex codes match
- words must be in all caps with only A-Z
- output of the circuit will be printed after "OUTPUT OF CIRCUIT:"

`docker run --rm jsnark java -cp bin examples.generators.soundex.SoundexCircuitGenerator <datafile path> <word 2>`

example:

`docker run --rm jsnark java -cp bin examples.generators.soundex.SoundexCircuitGenerator data.txt COT`

## Execute Using JSnark interface
`docker run --rm jsnark ./../libsnark/build/libsnark/jsnark_interface/run_ppzksnark soundex.arith soundex.in `
`docker run --rm jsnark ./../libsnark/build/libsnark/jsnark_interface/run_ppzksnark "MiMC Input.arith" "MiMC Input.in" `

## Run Soundex Tests

`docker run --rm jsnark java -cp bin:/usr/share/java/junit4.jar org.junit.runner.JUnitCore  examples.tests.soundex.Soundex_Test`

`docker run --rm jsnark sh test.sh`

## Soundex Code
- [Circuit Generator](soundex/SoundexCircuitGenerator.java)
  - Defines number or inputs and dimensions of inputs
  - Define how inputs are encoded
- [Gadget](soundex/SoundexGadget.java)
  - Holds all of the logic for circuit and outputs
- [Tests](soundex/Soundex_Test.java)
  - Holds all test cases for circuit
