package it.cosenonjaviste.ui.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.cosenonjaviste.R;
import it.cosenonjaviste.core.category.CategoryListPresenter;
import it.cosenonjaviste.core.category.CategoryListView;
import it.cosenonjaviste.core.post.PostListModel;
import it.cosenonjaviste.databinding.CategoryRowBinding;
import it.cosenonjaviste.lib.MvpFragment;
import it.cosenonjaviste.ui.CoseNonJavisteApp;
import it.cosenonjaviste.ui.post.PostListFragment;
import it.cosenonjaviste.ui.utils.RecyclerBindingBuilder;
import it.cosenonjaviste.ui.utils.SingleFragmentActivity;

public class CategoryListFragment extends MvpFragment<CategoryListPresenter> implements CategoryListView {

    @Override protected CategoryListPresenter createPresenter() {
        return CoseNonJavisteApp.getComponent(this).getCategoryListPresenter();
    }

    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new RecyclerBindingBuilder<>(inflater, container, presenter)
                .gridLayoutManager(2)
                .viewHolderFactory(v -> new CategoryViewHolder(CategoryRowBinding.bind(inflater.inflate(R.layout.category_row, v, false)), presenter))
                .getRoot();
    }

    @Override public void openPostList(PostListModel model) {
        SingleFragmentActivity.open(getActivity(), PostListFragment.class, model);
    }
}
