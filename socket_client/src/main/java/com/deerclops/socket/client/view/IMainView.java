package com.deerclops.socket.client.view;

import com.deerclops.socket.core.mvp.IContract;

public interface IMainView extends IContract.IView{
    void updateMsgFromClient(String msg);
}
