package it.cosenonjaviste.twitter;

import android.annotation.SuppressLint;
import android.view.View;

import com.quentindommerc.superlistview.SuperListview;

import org.parceler.ParcelClass;
import org.parceler.ParcelClasses;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.InjectView;
import butterknife.OnClick;
import it.cosenonjaviste.CnjFragment;
import it.cosenonjaviste.R;
import it.cosenonjaviste.model.Tweet;
import it.cosenonjaviste.mvp.base.MvpView;
import rx.functions.Actions;

@ParcelClasses({@ParcelClass(TweetListModel.class), @ParcelClass(Tweet.class)})
public class TweetListFragment extends CnjFragment<TweetListPresenter, TweetListModel> implements MvpView<TweetListModel> {

    @InjectView(R.id.list) SuperListview list;

    @Inject Provider<TweetListPresenter> presenterProvider;

    private TweetAdapter adapter;

    @Override protected TweetListPresenter createPresenter() {
        return presenterProvider.get();
    }

    @Override protected int getLayoutId() {
        return R.layout.super_list;
    }

    @SuppressLint("ResourceAsColor") @Override protected void initView(View view) {
        super.initView(view);
        adapter = new TweetAdapter(getActivity());
        list.setAdapter(adapter);
        list.setRefreshingColor(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        list.setRefreshListener(presenter::reloadData);
        list.setupMoreListener((numberOfItems, numberBeforeMore, currentItemPos) -> presenter.loadNextPage(), 1);
    }

    @OnClick(R.id.error_retry) void retry() {
        presenter.reloadData();
    }

    @Override public void update(TweetListModel model) {
        model.call(
                items -> {
                    list.showList();
                    list.hideMoreProgress(model.isMoreDataAvailable());
                    adapter.reloadData(items);
                }
        ).whenError(
                t -> list.showError()
        ).whenEmpty(
                Actions.empty()
        );
    }

    public void startLoading(boolean showMainLoading) {
        if (showMainLoading) {
            list.showProgress();
        } else {
            list.setRefreshing(true);
        }
    }

    public void startMoreItemsLoading() {
        list.showMoreProgress();
    }
}