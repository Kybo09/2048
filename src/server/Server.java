package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Server {

    private static ArrayList<Room> rooms = new ArrayList<Room>();
    public static void main(String[] args) throws IOException, InterruptedException {
        startServer();
    }

    public static void startServer() throws IOException, InterruptedException{
        DatagramSocket datagramSocket = new DatagramSocket(300);
        byte buf[] = new byte[1024];
        System.out.println("Server started !");
        while(true){
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
            datagramSocket.receive(datagramPacket);
            String data = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            System.out.println("Server receive : " + data);
            if(data.contains("roomslist")){
                String answer = "roomslist,";
                if(rooms.size() > 0){
                    for(Room r : rooms){
                        if(r.canJoin()){
                            answer += r.getNum() + ",";
                        }
                    }
                    answer = answer.substring(0,answer.length() - 1);
                }else{
                    answer += "None";
                }
                byte[] ucast = answer.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packetSend = new DatagramPacket(ucast, ucast.length, datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(packetSend);
            }
            if(data.contains("createroom")){
                Room room = new Room();
                rooms.add(room);
                String answer = "roomslist,";
                if(rooms.size() > 0){
                    for(Room r : rooms){
                        if(r.canJoin()){
                            answer += r.getNum() + ",";
                        }
                    }
                    answer = answer.substring(0,answer.length() - 1);
                }else{
                    answer += "None";
                }
                byte[] ucast = answer.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packetSend = new DatagramPacket(ucast, ucast.length, datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(packetSend);
            }
            if(data.contains("joinroom")){
                String[] msgContent = data.split(":");
                int roomNum = Integer.parseInt(msgContent[1]);
                String pseudo = msgContent[2];
                for(Room r : rooms){
                    if(r.getNum() == roomNum){
                        r.addPlayer(datagramPacket.getAddress(), datagramPacket.getPort(), pseudo);
                        if(r.getNbPlayer() == 1){
                            r.start();
                        }
                    }
                }
                System.out.println(roomNum);
                System.out.println("Room num : " + roomNum);
            }
        }
    }
}
