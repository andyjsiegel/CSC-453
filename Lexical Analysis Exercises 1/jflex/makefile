# Install:
#   > sudo port install jflex

all: 
	jflex grammar.flex
	javac Scanner.java
	java -cp :.: Scanner input.txt
