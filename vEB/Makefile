JAVAC = javac
JAVA = java
SRC = $(wildcard *.java)
CLASSES = $(SRC:.java=.class)
MAIN = Main
INPUT ?= entrada.txt

build: $(CLASSES)

%.class: %.java
	$(JAVAC) $<

run: build
	$(JAVA) $(MAIN) $(INPUT)

clean:
	rm -f *.class

.PHONY: build run clean