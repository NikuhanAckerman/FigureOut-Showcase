package com.project.figureout;

import lombok.Getter;
import lombok.Setter;

public class ClientNavigator {
    private static final ClientNavigator clientNavigator = new ClientNavigator();

    private volatile long clientId = 0;

    public static ClientNavigator getInstance() {
        return clientNavigator;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
