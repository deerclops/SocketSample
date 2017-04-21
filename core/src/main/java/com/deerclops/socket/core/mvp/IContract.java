package com.deerclops.socket.core.mvp;

public interface IContract {
    interface IView{
        void initView();
        void initListeners();
    }

    abstract class AbsPresenter<VIEW extends IView,MODEL>{

        protected VIEW mView;
        protected MODEL mModel;

        public AbsPresenter(VIEW view){
            mView = view;
            mModel = createModel();
        }

        protected abstract MODEL createModel();
    }
}
