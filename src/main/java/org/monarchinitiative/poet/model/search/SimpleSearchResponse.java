package org.monarchinitiative.poet.model.search;

public class SimpleSearchResponse {

    private String name;
    private String id;
    private String type;

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
