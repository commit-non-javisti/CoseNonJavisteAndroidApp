package it.cosenonjaviste.mvp.author;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import it.cosenonjaviste.model.Author;
import it.cosenonjaviste.model.AuthorResponse;
import it.cosenonjaviste.model.WordPressService;
import it.cosenonjaviste.mvp.ListPresenter;
import it.cosenonjaviste.mvp.base.SchedulerManager;
import it.cosenonjaviste.mvp.post.PostListModel;
import it.cosenonjaviste.mvp.post.PostListView;
import rx.Observable;

public class AuthorListPresenter extends ListPresenter<Author> {

    @Inject WordPressService wordPressService;

    @Inject public AuthorListPresenter(SchedulerManager schedulerManager) {
        super(schedulerManager);
    }

    public void loadAuthors() {
        Observable<List<Author>> observable = wordPressService
                .listAuthors()
                .map(AuthorResponse::getAuthors)
                .doOnNext(Collections::sort);
        subscribeListObservable(observable);
    }

    public void goToAuthorDetail(int position) {
        Author author = model.get(position);
        getView().open(PostListView.class, new PostListModel(author));
    }
}
