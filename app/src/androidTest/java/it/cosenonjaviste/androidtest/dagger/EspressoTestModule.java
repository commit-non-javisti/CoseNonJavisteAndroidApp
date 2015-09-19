package it.cosenonjaviste.androidtest.dagger;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.cosenonjaviste.androidtest.base.EspressoSchedulerManager;
import it.cosenonjaviste.core.MessageManager;
import it.cosenonjaviste.core.Navigator;
import it.cosenonjaviste.model.MailJetService;
import it.cosenonjaviste.model.TwitterService;
import it.cosenonjaviste.model.WordPressService;
import it.cosenonjaviste.mv2m.rx.SchedulerManager;
import it.cosenonjaviste.ui.SnackbarMessageManager;

@Module
public class EspressoTestModule {

    @Provides @Singleton WordPressService provideWordPressService() {
        return Mockito.mock(WordPressService.class);
    }

    @Provides @Singleton SchedulerManager provideSchedulerManager() {
        return new EspressoSchedulerManager();
    }

    @Provides @Singleton TwitterService provideTwitterService() {
        return Mockito.mock(TwitterService.class);
    }

    @Provides @Singleton MailJetService provideMailJetService() {
        return Mockito.mock(MailJetService.class);
    }

    @Provides Navigator provideNavigator() {
        return Mockito.mock(Navigator.class);
    }

    @Provides MessageManager provideMessageManager() {
        return new SnackbarMessageManager();
    }
}
