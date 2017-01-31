package enterprisejdbcclient;

import EnterpriseJDBCServer.DBSessionBeanRemote;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ServerUDPConnect extends Thread {

    DatagramSocket dgs = null;
    final InetAddress ClientInetAddress;
    final int ClientPort;
    static int bufSize = 1024;

    
    private static DBSessionBeanRemote lookupDBSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (DBSessionBeanRemote) c.lookup("java:comp/env/DBSessionBean");
        } catch (NamingException ne) {

            throw new RuntimeException(ne);
        }
    }

    private static DBSessionBeanRemote dBSessionBean = lookupDBSessionBeanRemote();

    public ServerUDPConnect(InetAddress ClientInetAddress, int ClientPort) throws SocketException, IOException {

        this.ClientInetAddress = ClientInetAddress;
        this.ClientPort = ClientPort;

        this.dgs = new DatagramSocket();
        String response = "Port:" + this.dgs.getLocalPort() + ";";

        byte[] buffer = response.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        packet.setAddress(this.ClientInetAddress);
        packet.setPort(this.ClientPort);
        dgs.send(packet);
    }

    @Override
    public void run() {
        byte[] buffer = new byte[bufSize];

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                dgs.receive(packet);
                String queryText = new String(packet.getData());
                QueryClass q = new QueryClass(queryText);
                buffer = new byte[bufSize];
                
                byte[] output = "Error:1;Description:NotValidQuery;".getBytes();
                if (q.Ok) {
                    q.executeQuery(dBSessionBean);
                    output = q.getResult().getBytes(StandardCharsets.UTF_8);
                }

                packet = new DatagramPacket(output, output.length);
                packet.setAddress(ClientInetAddress);
                packet.setPort(ClientPort);
                dgs.send(packet);

                if ("logout".equals(q.getFunction())) {
                    dgs.close();
                    break;
                }

            } catch (IOException ex) {
                Logger.getLogger(ServerUDPConnect.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
