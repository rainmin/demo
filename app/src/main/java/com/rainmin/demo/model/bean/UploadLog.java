package com.rainmin.demo.model.bean;

import org.litepal.crud.LitePalSupport;

public class UploadLog extends LitePalSupport {

    private String uploadfilepath;
    private String sourceid;

    public String getUploadfilepath() {
        return uploadfilepath;
    }

    public void setUploadfilepath(String uploadfilepath) {
        this.uploadfilepath = uploadfilepath;
    }

    public String getSourceid() {
        return sourceid;
    }

    public void setSourceid(String sourceid) {
        this.sourceid = sourceid;
    }
}
