package com.rainmin.demo.service;

import com.rainmin.demo.model.bean.UploadLog;

import org.litepal.LitePal;

import java.io.File;

public class UploadLogService {

    public UploadLogService() {

    }

    public void save(String sourceid, File uploadFile) {
        UploadLog log = new UploadLog();
        log.setSourceid(sourceid);
        log.setUploadfilepath(uploadFile.getAbsolutePath());
        log.save();
    }

    public void delete(File uploadFile) {
        LitePal.deleteAll(UploadLog.class, "uploadfilepath = ?", uploadFile.getAbsolutePath());
    }

    public String getBindId(File uploadFile) {
        UploadLog log = LitePal.where("uploadfilepath = ?", uploadFile.getAbsolutePath()).findFirst(UploadLog.class);
        return log != null ? log.getSourceid() : null;
    }
}
