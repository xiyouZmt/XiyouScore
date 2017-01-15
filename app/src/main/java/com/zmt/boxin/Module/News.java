package com.zmt.boxin.Module;

import java.io.Serializable;

/**
 * Created by Dangelo on 2016/6/8.
 */
public class News implements Serializable {

    private String title;
    private String url;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
