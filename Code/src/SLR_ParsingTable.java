/*
        ===========================================================
        CPCS-302 Project Phase 03 (SLR Parser)
        Mahmued Alardawi    2135209    CS1
        mmalardawi@stu.kau.edu.sa
        ===========================================================
 */

import java.util.*;

public class SLR_ParsingTable {
    HashSet<String> terminals = new HashSet<>();
    HashSet<String> nonTerminals = new HashSet<>();
    HashMap<String, HashMap<String, String>> productions = new HashMap<>();
    HashMap<String, HashMap<String, String>> parseTable = new HashMap<>();

    public SLR_ParsingTable() {
        // Initialize the terminals
        terminals = new HashSet<>(Arrays.asList("a", "b", "c", "ϵ", "$"));

        // Initialize the nonTerminals
        nonTerminals = new HashSet<>(Arrays.asList("S'", "T", "R"));

        // Initialize the productions
        productions.put("0", new HashMap<>() {{put("S'", "T");}});
        productions.put("1", new HashMap<>() {{put("T", "R");}});
        productions.put("2", new HashMap<>() {{put("T", "aTc");}});
        productions.put("3", new HashMap<>() {{put("R", "ϵ");}});
        productions.put("4", new HashMap<>() {{put("R", "bR");}});
    }

    public void constructTable () {
        parseTable.put("0", new HashMap<>() {{
            put("a", "s3");
            put("b", "s4");
            put("c", "r3");
            put("$", "r3");
            put("T", "1");
            put("R", "2");
        }});
        parseTable.put("1", new HashMap<>() {{put("$", "Accept");}});
        parseTable.put("2", new HashMap<>() {{
            put("c", "r1");
            put("$", "r1");
        }});
        parseTable.put("3", new HashMap<>() {{
            put("a", "s3");
            put("b", "s4");
            put("c", "r3");
            put("$", "r3");
            put("T", "5");
            put("R", "2");
        }});
        parseTable.put("4", new HashMap<>() {{
            put("b", "s4");
            put("c", "r3");
            put("$", "r3");
            put("R", "6");
        }});
        parseTable.put("5", new HashMap<>() {{put("c", "s7");}});
        parseTable.put("6", new HashMap<>() {{
            put("c", "r4");
            put("$", "r4");
        }});
        parseTable.put("7", new HashMap<>() {{
            put("c", "r2");
            put("$", "r2");
        }});
    }
}