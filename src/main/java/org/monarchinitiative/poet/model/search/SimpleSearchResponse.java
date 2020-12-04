package org.monarchinitiative.poet.model.search;

public class SimpleSearchResponse {

    private final String name;
    private final String id;
    private final String type;

    public SimpleSearchResponse(String name, String id, String type){
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
