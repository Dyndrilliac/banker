JFLAGS = -g -cp ".:JAMA-1.0.3.jar"
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		BankersAlgorithm.java
		

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class

execute: default
		java -cp ".:JAMA-1.0.3.jar" BankersAlgorithm