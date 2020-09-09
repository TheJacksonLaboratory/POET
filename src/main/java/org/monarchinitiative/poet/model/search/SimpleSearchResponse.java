package org.monarchinitiative.poet.model.search;

public class SimpleSearchResponse {

    private String name;
    private String value;
    private String type;

    public SimpleSearchResponse(String name, String value, String type){
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
