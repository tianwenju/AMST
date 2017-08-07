package com.delta.WebSocketLibs;

import okio.ByteString;

/**
 * @description :策略模式构建WsManager 以后不依赖具体的实现
 * @autHor :  V.Wenju.Tian
 * @date : 2017/3/2 16:43
 */


public class WsManager implements ISocketListener {


    private BaseWebSocketStrategy baseWebSocketStrategy;

    public WsManager() {
    }

    public WsManager(BaseWebSocketStrategy baseWebSocketStrategy) {
        this.baseWebSocketStrategy = baseWebSocketStrategy;
    }

    public void setBaseWebSocketStrategy(BaseWebSocketStrategy baseWebSocketStrategy) {
        this.baseWebSocketStrategy = baseWebSocketStrategy;
    }


    @Override
    public void startConnect() {
        baseWebSocketStrategy.startConnect();
    }

    @Override
    public void stopConnect() {
        baseWebSocketStrategy.stopConnect();

    }

    @Override
    public boolean isWsConnected() {
        return baseWebSocketStrategy.isWsConnected();
    }

    @Override
    public int getCurrentStatus() {
        return baseWebSocketStrategy.getCurrentStatus();
    }

    @Override
    public boolean sendMessage(String msg) {
        return baseWebSocketStrategy.sendMessage(msg);
    }

    @Override
    public boolean sendMessage(ByteString byteString) {
        return baseWebSocketStrategy.sendMessage(byteString);
    }


    public static WsManager getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        static WsManager sInstance = new WsManager();
    }
}
