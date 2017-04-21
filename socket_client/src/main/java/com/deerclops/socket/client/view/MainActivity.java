package com.deerclops.socket.client.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.deerclops.socket.client.R;
import com.deerclops.socket.client.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements IMainView{

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);
        initView();
        initListeners();
    }

//    private Button btnAccept;
    private Button btnRcvMsg;
    private Button btnSendMsg;
    private TextView tvMsg;
    private EditText etContent;

    @Override
    public void initView() {
//        btnAccept = findViewById(R.id.btn_accept);
        btnRcvMsg = findViewById(R.id.btn_rcv_msg);
        btnSendMsg = findViewById(R.id.btn_send_msg);
        tvMsg = findViewById(R.id.tv_msgs);
        etContent = findViewById(R.id.et_content);
    }

    @Override
    public void initListeners() {
//        btnAccept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        btnRcvMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.rcvMsgFromServer();
//            }
//        });
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.sendMsg2Server(etContent.getText().toString());
                etContent.setText("");
            }
        });
    }

    @Override
    public void updateMsgFromClient(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMsg.setText(msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
