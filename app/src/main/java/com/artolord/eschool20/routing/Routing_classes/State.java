package com.artolord.eschool20.routing.Routing_classes;

public class State {
    int userId = -1;
    String username;
    String prsFio;

    public boolean isNnull(){
        return (userId == -1) || (username==null)  || (prsFio == null);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrsFio(String prsFio) {
        this.prsFio = prsFio;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPrsFio() {
        return prsFio;
    }
}
