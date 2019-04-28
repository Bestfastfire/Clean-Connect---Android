package com.cleancode.cleanconnect.conexao;

public class uploadObj {
    private String file;
    private String url;
    private boolean delete;

    public uploadObj(String file, String url, boolean delete) {
        this.file = file;
        this.url = url;
        this.delete = delete;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
