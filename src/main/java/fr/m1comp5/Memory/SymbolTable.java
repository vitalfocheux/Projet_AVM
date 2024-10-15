package fr.m1comp5.Memory;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Object> table = new HashMap<>();

    public void put(String name, Object value) {
        table.put(name, value);
    }

    public Object get(String name) {
        return table.get(name);
    }

    public Map<String, Object> getTable() {
        return this.table;
    }
}
