import receiver.Receiver;
import receiver.ReceiverImpl;

public class ReceiverMain {

    public static void main(final String[] args) {
        Receiver receiver = new ReceiverImpl();
        receiver.startRecv();
    }

}
