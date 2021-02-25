package org.monarchinitiative.poet.model.response;

public class SearchResponse {

    private String id;
    private String name;
    private String type;

    public SearchResponse(){}

    public SearchResponse(String id, String name,  String type){
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
