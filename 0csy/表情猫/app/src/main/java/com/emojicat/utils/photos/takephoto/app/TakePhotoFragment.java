package com.emojicat.utils.photos.takephoto.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.emojicat.R;
import com.emojicat.utils.photos.takephoto.app.*;
import com.emojicat.utils.photos.takephoto.app.TakePhotoImpl;
import com.emojicat.utils.photos.takephoto.model.InvokeParam;
import com.emojicat.utils.photos.takephoto.model.TContextWrap;
import com.emojicat.utils.photos.takephoto.model.TResult;
import com.emojicat.utils.photos.takephoto.permission.InvokeListener;
import com.emojicat.utils.photos.takephoto.permission.PermissionManager;
import com.emojicat.utils.photos.takephoto.permission.TakePhotoInvocationHandler;

/**
 * 继承这个类来让Fragment获取拍照的能力<br>
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:3.0.0
 * 技术博文：http://www.cboy.me
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class TakePhotoFragment extends Fragment implements com.emojicat.utils.photos.takephoto.app.TakePhoto.TakeResultListener,InvokeListener {
    private static final String TAG = TakePhotoFragment.class.getName();
    private InvokeParam invokeParam;
    private com.emojicat.utils.photos.takephoto.app.TakePhoto takePhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(getActivity(),type,invokeParam,this);
    }
    /**
     *  获取TakePhoto实例
     * @return
     */
    public com.emojicat.utils.photos.takephoto.app.TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (com.emojicat.utils.photos.takephoto.app.TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }
    @Override
    public void takeSuccess(TResult result) {
        Log.i(TAG,"takeSuccess：" + result.getImage().getCompressPath());
    }
    @Override
    public void takeFail(TResult result,String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }
    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
    }
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }
}
