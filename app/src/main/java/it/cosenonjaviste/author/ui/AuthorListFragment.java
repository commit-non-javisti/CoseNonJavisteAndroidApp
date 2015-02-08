package it.cosenonjaviste.author.ui;

import android.annotation.SuppressLint;
import android.view.View;

import com.quentindommerc.superlistview.SuperGridview;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.InjectView;
import butterknife.OnClick;
import it.cosenonjaviste.CnjFragment;
import it.cosenonjaviste.R;
import it.cosenonjaviste.author.AuthorListModel;
import it.cosenonjaviste.author.AuthorListPresenter;
import rx.functions.Actions;

public class AuthorListFragment extends CnjFragment<AuthorListPresenter, AuthorListModel> {

    @InjectView(R.id.grid) SuperGridview grid;

    @Inject Provider<AuthorListPresenter> presenterProvider;

    private AuthorAdapter adapter;

    @Override protected AuthorListPresenter createPresenter() {
        return presenterProvider.get();
    }

    @Override protected int getLayoutId() {
        return R.layout.super_grid;
    }

    @SuppressLint("ResourceAsColor") @Override protected void initView(View view) {
        super.initView(view);
        adapter = new AuthorAdapter(getActivity());
        grid.getList().setNumColumns(2);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener((parent, v, position, id) -> presenter.goToAuthorDetail(position));
    }

    @OnClick(R.id.error_retry) void retry() {
        presenter.loadAuthors();
    }

    @Override public void update(AuthorListModel model) {
        presenter.testFromFragment();
        model.call(authors -> {
            grid.showList();
            adapter.reloadData(authors);
        }).whenError(t -> grid.showError()).whenEmpty(Actions.empty());
    }

    public void startLoading() {
        grid.showProgress();
    }
}