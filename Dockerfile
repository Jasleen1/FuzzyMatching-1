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

ADD precompute/SoundexCircuitGenerator.java /workspace/jsnark/JsnarkCircuitBuilder/src/examples/generators/soundex/SoundexCircuitGenerator.java
ADD precompute/SoundexGadget.java /workspace/jsnark/JsnarkCircuitBuilder/src/examples/gadgets/soundex/SoundexGadget.java
ADD precompute/data.txt /workspace/jsnark/JsnarkCircuitBuilder/src/examples/gadgets/soundex/data.txt
# ADD test/test_precompute.sh /workspace/jsnark/JsnarkCircuitBuilder/test.sh

ADD mimc/MiMCCircuitGenerator.java /workspace/jsnark/JsnarkCircuitBuilder/src/examples/generators/mimc/MiMCCircuitGenerator.java
ADD mimc/MiMCGadget.java /workspace/jsnark/JsnarkCircuitBuilder/src/examples/gadgets/mimc/MiMCGadget.java

RUN mkdir -p bin
RUN javac -d bin -cp /usr/share/java/junit4.jar:/bcprov-jdk15on-159.jar  $(find ./src/* | grep ".java$")

RUN java -cp bin examples.generators.mimc.MiMCCircuitGenerator 234 2
# RUN java -cp bin examples.generators.soundex.SoundexCircuitGenerator /workspace/jsnark/JsnarkCircuitBuilder/src/examples/gadgets/soundex/data.txt CCC
