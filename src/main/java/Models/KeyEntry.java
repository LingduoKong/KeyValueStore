package Models;

import java.util.*;

/**
 * As it is a friends relationship service, assume it will be read heavy.
 * Will force on read performance rather than write
 *
 * KeyEntry class holds status snapshots to track the change.
 * Status snapshots are stored in a red-black tree by timestamp.
 */
public class KeyEntry {
    TreeMap<Long, Snapshot> statusHistory;

    public KeyEntry() {
        statusHistory = new TreeMap<>();
        statusHistory.put(System.nanoTime(), new Snapshot());
    }

    /**
     * time complexity O(lg(n)) where n is the number of total snapshots
     * space complexity O(1)
     */
    public List<String> get() {
        Map.Entry<Long, Snapshot> last = statusHistory.lastEntry();
        return last.getValue().valuesList;
    }

    /**
     * time complexity O(lg(n) + k)
     * space complexity O(k)
     * where n is the number of total snapshots, k is the size of values in the latest snapshot
     */
    public boolean put(String value) {
        Map.Entry<Long, Snapshot> last = statusHistory.lastEntry();
        if (!last.getValue().contains(value)) {
            Snapshot new_snapshot = new Snapshot(last.getValue().valuesList);
            statusHistory.put(System.nanoTime(), new_snapshot);
            return new_snapshot.add(value);
        }
        return false;
    }

    /**
     * time complexity O(lg(n)) where n is the number of total snapshots
     * space complexity O(1)
     */
    public List<String> get(long time) {
        Map.Entry<Long, Snapshot> entry = statusHistory.floorEntry(time);
        return entry == null ? Collections.EMPTY_LIST : entry.getValue().valuesList;
    }

    public boolean delete(String value) {
        Map.Entry<Long, Snapshot> last = statusHistory.lastEntry();
        if (last.getValue().contains(value)) {
            Snapshot new_snapshot = new Snapshot(last.getValue().valuesList);
            statusHistory.put(System.nanoTime(), new_snapshot);
            return new_snapshot.delete(value);
        }
        return false;
    }

    public List<String> diff(long time1, long time2) {
        Map.Entry<Long, Snapshot> entry1 = statusHistory.floorEntry(time1);
        Map.Entry<Long, Snapshot> entry2 = statusHistory.floorEntry(time2);
        if (entry2 == null) {
            return Collections.EMPTY_LIST;
        } else if (entry1 == null) {
            return entry2.getValue().valuesList;
        }
        ArrayList<String> result = new ArrayList<>();
        for (String val : entry2.getValue().valuesList) {
            if (!entry1.getValue().contains(val)) {
                result.add(val);
            }
        }
        return result;
    }
}
