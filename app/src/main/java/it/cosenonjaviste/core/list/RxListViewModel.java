package it.cosenonjaviste.core.list;

import android.databinding.ObservableBoolean;

import it.cosenonjaviste.lib.rx.RxViewModel;
import it.cosenonjaviste.lib.rx.SchedulerManager;

public abstract class RxListViewModel<M extends ListModel<?>, V> extends RxViewModel<M, V> implements GenericRxListViewModel {
    protected ObservableBoolean loading = new ObservableBoolean();

    protected ObservableBoolean loadingNextPage = new ObservableBoolean();

    protected ObservableBoolean loadingPullToRefresh = new ObservableBoolean();

    public RxListViewModel(SchedulerManager schedulerManager) {
        super(schedulerManager);
    }

    @Override public ObservableBoolean isLoading() {
        return loading;
    }

    @Override public ObservableBoolean isLoadingPullToRefresh() {
        return loadingPullToRefresh;
    }

    @Override public ObservableBoolean isLoadingNextPage() {
        return loadingNextPage;
    }

    @Override public ObservableBoolean isError() {
        return getModel().error;
    }

    @Override public void resume() {
        super.resume();
        if (!getModel().isLoaded() && !loading.get()) {
            reloadData();
        }
    }

    public void reloadData() {
        reloadData(loading);
    }

    public final void loadDataPullToRefresh() {
        reloadData(loadingPullToRefresh);
    }

    protected abstract void reloadData(ObservableBoolean loadingAction);
}