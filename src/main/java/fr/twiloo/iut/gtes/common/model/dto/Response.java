package fr.twiloo.iut.gtes.common.model.dto;

import java.io.Serializable;

/**
 * @param <C> content type
 */
public abstract class Response<C> implements Serializable {
    protected final C content;

    public Response(C content) {
        this.content = content;
    }

    public C getContent() {
        return content;
    }
}
