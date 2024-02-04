
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class PredictiveParserLL1 {
    public static int counter = 0;
    private HashMap<String , ArrayList<String> > productions; 
    private HashMap<String , HashSet<String> > first;
    private HashMap<String , HashSet<String> > follow;
    private HashSet<String> nonTerminalsSet;
    private ArrayList<String> terminalsArr;
    private HashMap<String , String > table; // key =  terminal, value = (key = nonT , value= productions ) 
    public static String start = "";
    public PredictiveParserLL1(){
        this.productions = new HashMap<>();
        this.first = new HashMap<>();
        this.follow = new HashMap<>();
        this.nonTerminalsSet = new HashSet<>();
        this.terminalsArr = new ArrayList<>();
        this.table = new HashMap<>();
    }


    // S -> E+E, key = S , value = E+E
    public void InsertProduction(String nonTerminal , ArrayList<String> production ){
        productions.put(nonTerminal, production);
    }


    public void addTerminal(String terminal) {
        HashSet<String> set = new HashSet<>();
        set.add(terminal);
        first.put(terminal, set); // the first for each nonTerminal = the nonterminal itself
    }


    public void addNonTerminal(String nonTerminal ) {
        nonTerminalsSet.add(nonTerminal);
    }

    public void addFirst(String nonTerminal , HashSet<String> firstSet){
        first.put(nonTerminal, firstSet);
    }
    public void addFollow(String nonTerminal , HashSet<String> followSet){
        follow.put(nonTerminal, followSet);
    }
    public void constructFirstSets() {
        System.out.println("construct First Sets");
        for (String nonTerminal : nonTerminalsSet) {
            boolean isRooted = true; // if the cutrrent nonT contains another nonT aka first(S) = first(E)
            while (isRooted) {
                isRooted = false;
                HashSet<String> addrooted = new HashSet<>(); //  holds sets of the recurcion nonT
                HashSet<String> removeRooted = new HashSet<>(); // holds nonT to be removed
                System.out.println(nonTerminal + ":");
                for (String string : first.get(nonTerminal)) {
                    System.out.println(string);
                }  
                for (String elemnt : first.get(nonTerminal)) {

                    if (nonTerminalsSet.contains(elemnt) && !elemnt.equals(nonTerminal)) { // E=E infinet loop
                        addrooted.addAll(first.get(elemnt));
                        removeRooted.add(elemnt);
                        isRooted = true;  
                    }
                }
                first.get(nonTerminal).addAll(addrooted);
                first.get(nonTerminal).removeAll(removeRooted);
            }
            System.out.println("---------------------------");
            
        }
        
    }
    
    public void constructLL1Table(int numberNonterminals , int numberTerminals){
        
        
        ArrayList<String> nonTArr = new ArrayList<>();
        for (String elem : nonTerminalsSet) {
            nonTArr.add(elem);
        }
        for (String nonT : nonTArr) {
            HashSet<String> firstSet = first.get(nonT);
            if (firstSet.contains("j")) {
                first.get(nonT).remove("j");
                for (String term : follow.get(nonT)) {
                    table.put(nonT + ", " + term, "j");
                }
            }
            for (String terminal : terminalsArr) {
                String key = nonT + ", " + terminal;

                if (firstSet.contains(terminal) && !terminal.equals("j")) {
                    for (String product : productions.get(nonT)) {
                        if (product.contains(terminal)) {
                            table.put(key, product);
                        }
                    }
                }
                if (firstSet.contains(terminal) && !terminal.equals("j") && table.get(key) == null) {
                    for (String product : productions.get(nonT)) {
                        if (!product.contains("j")) {
                            table.put(key, product);
                        }
                    }
                }
                
            }
        }
        System.out.println("LL1 table: ");
        for (String key : table.keySet()) {
            System.out.println("[" + key + "] : " + table.get(key));
        }
        System.out.println("---------------------------");
    }
    
    public void parsingLL1(Stack<String> input){
        // input =  input.append("$") ;
        input.push("$");
        Stack<String> inputStack = new Stack<>();
        while (!input.isEmpty()) {
            inputStack.push(input.pop());
        }
        
        


        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push(start); // start symbol

        while (!stack.isEmpty()) {
            counter++;
            String peek = stack.peek();
            String inputChar = inputStack.peek();
            String cross = peek + ", " + inputChar ;
            ArrayList<String> nonTProductions = productions.get(peek);
            String rhs = table.get(cross);
            // System.out.println(cross);
            printStack(stack);
            printInputStack(inputStack);
            
            if (peek.equals("j")) {
                stack.pop();
                System.out.println("------");
                continue;
            }
            
            if (peek.equals(inputChar)) {
                System.out.println("Match!");
                stack.pop();
                inputStack.pop();
            }

            else if ( nonTProductions.contains(rhs)) {

                stack.pop();
                System.out.println("action: [" + cross +"] -> "+  rhs);
                String[] temp = rhs.split("");
                ArrayList<String> newRule = new ArrayList<>();
                for (int i = 0; i < temp.length; i++) {
                    String s = temp[i];
                        if (s.equals("'")) {
                            s = temp[i-1] + "'";
                            newRule.remove(newRule.size() - 1);
                            newRule.add(s); 
                        }
                        else
                        newRule.add(s);
                } 
                for (int i = newRule.size() - 1; i >= 0; i--) {
                            stack.push(newRule.get(i));
                }

               
            }
            
            else{
                break;
            }
            System.out.println("------");
        }
        if (stack.isEmpty()) {
            System.out.println("acc!");
        }
        else{
            System.out.println("rej!");
        }
    }      

    public static <T> void printStack(Stack<T> stack) {
        

        
        List<T> reversedList = Collections.list(stack.elements());
        Collections.reverse(reversedList);

        System.out.print("step number: " + counter + ", stack: ");
        for (T element : reversedList) {
            System.out.print(element+ "");
        }
        System.out.println();
    }
    public static <T> void printInputStack(Stack<T> stack) {
        

        
        List<T> reversedList = Collections.list(stack.elements());
        Collections.reverse(reversedList);

        System.out.print("Input stack: ");
        for (T element : reversedList) {
            System.out.print(element);
        }
        System.out.println();
    }
    public static void main(String[] args) {

        PredictiveParserLL1 parserLL1 = new PredictiveParserLL1();
        Scanner in = new Scanner(System.in);
        boolean isStart = true;

        System.out.println("Enter the number of nonterminals");
        int nonterminalsNumber = in.nextInt();
        int t = nonterminalsNumber;

        while (t-- > 0) {
            HashSet<String> firstSet = new HashSet<>();
            HashSet<String> followSet = new HashSet<>();
            ArrayList<String> productionsArr = new ArrayList<>();
            System.out.println("Enter the symbol of nonterminal");

            String nonterminal = in.next();
            parserLL1.addNonTerminal(nonterminal);
            // check if the input is start
            if (isStart) {
                System.out.println("is this nonterminal a start? y/n");
                String s = in.next();
                if (s.equals("y")) {
                    isStart = false;
                    start = nonterminal;
                }

            }


            System.out.println("Enter the number of rules for this nonterminal");
            System.out.println("if the rule contains an or operator aka'S -> aB | b' please enter 2 not 1!");
            int rulesNumber = in.nextInt();
            
            while (rulesNumber-- > 0) {
                System.out.println("Enter the right hand side of production rule without spaces");
                String rule = in.next();
                productionsArr.add(rule);
                System.out.println("Enter the first of this rule");
                String element = in.next();
                firstSet.add(element);
            }


            System.out.println("Enter the length of follow set for this nonterminal");
            int followLen = in.nextInt();
            while (followLen-- > 0) {
                System.out.println("enter the follow for the nonterminal");
                String element = in.next();
                followSet.add(element);
            }


            parserLL1.addFirst(nonterminal, firstSet);
            parserLL1.InsertProduction(nonterminal, productionsArr);
            parserLL1.addFollow(nonterminal, followSet);
            System.out.println("Production has been added!");
        }



        /* terminals */
        System.out.println("Enter the number of terminals");
        int terminalsNumber = in.nextInt();
         t = terminalsNumber;
        while (t-- > 0) {
            System.out.println("Enter a terminal");
            String terminal = in.next();
            parserLL1.addTerminal(terminal);
            parserLL1.terminalsArr.add(terminal);
        }

        parserLL1.constructFirstSets();

        parserLL1.constructLL1Table(nonterminalsNumber, terminalsNumber);


        /* parsing */
        System.out.println("Enter string to test, one non terimenal each time!");
        Stack<String> inpuStack =  new Stack<>();
        while (true) {
            System.out.println("Enter the non terimenal or # to end");
            String tempString = in.next();
            if (tempString.equals("#")) {
                break;
            }
            inpuStack.push(tempString);
        }
        parserLL1.parsingLL1(inpuStack);
        in.close();
    }



    
}
