# Build Jsnark Image
- install jsnark
- compile Soundex circuit
- run Soundex test cases

`docker build -t jsnark .`

# Execute Soundex Circuit
- calculate soundex code for words and output if soundex codes match
- words must be in all caps with only A-Z
- output of the circuit will be printed after "OUTPUT OF CIRCUIT:"

`docker run --rm jsnark java -cp bin examples.generators.soundex.SoundexCircuitGenerator <word 1> <word 2>`

example:

`docker run --rm jsnark java -cp bin examples.generators.soundex.SoundexCircuitGenerator CAT COT`

# Run Soundex Tests

`docker run --rm jsnark java -cp bin:/usr/share/java/junit4.jar org.junit.runner.JUnitCore  examples.tests.soundex.Soundex_Test`
