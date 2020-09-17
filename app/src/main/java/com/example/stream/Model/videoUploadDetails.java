package com.example.stream.Model;

public class videoUploadDetails {
    String video_slide,video_type,vdeo_thumb,video_url,video_name,video_description,video_catogory;

    public videoUploadDetails(String video_slide, String video_type, String vdeo_thumb, String video_url, String video_name, String video_description, String video_catogory) {
        this.video_slide = video_slide;
        this.video_type = video_type;
        this.vdeo_thumb = vdeo_thumb;
        this.video_url = video_url;
        this.video_name = video_name;
        this.video_description = video_description;
        this.video_catogory = video_catogory;
    }

    public videoUploadDetails(String video_slide) {

    }

    public String getVideo_slide() {
        return video_slide;
    }

    public void setVideo_slide(String video_slide) {
        this.video_slide = video_slide;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getVdeo_thumb() {
        return vdeo_thumb;
    }

    public void setVdeo_thumb(String vdeo_thumb) {
        this.vdeo_thumb = vdeo_thumb;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_description() {
        return video_description;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }

    public String getVideo_catogory() {
        return video_catogory;
    }

    public void setVideo_catogory(String video_catogory) {
        this.video_catogory = video_catogory;
    }
}
