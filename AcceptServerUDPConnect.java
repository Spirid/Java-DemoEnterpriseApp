package enterprisejdbcclient;

import static enterprisejdbcclient.ServerUDPConnect.bufSize;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AcceptServerUDPConnect extends Thread {
    
    DatagramSocket dgs;
    boolean working = true;
    static int bufSize = 1024;
    
    public AcceptServerUDPConnect(int ServerPort, InetAddress ServerInetAddress) throws SocketException
    {
        this.dgs = new DatagramSocket(ServerPort, ServerInetAddress);
    }    
    
    @Override
    public void run ()
    {
        byte[] buffer = new byte[bufSize];
        
        while(working)
        {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                dgs.receive(packet);
            } catch (IOException ex) {
                Logger.getLogger(AcceptServerUDPConnect.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                ServerUDPConnect udp = new ServerUDPConnect(packet.getAddress(), packet.getPort());
                udp.start();
            } catch (IOException ex) {
                Logger.getLogger(AcceptServerUDPConnect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        dgs.close();
    }
    
    public synchronized void stopServer()
    {
        working = false;
    }
        
}
