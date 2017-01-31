/*
If UDP accept. 
accepting new clients.
It creates an instance of the class that handles requests from clients.
The client is identified by a port number with which it works.
 */
package enterprisejdbcclient;

import EnterpriseJDBCServer.DBSessionBeanRemote;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;


public class AppServer
        implements DBSessionBeanRemote
{

    @EJB
    private static DBSessionBeanRemote dBSessionBean;
    private boolean isUp = true; 

    private DatagramSocket socket;
    private DatagramPacket packet;
    private final int sendPort = 16001; 
    private final int receivePort = 16000;  
    private final int bufSize = 1024;   

    static private final String INET_ADDRESS = "localhost";

    @Override
    public String[] getHistory(String name) {
        return dBSessionBean.getHistory(name);
    }

    @Override
    public String[] getHistory(int code) {
        return dBSessionBean.getHistory(code);
    }

    @Override
    public boolean login(String user, String password) {
        return dBSessionBean.login(user, password);
    }

    @Override
    public boolean logout() {
        return dBSessionBean.logout();
    }

    public boolean getUpStatus() {
        return isUp;
    }

    public void CloseUDP() {
        socket.close();
    }

    public void StartUdp() {
        try {
            AcceptServerUDPConnect udpLisoner = new AcceptServerUDPConnect(receivePort, InetAddress.getByName(INET_ADDRESS));
            udpLisoner.start();
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(AppServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        
        AppServer appServer = new AppServer();
        appServer.StartUdp();
        

    }
}
