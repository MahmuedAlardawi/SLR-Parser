/*
        ===========================================================
        CPCS-302 Project Phase 03 (SLR Parser)
        Mahmued Alardawi    2135209    CS1
        mmalardawi@stu.kau.edu.sa
        ===========================================================
 */

import java.util.*;
import java.io.*;

public class SLR_Parser {
    public static void main(String[] args) throws IOException {
        // Construct the parsing table
        SLR_ParsingTable table = new SLR_ParsingTable();
        table.constructTable();

        // Display table data
        data(table);

        // Parse inputs from input.txt
        BufferedReader input = new BufferedReader(new FileReader("input.txt"));
        PrintWriter output = new PrintWriter("output.txt");
        parser(table, input, output);

        // Parse inputs from input1.txt
        BufferedReader input1 = new BufferedReader(new FileReader("input1.txt"));
        PrintWriter output1 = new PrintWriter("output1.txt");
        parser(table, input1, output1);

    }

    // Prints Data
    public static void data(SLR_ParsingTable table) {
        System.out.println("Data: -");
        System.out.println("---------------------------------");
        System.out.print("Terminals: ");
        for (String entry : table.terminals) {
            System.out.print(entry + ", ");
        }
        System.out.println("\n");

        System.out.print("Non-terminals: ");
        for (String entry : table.nonTerminals) {
            System.out.print(entry + ", ");
        }
        System.out.println("\n");

        System.out.println("Productions: ");
        for (Map.Entry<String, HashMap<String, String>> row : table.productions.entrySet()) {
            System.out.println(row);
        }
        System.out.println();

        System.out.println("Parse Table:");
        for (Map.Entry<String, HashMap<String, String>> row : table.parseTable.entrySet()) {
            System.out.println(row);
        }
        System.out.println("---------------------------------");
    }

    public static void parser(SLR_ParsingTable table, BufferedReader input, PrintWriter output)
            throws IOException {
        // Output file data storage
        ArrayList<Stack<Object>> stackData = new ArrayList<>();
        ArrayList<ArrayList<String>> inputData = new ArrayList<>();
        ArrayList<StringBuilder> actionData = new ArrayList<>();

        // Initialize the parsing stack with state 0
        Stack<Object> stack = new Stack<>();
        stack.push("0");

        // Read the input line and tokenize it
        String line = input.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line);
        String token = tokenizer.nextToken();

        boolean shift = false;
        boolean reduce = false;
        boolean reduceAction = false;
        boolean accept = true;

        // Initial Stack data storage
        Stack<Object> s0 = new Stack<>();
        s0.addAll(stack);
        stackData.add(s0);

        // Initial Input data mini storage
        String[] stringArray = line.split(" ");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));

        // Initial Input data storage
        ArrayList<String> a0 = new ArrayList<>(arrayList);
        inputData.add(a0);

        while (accept) {
            for (Map.Entry<String, HashMap<String, String>> state : table.parseTable.entrySet()) {
                if (Objects.equals(state.getKey(), stack.peek())) {
                    for (Map.Entry<String, String> entry : state.getValue().entrySet()) {
                        if (Objects.equals(entry.getKey(), token)) {
                            String action = entry.getValue();

                            // Shift action
                            if (Objects.equals(action.charAt(0), 's')) {
                                shift = true;
                                stack.push(token);
                                stack.push(String.valueOf(action.charAt(1)));
                                actionData.add(new StringBuilder(action + " (Shift " +
                                        action.charAt(1) + ")"));
                            }

                            // Reduce action
                            else if (Objects.equals(action.charAt(0), 'r')) {
                                reduce = true;
                                int productionLength = 0;
                                String LHS = "";
                                boolean epsilon = false;
                                String p = "";

                                for (Map.Entry<String, HashMap<String, String>> productionNumber :
                                        table.productions.entrySet()) {
                                    if (Objects.equals(String.valueOf(action.charAt(1)), productionNumber.getKey())) {
                                        for (Map.Entry<String, String> production :
                                                productionNumber.getValue().entrySet()) {
                                            if (Objects.equals(production.getValue(), "ϵ")) {epsilon = true;}
                                            productionLength = production.getValue().length();
                                            LHS = production.getKey();
                                            p = production.getValue();
                                        }
                                    }
                                }

                                if (!epsilon) {
                                    // Pop twice for each symbol in production's RHS
                                    for (int i = 0; i < productionLength * 2; i++) {
                                        stack.pop();
                                    }
                                }

                                for (Map.Entry<String, HashMap<String, String>> state1 : table.parseTable.entrySet()) {
                                    if (Objects.equals(state1.getKey(), stack.peek())) {
                                        for (Map.Entry<String, String> entry1 : state1.getValue().entrySet()) {
                                            if (Objects.equals(entry1.getKey(), LHS)) {
                                                stack.push(LHS);
                                                stack.push(entry1.getValue());
                                                reduceAction = true;
                                            }
                                        }
                                    }
                                    if (reduceAction) {break;}
                                }
                                actionData.add(new StringBuilder(action + " (Reduce by " +
                                        LHS + " -> " + p + ")"));
                            }

                            // Accept action
                            else {
                                accept = false;
                                actionData.add(new StringBuilder("Accept"));
                            }
                        }
                    }
                }
                // Shift to next token
                if (shift) {
                    token = tokenizer.nextToken();
                    shift = false;
                    arrayList.removeFirst();
                    break;
                }
                // Reprocess token
                else if (reduce) {
                    reduce = false;
                    reduceAction = false;
                    break;
                }
            }

            // Adding stack data to storage
            s0 = new Stack<>();
            s0.addAll(stack);
            stackData.add(s0);

            // Adding input data to storage
            a0 = new ArrayList<>(arrayList);
            inputData.add(a0);
        }
        // Remove radiant data
        stackData.removeLast();
        inputData.removeLast();

        // Output parsing results
        output.printf("%-30s", "Stack");
        output.printf("%-30s", "Input");
        output.printf("%-30s", "Action");
        output.println();
        output.println("---------------------------------------------------------------------------------");
        for (int i = 0; i < stackData.size(); i++) {
            output.printf("%-30s", stackData.get(i));
            output.printf("%-30s", inputData.get(i));
            output.printf("%-30s", actionData.get(i));
            output.println();
        }

        input.close();
        output.close();
    }
}