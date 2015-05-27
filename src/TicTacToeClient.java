import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * TicTacToeClient
 * Project 7
 *
 * @author Cary Anderson
 * */
public class TicTacToeClient {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("45.50.5.238", 38007);

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            ConnectMessage connect = new ConnectMessage("Cary");
            output.writeObject(connect);

            Scanner kb = new Scanner(System.in);

            while (true) {
                System.out.println("1. Start new game\n2. Exit the program\nSelect> ");

                int selection = kb.nextInt();
                if (selection == 1) {
                    CommandMessage cmd = new CommandMessage(CommandMessage.Command.NEW_GAME);
                    output.writeObject(cmd);
                    break;
                } else if (selection == 2) {
                    output.writeObject(new CommandMessage(CommandMessage.Command.EXIT));
                    System.out.println("Exiting, Goodbye!");
                    System.exit(0);
                } else {
                    System.out.println("Invalid selection.");
                }
            }

            while (true) {
                Object obj = input.readObject();
                if (obj instanceof BoardMessage) {
                    BoardMessage boardMessage = (BoardMessage) obj;
                    printBoard(boardMessage);

                    if (boardMessage.getStatus() == BoardMessage.Status.IN_PROGRESS) {
                        while (true) {
                            System.out.println("1. Make a Move\n2. Give up\nSelect>");
                            int choice = kb.nextInt();

                            if (choice == 1) {
                                System.out.print("Select Row: (1, 2, or 3): ");
                                byte row = (byte) (kb.nextInt() - 1);
                                System.out.print("Select Column: (1, 2, or 3): ");
                                byte col = (byte) (kb.nextInt() - 1);
                                output.writeObject(new MoveMessage(row, col));
                                break;
                            } else if (choice == 2) {
                                output.writeObject(new CommandMessage(CommandMessage.Command.SURRENDER));
                                break;
                            } else {
                                System.out.println("Invalid selection!");
                            }
                        }
                    } else if (boardMessage.getStatus() == BoardMessage.Status.PLAYER1_VICTORY) {
                        System.out.println("You Win!");
                        break;
                    } else if (boardMessage.getStatus() == BoardMessage.Status.PLAYER2_VICTORY) {
                        System.out.println("Your Opponent Wins!");
                        break;
                    } else if (boardMessage.getStatus() == BoardMessage.Status.PLAYER1_SURRENDER) {
                        System.out.println("You have Surrendered. DUN DUN DUNNNNNN");
                        break;
                    } else if (boardMessage.getStatus() == BoardMessage.Status.PLAYER2_SURRENDER) {
                        System.out.println("The computer Surrendered!");
                        break;
                    } else if (boardMessage.getStatus() == BoardMessage.Status.STALEMATE) {
                        System.out.println("Stalemate!");
                        break;
                    } else {
                        System.out.println("Error something bad happened!");
                        break;
                    }
                } else if (obj instanceof ErrorMessage) {
                    ErrorMessage error = (ErrorMessage) obj;
                    System.out.println(error.getError());
                    System.exit(0);
                }
            }
        } catch (IOException|ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void printBoard(BoardMessage b) {
        for (int i = 0; i < b.getBoard().length; i++) {
            for (int j = 0; j < b.getBoard()[i].length; j++) {
                System.out.print(" | ");
                switch (b.getBoard()[i][j]) {
                    case 0:
                        System.out.print(" _ ");
                        break;
                    case 1:
                        System.out.print(" X ");
                        break;
                    case 2:
                        System.out.print(" O ");
                        break;
                }
                System.out.print(" | ");
            }
            System.out.println("");
        }
    }
}
