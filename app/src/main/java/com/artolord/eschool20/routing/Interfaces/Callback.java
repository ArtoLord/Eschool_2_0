package com.artolord.eschool20.routing.Interfaces;

import com.artolord.eschool20.routing.Routing_classes.Unit;

import java.util.ArrayList;


public interface Callback<T> {
    void callback (T callback, Object... args);
    void onError(Integer errIndex);
}
