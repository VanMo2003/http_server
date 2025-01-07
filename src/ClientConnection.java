import java.util.Objects;

public class ClientConnection {
    private String address;
    private int port;
    private String protocol;

    public ClientConnection(String address, int port, String protocol) {
        this.address = address;
        this.port = port;
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientConnection that)) return false;
        return port == that.port && address.equals(that.address) && protocol.equals(that.protocol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port, protocol);
    }
}
