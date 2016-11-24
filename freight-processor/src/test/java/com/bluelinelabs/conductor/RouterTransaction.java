package com.bluelinelabs.conductor;

public class RouterTransaction {
    public static RouterTransaction with(Controller controller){
        return new RouterTransaction();
    }

    public RouterTransaction tag(String tag){
        return new RouterTransaction();
    }
}
