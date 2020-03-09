FROM ubuntu
RUN apt-get update
RUN apt-get install -y software-properties-common wget git
#libsnark deps
# RUN apt-get install -y build-essential cmake git libprocps4-dev libgmp3-dev python-markdown libboost-all-dev libssl-dev
RUN apt-get install -y build-essential cmake git libgmp3-dev libprocps-dev python-markdown libboost-all-dev libssl-dev
#install java
RUN apt-get install -y openjdk-8-jdk junit4
#instal bouncycastle
RUN wget https://www.bouncycastle.org/download/bcprov-jdk15on-159.jar
WORKDIR /workspace
#clone repo
RUN git clone --recursive https://github.com/akosba/jsnark.git
WORKDIR /workspace/jsnark/libsnark
RUN git submodule init && git submodule update
RUN mkdir build && cd build && cmake ..
WORKDIR /workspace/jsnark/libsnark/build
RUN make
WORKDIR /workspace/jsnark/JsnarkCircuitBuilder
ADD soundex/SoundexCircuitGenerator.java /workspace/jsnark/JsnarkCircuitBuilder/src/examples/generators/soundex/SoundexCircuitGenerator.java
ADD soundex/SoundexGadget.java /workspace/jsnark/JsnarkCircuitBuilder/src/examples/gadgets/soundex/SoundexGadget.java
ADD soundex/Soundex_Test.java /workspace/jsnark/JsnarkCircuitBuilder/src/examples/tests/soundex/Soundex_Test.java

ADD jubjub/JubJubCircuitGenerator.java /workspace/jsnark/JsnarkCircuitBuilder/src/examples/generators/jubjub/JubJubCircuitGenerator.java
ADD jubjub/JubJubAddGadget.java /workspace/jsnark/JsnarkCircuitBuilder/src/examples/gadgets/jubjub/JubJubAddGadget.java
ADD jubjub/JubJubMulGadget.java /workspace/jsnark/JsnarkCircuitBuilder/src/examples/gadgets/jubjub/JubJubMulGadget.java

RUN mkdir -p bin
RUN javac -d bin -cp /usr/share/java/junit4.jar:/bcprov-jdk15on-159.jar  $(find ./src/* | grep ".java$")
RUN java -cp bin examples.generators.soundex.SoundexCircuitGenerator
RUN java -cp bin:/usr/share/java/junit4.jar org.junit.runner.JUnitCore  examples.tests.soundex.Soundex_Test
# RUN java -cp bin examples.generators.soundex.SoundexCircuitGenerator CAT COT
