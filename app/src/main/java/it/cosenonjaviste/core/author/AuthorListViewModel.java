package it.cosenonjaviste.core.author;

import android.databinding.ObservableBoolean;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import it.cosenonjaviste.core.Navigator;
import it.cosenonjaviste.core.list.RxListViewModel;
import it.cosenonjaviste.core.post.PostListModel;
import it.cosenonjaviste.model.Author;
import it.cosenonjaviste.model.AuthorResponse;
import it.cosenonjaviste.model.WordPressService;
import it.cosenonjaviste.mv2m.rx.SchedulerManager;
import rx.Observable;

public class AuthorListViewModel extends RxListViewModel<AuthorListModel> {

    private WordPressService wordPressService;

    private Navigator navigator;

    @Inject public AuthorListViewModel(SchedulerManager schedulerManager, WordPressService wordPressService, Navigator navigator) {
        super(schedulerManager);
        this.wordPressService = wordPressService;
        this.navigator = navigator;
        registerActivityAware(navigator);
    }

    @Override public AuthorListModel createDefaultModel() {
        return new AuthorListModel();
    }

    @Override protected void reloadData(ObservableBoolean loadingAction) {
        loadingAction.set(true);

        Observable<List<Author>> observable = wordPressService
                .listAuthors()
                .map(AuthorResponse::getAuthors)
                .doOnNext(Collections::sort)
                .finallyDo(() -> loadingAction.set(false));

        subscribe(observable,
                l -> getModel().done(l),
                throwable -> getModel().error());
    }

    public void goToAuthorDetail(int position) {
        Author author = getModel().get(position);
        navigator.openPostList(new PostListModel(author));
    }
}
