package net.awired.housecream.server.it;


//public class HcwWebSocket implements WebSocket.OnTextMessage {
//
//    public class Message {
//        public String text;
//
//        public Message(String text) {
//            this.text = text;
//        }
//    }
//
//    private Connection connection;
//    private List<Message> messages = Collections.synchronizedList(new ArrayList<Message>());
//    private WebSocketClient client;
//
//    public HcwWebSocket(WebSocketClient client) {
//        this.client = client;
//    }
//
//    public List<Message> getMessages() {
//        return messages;
//    }
//
//    public Connection getConnection() {
//        return connection;
//    }
//
//    @Override
//    public void onClose(int closeCode, String message) {
//        this.connection = null;
//        //System.out.printf( "Closing with %d and message %s%n", closeCode, message );
//    }
//
//    @Override
//    public void onOpen(Connection connection) {
//        this.connection = connection;
//    }
//
//    public void sendMessage(String text) {
//        try {
//            connection.sendMessage(text);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void onMessage(String text) {
//        this.messages.add(new Message(text));
//    }
//
//}
