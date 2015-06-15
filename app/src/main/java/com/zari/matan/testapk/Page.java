package com.zari.matan.testapk;

/**
 * Created by Matan on 5/6/2015.
 */
public class Page {

    String title, cover, name;

    public Page(String title, String cover, String name) {
        this.title = title;
        this.cover = cover;
        this.name = name;
    }

    public Page(String title, String cover) {
        this.title = title;
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }
}
