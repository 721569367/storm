package backtype.storm.drpc;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import backtype.storm.generated.DRPCRequest;
import backtype.storm.generated.DistributedRPCInvocations;

public class DRPCInvocationsClient implements DistributedRPCInvocations.Iface {
    private TTransport conn;
    private DistributedRPCInvocations.Client client;
    private String host;
    private int port;    

    public DRPCInvocationsClient(String host, int port) {
        try {
            this.host = host;
            this.port = port;
            connect();
        } catch(TException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void connect() throws TException {
        conn = new TFramedTransport(new TSocket(host, port));
        client = new DistributedRPCInvocations.Client(new TBinaryProtocol(conn));
        conn.open();
    }
    
    public String getHost() {
        return host;
    }
    
    public int getPort() {
        return port;
    }       

    public void result(String id, String result) throws TException {
        try {
            if(client==null) connect();
            client.result(id, result);
        } catch(TException e) {
            client = null;
            throw e;
        }
    }

    public DRPCRequest fetchRequest(String func) throws TException {
        try {
            if(client==null) connect();
            return client.fetchRequest(func);
        } catch(TException e) {
            client = null;
            throw e;
        }
    }    

    public void failRequest(String id) throws TException {
        try {
            if(client==null) connect();
            client.failRequest(id);
        } catch(TException e) {
            client = null;
            throw e;
        }
    }

    public void close() {
        conn.close();
    }
}
