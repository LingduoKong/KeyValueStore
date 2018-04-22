package Models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Snapshot {
    private HashSet<String> valuesSet;
    ArrayList<String> valuesList;

    Snapshot() {
        valuesList = new ArrayList<>();
        valuesSet = new HashSet<>();
    }

    Snapshot(Collection<String> values) {
        valuesList = new ArrayList<>(values);
        valuesSet = new HashSet<>(values);
    }

    public boolean contains(String value) {
        return valuesSet.contains(value);
    }

    public boolean add(String value) {
        return valuesSet.add(value) && valuesList.add(value);
    }

    public boolean delete(String value) {
        return valuesSet.remove(value) && valuesList.remove(value);
    }
}
