# Predictive Parser LL(1) for Java

This Java program implements a Predictive Parser for LL(1) grammars. The LL(1) parser is constructed based on the given grammar productions, first sets, and follow sets. It allows users to define a grammar, terminals, and test the parser on a provided input string.



## Usage

The Predictive Parser LL(1) is designed to assist in parsing strings based on a given LL(1) grammar. Users can define non-terminals, terminals, and production rules, and the program will generate a parsing table and perform parsing on input strings.
note!
you can only use one char non terminals.
j := epsilon.

## Components

- **PredictiveParserLL1.java**: Main Java class containing the implementation of the LL(1) parser.
- **SampleInput.txt**: An example input file showcasing the format for defining the grammar.

## How to Run

1. Compile the Java program:

    ```bash
    javac PredictiveParserLL1.java
    ```

2. Run the compiled program:

    ```bash
    java PredictiveParserLL1
    ```

3. Follow the on-screen instructions to input non-terminals, terminals, production rules, and test the LL(1) parser.

## Sample Input

The program prompts the user to input the grammar, terminals, and an input string for testing. Here is an example:

```plaintext
Enter the number of nonterminals
2
Enter the symbol of nonterminal
S
is this nonterminal a start? y/n
y
Enter the number of rules for this nonterminal
1
Enter the right hand side of the production rule without spaces
a+b
Enter the first of this rule
a
Enter the length of the follow set for this nonterminal
1
enter the follow for the nonterminal
$
Production has been added!
...
Enter the number of terminals
2
Enter a terminal
a
Enter a terminal
b
...
Enter string to test, one non-terminal each time!
Enter the non-terminal or # to end
S
#
