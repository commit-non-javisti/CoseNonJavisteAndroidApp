package it.cosenonjaviste.lib.mvp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import dagger.ObjectGraph;
import it.cosenonjaviste.lib.mvp.dagger.DaggerApplication;
import it.cosenonjaviste.lib.mvp.dagger.ObjectGraphHolder;
import it.cosenonjaviste.mvp.base.ContextBinder;
import it.cosenonjaviste.mvp.base.MvpConfig;
import it.cosenonjaviste.mvp.base.PresenterArgs;
import rx.Observable;
import rx.Scheduler;
import rx.android.observables.AndroidObservable;
import rx.schedulers.Schedulers;

public class ActivityContextBinder implements ContextBinder {

    private static Scheduler io = Schedulers.io();

    public static <T> Observable<T> background(Activity activity, Observable<T> observable) {
        return AndroidObservable.bindActivity(activity, observable.subscribeOn(io));
    }

    public static <T> Observable<T> background(Object fragment, Observable<T> observable) {
        return AndroidObservable.bindFragment(fragment, observable.subscribeOn(io));
    }

    public static void setIo(Scheduler io) {
        ActivityContextBinder.io = io;
    }

    private FragmentActivity activity;

    public ActivityContextBinder(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override public <T> Observable<T> bindObservable(Observable<T> observable) {
        return background(activity, observable);
    }

    public void showInActivity(String fragmentClassName, PresenterArgs args) {
//        if (activity instanceof MultiFragmentActivity) {
//            Fragment fragment = instantiate(fragmentClassName, argsAction);
//            ((MultiFragmentActivity) activity).showFragment(fragment);
//        } else {
//            throw new RuntimeException("Actvivity class " + activity.getClass().getName() + " must implement " + MultiFragmentActivity.class.getName());
//        }
    }

//    private Fragment instantiate(String fragmentClassName, Action1<PresenterArgs> argsAction) {
//        return Fragment.instantiate(activity, fragmentClassName, createArgs(argsAction));
//    }

    @Override public void startNewActivity(Class<? extends MvpConfig<?, ?, ?>> config, PresenterArgs args) {
        Intent intent = SingleFragmentActivity.createIntent(activity, config);
        Bundle bundle = createArgs(args);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override public <T> T createFragment(MvpConfig<?, ?, ?> config, PresenterArgs args) {
        Fragment fragment = (Fragment) config.createView();
        Bundle bundle = createArgs(args);
        bundle.putString(SingleFragmentActivity.CONFIG_CLASS, config.getClass().getName());
        fragment.setArguments(bundle);
        return (T) fragment;
    }

    private Bundle createArgs(PresenterArgs args) {
        if (args != null) {
            return ((BundlePresenterArgs) args).getBundle();
        } else {
            return new Bundle();
        }
    }

    @Override public <T> T getObject(Class<T> type) {
        ObjectGraph objectGraph = ObjectGraphHolder.getObjectGraph((DaggerApplication) activity.getApplication());
        return objectGraph.get(type);
    }

    @Override public PresenterArgs createArgs() {
        return new BundlePresenterArgs();
    }
}
