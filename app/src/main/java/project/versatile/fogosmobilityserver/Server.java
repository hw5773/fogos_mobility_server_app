package project.versatile.fogosmobilityserver;

import android.net.Network;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Server {
    MainActivity activity;
    ServerSocket serverSocket;
    String message = "";
    int port = 8080;
    int bytes;

    public Server(MainActivity activity, int port) {
        this.activity = activity;
        this.port = port;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return port;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.getStackTraceString(e);
            }
        }
    }

    private class SocketServerThread extends Thread {
        int count = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(port);

                while (true) {
                    Socket socket = serverSocket.accept();
                    count++;
                    message += "Accept from " + socket.getInetAddress() + ":" + socket.getPort() + "\n";
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.textView1.setText(message);
                            bytes = Integer.parseInt(activity.editText.getText().toString());
                        }
                    });

                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(socket, count);
                    socketServerReplyThread.start();
                }
            } catch (IOException e) {
                Log.getStackTraceString(e);
            }
        }
    }

    private class SocketServerReplyThread extends Thread {
        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            String msgReply = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();
            } catch (IOException e) {
                Log.getStackTraceString(e);
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.textView2.setText(bytes + " 바이트 전송을 완수하였습니다.");
                }
            });
        }
    }

    public String getIpAddress() {
        String ip = "";

        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();

                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.getStackTraceString(e);
        }

        return ip;
    }
}
