package mik.voice.siri.api_data_class;

public class instructions {
    String id;
    String description;
    public instructions(){

    }
    public instructions(String id, String description){
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
