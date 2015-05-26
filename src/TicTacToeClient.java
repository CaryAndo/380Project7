import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class TicTacToeClient {

    public static void main(String[] args) {

        class Listener implements Runnable {

            Socket socket;

            public Listener(Socket socket) {
                this.socket = socket;
            }

            @Override
            public void run() {
                try {
                    ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

                    while (true) {
                        Object obj = is.readObject();
                        if (obj instanceof ErrorMessage) {
                            System.out.println("THERE WAS AN ERROR: " + ((ErrorMessage) obj).getError());
                        } else if (obj instanceof Message) {

                        }
                    }
                } catch (IOException ioe) {
                    //ioe.printStackTrace();
                } catch (ClassNotFoundException cnfe) {
                    cnfe.printStackTrace();
                }
            }
        }

        try {
            Socket socket = new Socket("45.50.5.238", 38007);
            Listener listener = new Listener(socket);
            Thread t = new Thread(listener);
            t.start();

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            ConnectMessage connect = new ConnectMessage("Tobias");
            output.writeObject(connect);



        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
