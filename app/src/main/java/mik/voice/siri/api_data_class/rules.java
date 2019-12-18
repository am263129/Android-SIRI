package mik.voice.siri.api_data_class;

public class rules {
    String id;
    String description;
    public rules(){

    }
    public rules(String id, String description){
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

}
