package org.monarchinitiative.poet.model;

public class CommonDiseaseAnnotation {

    private final long id;
    private final String content;

    public CommonDiseaseAnnotation(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
