package it.cosenonjaviste.mvp.post;

import java.util.List;

import javax.inject.Inject;

import it.cosenonjaviste.model.Author;
import it.cosenonjaviste.model.Category;
import it.cosenonjaviste.model.Post;
import it.cosenonjaviste.model.PostResponse;
import it.cosenonjaviste.model.WordPressService;
import it.cosenonjaviste.mvp.base.RxMvpPresenter;
import it.cosenonjaviste.mvp.base.SchedulerManager;
import it.cosenonjaviste.mvp.base.args.PresenterArgs;
import it.cosenonjaviste.mvp.base.args.PresenterArgsFactory;
import it.cosenonjaviste.mvp.page.PagePresenter;
import it.cosenonjaviste.mvp.page.PageView;
import rx.Observable;

public class PostListPresenter extends RxMvpPresenter<PostListModel> {

    private static final String CATEGORY = "category";

    private static final String AUTHOR = "author";

    @Inject WordPressService wordPressService;

    @Inject public PostListPresenter(SchedulerManager schedulerManager, PresenterArgsFactory presenterArgsFactory) {
        super(schedulerManager, presenterArgsFactory);
    }

    @Override public PostListModel createModel(PresenterArgs args) {
        PostListModel postListModel = new PostListModel();
        postListModel.setCategory(args.getObject(CATEGORY));
        postListModel.setAuthor(args.getObject(AUTHOR));
        return postListModel;
    }

    public void reloadData() {
        Observable<List<Post>> observable = getObservable(0);

        subscribePausable(observable,
                () -> getView().startLoading(model.getItems().isEmpty()),
                posts -> {
                    model.getItems().done(posts);
                    model.setMoreDataAvailable(posts.size() == WordPressService.POST_PAGE_SIZE);
                    view.update(model);
                }, throwable -> {
                    model.getItems().error(throwable);
                    view.update(model);
                });
    }

    public void goToDetail(Post item) {
        PresenterArgs args = PagePresenter.populateArgs(presenterArgsFactory.create(), item.getUrl());
        getView().open(PageView.class, args);
    }

    public static PresenterArgs open(PresenterArgsFactory factory, Category category) {
        PresenterArgs args = factory.create();
        args.putObject(CATEGORY, category);
        return args;
    }

    public static PresenterArgs open(PresenterArgsFactory factory, Author author) {
        PresenterArgs args = factory.create();
        args.putObject(AUTHOR, author);
        return args;
    }

    public void loadNextPage() {
        int page = calcNextPage(model.getItems().size(), WordPressService.POST_PAGE_SIZE);
        Observable<List<Post>> observable = getObservable(page);

        subscribePausable(observable,
                () -> getView().startMoreItemsLoading(),
                posts -> {
                    model.getItems().append(posts);
                    model.setMoreDataAvailable(posts.size() == WordPressService.POST_PAGE_SIZE);
                    view.update(model);
                }, throwable -> {
                    model.getItems().error(throwable);
                    view.update(model);
                });
    }

    private Observable<List<Post>> getObservable(int page) {
        Observable<PostResponse> observable;
        Category category = model.getCategory();
        if (category != null) {
            observable = wordPressService.listCategoryPosts(category.getId(), page);
        } else {
            Author author = model.getAuthor();
            if (author != null) {
                observable = wordPressService.listAuthorPosts(author.getId(), page);
            } else {
                observable = wordPressService.listPosts(page);
            }
        }
        return observable.map(PostResponse::getPosts);
    }

    private static int calcNextPage(int size, int pageSize) {
        return size / pageSize + 1;
    }

    @Override public PostListView getView() {
        return (PostListView) super.getView();
    }
}
