package fr.twiloo.iut.gtes.common;

/**
 * @param <C> content type
 */
public abstract class Response<C> {
    protected final C content;

    public Response(C content) {
        this.content = content;
    }

    public C getContent() {
        return content;
    }
}
