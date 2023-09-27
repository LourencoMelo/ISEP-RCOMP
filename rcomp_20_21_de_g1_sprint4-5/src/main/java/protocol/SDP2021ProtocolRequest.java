package protocol;

public abstract class SDP2021ProtocolRequest {

    /**
     * Client request
     */
    protected final byte[] request;

    /**
     * Constructor to save the request
     * @param request
     */
    protected SDP2021ProtocolRequest(byte[] request) {
        this.request = request;
    }

    /**
     * Function execute
     * @return bytes
     */
    public abstract byte[] execute();

    /**
     * Returns the type of the message
     * @return the name of the type of the message
     */
    public abstract String messageType();

}