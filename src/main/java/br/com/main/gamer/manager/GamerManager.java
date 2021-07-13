package br.com.main.gamer.manager;

import br.com.main.gamer.Gamer;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class GamerManager {

    private final HashMap<UUID, Gamer> gamers;

    public GamerManager() {
        gamers = new HashMap<>();
    }

    public void loadGamer(UUID uniqueId, Gamer gamer) {
        gamers.put(gamer.getUniqueId(), gamer);
    }

    public void unloadGamer(UUID uniqueId) {
        gamers.remove(uniqueId);
    }

    public Collection<Gamer> getGamers() {
        return gamers.values();
    }

    public Gamer getGamer(UUID uniqueId) {
        return gamers.get(uniqueId);
    }
}
