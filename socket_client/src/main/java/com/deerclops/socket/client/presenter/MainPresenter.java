package com.deerclops.socket.client.presenter;

import com.deerclops.socket.client.view.IMainView;
import com.deerclops.socket.core.mvp.IContract;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainPresenter extends IContract.AbsPresenter<IMainView, Void> {

    private static final int PORT = 3399;
    private ExecutorService mExecutor = Executors.newFixedThreadPool(10);

    public MainPresenter(IMainView view) {
        super(view);
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                initSocket();
            }
        });
    }

    @Override
    protected Void createModel() {
        return null;
    }

    private Socket mClientSocket;

    private void initSocket() {
        try {
            mClientSocket = new Socket("localhost", PORT);
            mExecutor.execute(new RcvMsgRunnable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class RcvMsgRunnable implements Runnable {

        StringBuffer sbMsg = new StringBuffer();
        BufferedReader bufferedReader;

        RcvMsgRunnable() {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String msg;
            try {
                while (true) {
                    msg = bufferedReader.readLine();
                    if (msg != null) {
                        sbMsg.append(msg).append("\n");
                        mView.updateMsgFromClient(sbMsg.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMsg2Server(final String msg) {
        if (mClientSocket != null) {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream())));
                        printWriter.println(msg);
                        printWriter.flush();
                        Logger.i("Client has send a msg just now!!");
//                        mClientSocket.shutdownOutput();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
