package it.cosenonjaviste.core;

import java.util.Date;
import java.util.List;

import it.cosenonjaviste.core.model.Author;
import it.cosenonjaviste.core.model.AuthorResponse;
import it.cosenonjaviste.core.model.Category;
import it.cosenonjaviste.core.model.CategoryResponse;
import it.cosenonjaviste.core.model.Post;
import it.cosenonjaviste.core.model.PostResponse;
import it.cosenonjaviste.core.model.Tweet;
import rx.Observable;

public class TestData {

    public static Observable<PostResponse> postResponse(int size) {
        return postResponse(0, size);
    }

    public static Observable<PostResponse> postResponse(int start, int size) {
        return Observable.range(start, size)
                .map(i -> new Post(i, createAuthor(i), "post title " + i, new Date(), "url " + i, "excerpt " + i))
                .toList()
                .map(PostResponse::new);
    }

    public static Author createAuthor(int i) {
        return new Author(i, "name " + i, "last name " + i, "desc " + i);
    }

    public static Observable<AuthorResponse> authorResponse(int size) {
        return Observable.range(0, size)
                .map(TestData::createAuthor)
                .toList()
                .map(AuthorResponse::new);
    }

    public static Observable<CategoryResponse> categoryResponse(int size) {
        return Observable.range(0, size)
                .map(TestData::createCategory)
                .toList()
                .map(CategoryResponse::new);
    }

    private static Category createCategory(int i) {
        return new Category(i, "cat " + i, 10 + i);
    }

    public static Observable<List<Tweet>> tweets() {
        return Observable.range(0, 10)
                .map(i -> new Tweet(123, "tweet text " + i, new Date(), "image", "author"))
                .toList();
    }
}