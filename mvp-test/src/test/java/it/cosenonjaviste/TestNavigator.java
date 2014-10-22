package it.cosenonjaviste;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.cosenonjaviste.mvp.base.ContextBinder;
import it.cosenonjaviste.mvp.base.MapPresenterArgs;
import it.cosenonjaviste.mvp.base.Navigator;
import it.cosenonjaviste.mvp.base.PresenterArgs;
import it.cosenonjaviste.mvp.base.RxMvpPresenter;
import it.cosenonjaviste.utils.PresenterTestUtils;
import rx.functions.Action1;

@Singleton
public class TestNavigator implements Navigator {

    private Object lastModel;

    private RxMvpPresenter<?> lastPresenter;

    private String lastOpenedUrl;

    @Inject
    public TestNavigator() {
    }

    @Override public void show(ContextBinder contextBinder, Class<? extends RxMvpPresenter<?>> presenterClass, Action1<PresenterArgs> argsAction) {
        lastPresenter = createPresenter(presenterClass);
        lastModel = PresenterTestUtils.init(lastPresenter, getArgs(argsAction), this);
    }

    @Override public <T> T createFragment(Class<? extends RxMvpPresenter<?>> presenterClass, Action1<PresenterArgs> argsAction) {
        show(contextBinder, presenterClass, argsAction);
        return null;
    }

    @Override public void open(String url) {
        lastOpenedUrl = url;
    }

    private MapPresenterArgs getArgs(Action1<PresenterArgs> argsAction) {
        MapPresenterArgs args = new MapPresenterArgs();
        if (argsAction != null) {
            argsAction.call(args);
        }
        return args;
    }

    private RxMvpPresenter<?> createPresenter(Class<? extends RxMvpPresenter<?>> presenterClass) {
        RxMvpPresenter<?> presenter;
        try {
            presenter = presenterClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return presenter;
    }

    public <M> M getLastModel() {
        return (M) lastModel;
    }

    public RxMvpPresenter<?> getLastPresenter() {
        return lastPresenter;
    }

    public String getLastOpenedUrl() {
        return lastOpenedUrl;
    }
}
