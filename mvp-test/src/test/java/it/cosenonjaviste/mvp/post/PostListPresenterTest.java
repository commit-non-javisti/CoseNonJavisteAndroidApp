package it.cosenonjaviste.mvp.post;

import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import it.cosenonjaviste.MvpTestModule;
import it.cosenonjaviste.model.Post;
import it.cosenonjaviste.stubs.JsonStubs;
import it.cosenonjaviste.stubs.MockWebServerUtils;
import it.cosenonjaviste.utils.TestContextBinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class PostListPresenterTest {

    @Inject MockWebServer server;

    @Inject PostListPresenter presenter;

    private TestContextBinder contextBinder;
    private PostListView view;
    private PostListModel model;

    @Before
    public void setup() {
        ObjectGraph objectGraph = ObjectGraph.create(new MvpTestModule(), new TestModule());
        objectGraph.inject(this);
        contextBinder = new TestContextBinder(objectGraph);

        MockWebServerUtils.initDispatcher(server, JsonStubs.POSTS);
        view = contextBinder.createFragment(PostListView.class, PostListPresenter.class, null);
        model = contextBinder.getLastModel();
        presenter = contextBinder.getLastPresenter();
        presenter.subscribe(view);
    }

    @Test
    public void testLoad() {
        assertNotNull(model.getPosts());
        assertEquals(1, model.getPosts().size());
    }

    @Test
    public void testGoToDetails() {
        Post firstPost = model.getPosts().get(0);

        presenter.goToDetail(firstPost);

        PostDetailModel detailModel = contextBinder.getLastModel();
        assertNotNull(detailModel.getPost());
        assertEquals(firstPost.getId(), detailModel.getPost().getId());
    }

    @Module(injects = {PostListPresenterTest.class, PostDetailView.class, PostListView.class, PostListPresenter.class}, addsTo = MvpTestModule.class)
    public static class TestModule {
        @Provides PostListView providePostListView() {
            return mock(PostListView.class);
        }

        @Provides PostDetailView providePostDetailView() {
            return mock(PostDetailView.class);
        }
    }
}