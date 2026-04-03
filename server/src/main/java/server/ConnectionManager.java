package server;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String username, Session session) {
        if (!connections.containsKey(gameID)) {
            connections.put(gameID, new ConcurrentHashMap<>());
        }

        connections.get(gameID).put(username, session);
    }

    public void remove(int gameID, String username, Session session) {
        if (connections.containsKey(gameID)) {
            connections.get(gameID).remove(username, session);
        }
    }

    public void broadcast(int gameID, Session excludeSession, ServerMessage notification) throws IOException {
        String msg = notification.toString();

        var gameConnections = connections.get(gameID);

        if (gameConnections != null) {
            for (Session c : gameConnections.values()) {
                if (c.isOpen()) {
                    if (!c.equals(excludeSession)) {
                        c.getRemote().sendString(msg);
                    }
                }
            }
        }
    }

}
