import Models.KeyEntry;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import apis.thrift.keyValueStore.*;
import org.apache.thrift.TException;

public class Controller implements KeyValueStore.Iface{
    private HashMap<String, KeyEntry> entries;

    Controller() {
        entries = new HashMap<>();
    }

    @Override
    public List<String> Get(String key) {
        if (!entries.containsKey(key)) {
            return Collections.EMPTY_LIST;
        }
        return entries.get(key).get();
    }

    @Override
    public List<String> GetWithTime(String key, long time) {
        if (!entries.containsKey(key)) {
            return Collections.EMPTY_LIST;
        }
        return entries.get(key).get(time);
    }

    @Override
    public boolean Put(String key, String value) {
        if (!entries.containsKey(key)) {
            entries.put(key, new KeyEntry());
        }
        return entries.get(key).put(value);
    }

    @Override
    public boolean Delete(String key) {
        return entries.remove(key) != null;
    }

    @Override
    public boolean DeleteWithValue(String key, String value) {
        return entries.containsKey(key) && entries.get(key).delete(value);
    }

    @Override
    public List<String> Diff(String key, long time1, long time2) throws TException {
        if (time1 >= time2) {
            throw new TException("Invalid arguments");
        }
        if (!entries.containsKey(key)) {
            return Collections.EMPTY_LIST;
        }
        return entries.get(key).diff(time1, time2);
    }
}
