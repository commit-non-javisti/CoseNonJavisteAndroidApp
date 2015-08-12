package it.cosenonjaviste.lib.mvp;

public class MvpPresenter<M, V> implements LifeCycleListener<V> {
    public static final String MODEL = "model";

    private V view;

    private M model;

    @Override public void resume(V view) {
        this.view = view;
        resume();
    }

    @Override public void pause() {
    }

    public void resume() {
    }

    @Override public void destroy() {
    }

    @Override public void detachView() {
        this.view = null;
    }

    public M createDefaultModel() {
        return null;
    }

    @Override public void saveState(ObjectSaver saver) {
        saver.save(MODEL, getModel());
    }

    @Override public void loadState(ObjectLoader loader) {
        model = loader.load(MODEL);
        if (model == null) {
            model = createDefaultModel();
            if (model == null) {
                throw new RuntimeException("createDefaultModel not implemented in " + getClass().getName());
            }
        }
    }

    public M initAndResume(V view) {
        return initAndResume(null, view);
    }

    public M initAndResume(final M newModel, V view) {
        loadState(new LifeCycleListener.ObjectLoader() {
            @Override public <T> T load(String key) {
                return (T) newModel;
            }
        });
        resume(view);
        return getModel();
    }

    public final V getView() {
        return view;
    }

    public M getModel() {
        return model;
    }
}
