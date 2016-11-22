package io.dwak.freight.internal;

import com.bluelinelabs.conductor.Router;

import io.dwak.freight.Navigator;


public abstract class FreightNavigator implements Navigator {
    @SuppressWarnings("WeakerAccess")
    protected final Router router;

    public FreightNavigator(Router router) {
        this.router = router;
    }

    @Override
    public boolean popToRoot() {
        return router.popToRoot();
    }

    @Override
    public boolean goBack() {
        return router.popCurrentController();
    }
}
