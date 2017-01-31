package enterprisejdbcclient;

import java.io.Serializable;

public class MyUdpConteiner implements Serializable {

    private String senderId;
    private String addressee;
    private String[] pkg;

    MyUdpConteiner(String _sender, String _addressee, String[] _pkg) {
        senderId = _sender;
        addressee = _addressee;
        pkg = _pkg;
    }

    public String[] getPkg() {
        return pkg;
    }

    public String getAddresId() {
        return addressee;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setPkg(String[] MyPkg){
        this.pkg = MyPkg;
    }

    public void setSenderId(String MySenderId){
        this.senderId = MySenderId;
    }

    public void setAddressee(String MyAddressee){
        this.addressee = MyAddressee;
    }
}
