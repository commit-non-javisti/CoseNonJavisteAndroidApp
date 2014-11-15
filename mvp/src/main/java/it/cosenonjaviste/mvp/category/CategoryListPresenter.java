package it.cosenonjaviste.mvp.category;

import java.util.List;

import javax.inject.Inject;

import it.cosenonjaviste.model.Category;
import it.cosenonjaviste.model.CategoryResponse;
import it.cosenonjaviste.model.WordPressService;
import it.cosenonjaviste.mvp.ListPresenter;
import it.cosenonjaviste.mvp.base.SchedulerManager;
import it.cosenonjaviste.mvp.base.args.PresenterArgsFactory;
import it.cosenonjaviste.mvp.post.PostListPresenter;
import it.cosenonjaviste.mvp.post.PostListView;
import rx.Observable;

public class CategoryListPresenter extends ListPresenter<Category> {

    @Inject WordPressService wordPressService;

    @Inject public CategoryListPresenter(SchedulerManager schedulerManager, PresenterArgsFactory presenterArgsFactory) {
        super(schedulerManager, presenterArgsFactory);
    }

    public void loadData() {
        Observable<List<Category>> observable = wordPressService
                .listCategories()
                .map(CategoryResponse::getCategories);

        subscribeListObservable(observable);
    }

    public void goToPosts(int position) {
        Category category = model.get(position);
        getView().open(PostListView.class, PostListPresenter.open(presenterArgsFactory, category));
    }
}
