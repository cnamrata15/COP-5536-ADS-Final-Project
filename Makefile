JFLAGS = -g
JC = javac

.SUFFIXES: .java .class


.java.class:
		$(JC) $(JFLAGS) $*.java
SSPCLASSES = \
		Graph.java \
		FibonacciHeap.java \
		Dijkstra.java \
		ssp.java \
		BinaryTrie.java \
		routing.java \


default: classes

classes: $(SSPCLASSES:.java=.class)

clean:
		$(RM) -v *.class
		@- echo "Cleaned"

