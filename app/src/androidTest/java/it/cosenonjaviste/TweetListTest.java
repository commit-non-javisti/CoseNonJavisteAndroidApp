package it.cosenonjaviste;

import dagger.Module;
import it.cosenonjaviste.base.CnjFragmentTest;
import it.cosenonjaviste.base.MvpEspressoTestModule;
import it.cosenonjaviste.twitter.TweetListModel;
import it.cosenonjaviste.twitter.TweetListFragment;

public class TweetListTest extends CnjFragmentTest<TweetListModel> {

    public TweetListTest() {
        super(TweetListFragment.class, new TweetListModel());
    }

    @Override protected Object getTestModule() {
        return new TestModule();
    }

    public void testPostList() throws InterruptedException {
        showUi();
    }

    @Module(injects = TweetListTest.class, includes = MvpEspressoTestModule.class)
    public static class TestModule {
    }
}
