package host_config;

public class Create {
    static String address;
    static int port;
    static String username;
    public static Host WhiteBoard;

    public static void main(String[] args){
        if (args.length == 3){
            try {
                address = args[0];
                port = Integer.parseInt(args[1]);
                username = args[2];
            } catch (Exception e){
                System.out.println("Please give correct format of command");
                System.exit(1);
            }
        } else {
            System.out.println("Please give correct format of command");
            System.exit(1);
        }
        WhiteBoard = new Host(username);
        Server.launch(port,username);
    }
}
