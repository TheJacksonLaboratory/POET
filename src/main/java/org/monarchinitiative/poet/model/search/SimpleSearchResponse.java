package org.monarchinitiative.poet.model.search;

public class SimpleSearchResponse {

    private String name;
    private String value;

    public SimpleSearchResponse(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
