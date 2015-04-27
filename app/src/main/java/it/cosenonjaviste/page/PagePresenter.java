package it.cosenonjaviste.page;

import javax.inject.Inject;

import it.cosenonjaviste.lib.mvp.RxMvpPresenter;
import it.cosenonjaviste.lib.mvp.utils.SchedulerManager;
import it.cosenonjaviste.utils.PresenterScope;

@PresenterScope
public class PagePresenter extends RxMvpPresenter<PageModel> {

    private PageUrlManager pageUrlManager;

    @Inject public PagePresenter(SchedulerManager schedulerManager, PageUrlManager pageUrlManager) {
        super(schedulerManager);
        this.pageUrlManager = pageUrlManager;
    }

    public String getPostUrl() {
        return pageUrlManager.getUrl(model.getUrl());
    }
}
