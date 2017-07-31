package com.example.twanist.project_movie;


public class PlayVideo {

    private String Name;
    private String type;

    public PlayVideo(String name, String type) {
        Name = name;
        this.type = type;
    }


    public String getName() {
        return Name;
    }

    public String getType() {
        return type;
    }
}
