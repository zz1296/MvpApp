package com.dl7.myapp.module.beautylist;

import com.dl7.myapp.api.RetrofitService;
import com.dl7.myapp.api.bean.BeautyPhotoBean;
import com.dl7.myapp.module.base.IBasePresenter;
import com.dl7.myapp.module.base.ILoadDataView;
import com.dl7.myapp.views.EmptyLayout;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by long on 2016/9/5.
 * 美图 Presenter
 */
public class BeautyListPresenter implements IBasePresenter {

    private ILoadDataView mView;


    public BeautyListPresenter(ILoadDataView view) {
        this.mView = view;
    }


    @Override
    public void getData() {
        // 因为网易这个原接口参数一大堆，我只传了部分参数，返回的数据会出现图片重复的情况，请不要在意这个问题- -
        RetrofitService.getBeautyPhoto()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .subscribe(new Subscriber<List<BeautyPhotoBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.showNetError(new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getData();
                            }
                        });
                    }

                    @Override
                    public void onNext(List<BeautyPhotoBean> photoList) {
                        mView.loadData(photoList);
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitService.getMoreBeautyPhoto()
                .subscribe(new Subscriber<List<BeautyPhotoBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<BeautyPhotoBean> photoList) {
                        mView.loadMoreData(photoList);
                    }
                });
    }
}
