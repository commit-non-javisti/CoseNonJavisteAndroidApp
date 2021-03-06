package it.cosenonjaviste.core.category;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;

import io.reactivex.Single;
import it.cosenonjaviste.core.CnjJUnitDaggerRule;
import it.cosenonjaviste.core.Navigator;
import it.cosenonjaviste.core.ParcelableTester;
import it.cosenonjaviste.core.post.PostListArgument;
import it.cosenonjaviste.daggermock.InjectFromComponent;
import it.cosenonjaviste.model.Category;
import it.cosenonjaviste.model.WordPressService;
import it.cosenonjaviste.ui.category.CategoryListFragment;

import static it.cosenonjaviste.TestData.categoryResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryListViewModelTest {

    @Rule public final CnjJUnitDaggerRule daggerRule = new CnjJUnitDaggerRule();

    @Mock WordPressService wordPressService;

    @Mock Navigator navigator;

    @InjectFromComponent(CategoryListFragment.class) CategoryListViewModel viewModel;

    @Test
    public void testParcelable() {
        CategoryListModel model = new CategoryListModel();
        ParcelableTester.check(model, CategoryListModel.CREATOR);

        model.done(Arrays.asList(Category.create(123, "abc", 3), Category.create(123456, "abcdef", 6)));
        ParcelableTester.check(model, CategoryListModel.CREATOR);
    }

    @Test
    public void testLoad() {
        when(wordPressService.listCategories())
                .thenReturn(categoryResponse(3));

        CategoryListModel model = viewModel.initAndResume();

        assertThat(model.getItems()).hasSize(3);
        Category category = model.get(2);
        assertThat(category.id()).isEqualTo(2);
        assertThat(category.title()).isEqualTo("cat 2");
        assertThat(category.postCount()).isEqualTo(12);
    }

    @Test
    public void testLoadAndPullToRefresh() {
        when(wordPressService.listCategories())
                .thenReturn(categoryResponse(3), categoryResponse(2));

        CategoryListModel model = viewModel.initAndResume();

        assertThat(model.getItems()).hasSize(3);

        viewModel.loadDataPullToRefresh();

        assertThat(model.getItems()).hasSize(2);
    }

    @Test
    public void testRetryAfterError() {
        when(wordPressService.listCategories())
                .thenReturn(Single.error(new RuntimeException()));

        CategoryListModel model = viewModel.initAndResume();

        when(wordPressService.listCategories())
                .thenReturn(categoryResponse(3));

        viewModel.reloadData();

        assertThat(model.getItems()).hasSize(3);
    }

    @Test
    public void testGoToPosts() {
        when(wordPressService.listCategories())
                .thenReturn(categoryResponse(3));

        CategoryListModel categoryListModel = viewModel.initAndResume();

        viewModel.goToPosts(1);

        verify(navigator).openPostList(PostListArgument.create(categoryListModel.get(1)));
    }
}