package controllers;

import beauty_app.BeautyApp;

public class ClientController {
    public void newEntry() {
        BeautyApp.setEntry();
    }

    public void auth() {
        BeautyApp.setAuth();
    }
}
