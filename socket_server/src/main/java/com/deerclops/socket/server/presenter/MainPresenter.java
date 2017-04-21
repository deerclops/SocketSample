package com.deerclops.socket.server.presenter;

import com.deerclops.socket.core.mvp.IContract;
import com.deerclops.socket.server.view.IMainView;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainPresenter extends IContract.AbsPresenter<IMainView, Void> {

    private static final int PORT = 3399;
    private ExecutorService mExecutor = Executors.newFixedThreadPool(10);

    public MainPresenter(IMainView view) {
        super(view);
        mExecutor.execute(new AcceptRunnable());
    }

    private ServerSocket mServerSocket;
    private Socket mSocket;

    @Override
    protected Void createModel() {
        return null;
    }

    class AcceptRunnable implements Runnable {

        AcceptRunnable() {
            try {
                mServerSocket = new ServerSocket(PORT);
                Logger.i("Server socket has been created.");
            } catch (IOException e) {
                e.printStackTrace();
                Logger.e("Server socket failed to be created!");
            }
        }

        @Override
        public void run() {
            if (mServerSocket != null) {
                try {
                    while (true) {
                        mSocket = mServerSocket.accept();
                        Logger.i("Server socket starts to accept sockets from clients.");
                        mExecutor.execute(new RcvMsgRunnable(mSocket));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.e("Server socket failed to accept!");
                }
            }
        }
    }

    class RcvMsgRunnable implements Runnable {

        Socket mSocket;
        BufferedReader bufferedReader;
        StringBuffer sbMsg = new StringBuffer();

        RcvMsgRunnable(Socket socket) {
            mSocket = socket;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Logger.i("Server socket is looping to get message from client...");
            String msg;
            try {
                while (true) {
                    Logger.i("Enter lopping...");
                    msg = bufferedReader.readLine();
                    if (msg != null) {
                        Logger.i("Get one message like this, " + msg);
                        sbMsg.append(msg).append("\n");
                        mView.updateMsgFromClient(sbMsg.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMsg2Client(final String msg) {
        if (mSocket != null) {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
//                        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())));
//                        printWriter.println(msg);
//                        printWriter.flush();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                        bufferedWriter.write(msg.concat("\n"));
                        bufferedWriter.flush();
                        Logger.i("A message from server has been send.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void shutDown() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
