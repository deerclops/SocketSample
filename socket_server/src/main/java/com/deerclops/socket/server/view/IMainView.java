package com.deerclops.socket.server.view;

import com.deerclops.socket.core.mvp.IContract;

public interface IMainView extends IContract.IView{
    void updateMsgFromClient(String msg);
}
