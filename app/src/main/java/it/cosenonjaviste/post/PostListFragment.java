package it.cosenonjaviste.post;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.ParcelClass;

import javax.inject.Inject;

import it.cosenonjaviste.CoseNonJavisteApp;
import it.cosenonjaviste.databinding.PostRowBinding;
import it.cosenonjaviste.model.Post;
import it.cosenonjaviste.page.PageFragment;
import it.cosenonjaviste.page.PageModel;
import it.cosenonjaviste.utils.BindableViewHolder;
import it.cosenonjaviste.utils.RecyclerViewRxMvpFragment;
import it.cosenonjaviste.utils.SingleFragmentActivity;

@ParcelClass(PostListModel.class)
public class PostListFragment extends RecyclerViewRxMvpFragment<Post> implements PostListView {

    @Inject PostListPresenter presenter;

    @Override public void init() {
        CoseNonJavisteApp.createComponent(this,
                c -> DaggerPostListComponent.builder().applicationComponent(c).build()
        ).inject(this);
    }

    @NonNull @Override protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding.setModel(presenter.getModel());
        binding.swipeRefresh.setOnRefreshListener(presenter::loadDataPullToRefresh);
        presenter.getModel().setListChangeListener(adapter::reloadData);
        return view;
    }

    @Override protected void loadMoreItems() {
        presenter.loadNextPage();
    }

    @NonNull @Override protected BindableViewHolder<Post> createViewHolder(LayoutInflater inflater, ViewGroup v) {
        return new PostViewHolder(PostRowBinding.inflate(inflater, v, false), presenter);
    }

    @Override protected void retry() {
        presenter.reloadData();
    }

    @Override public void openDetail(PageModel model) {
        SingleFragmentActivity.open(getActivity(), PageFragment.class, model);
    }
}
