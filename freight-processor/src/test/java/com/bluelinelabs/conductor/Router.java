package com.bluelinelabs.conductor;

public abstract class Router {
    public abstract void pushController(RouterTransaction routerTransaction);
    public abstract boolean popToRoot();
    public abstract boolean popCurrentController();
}
