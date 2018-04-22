import apis.thrift.keyValueStore.*;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class KVStoreClient {

    public static void main(String [] args) {

        try {
            TTransport transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            KeyValueStore.Client client = new KeyValueStore.Client(protocol);

            perform(client);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static void perform(KeyValueStore.Client client) throws TException {
        client.Put("A", "c");
        long time1 = System.nanoTime();
        client.Put("B", "d");
        client.Put("A", "e");
        long time2 = System.nanoTime();
        System.out.println(client.Get("A").toString());
        System.out.println(client.GetWithTime("A", time1).toString());
        System.out.println(client.GetWithTime("A", time2).toString());
    }
}
