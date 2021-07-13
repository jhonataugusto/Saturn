package br.com.main.task.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class UpdateEvent extends Event {

    private final int tick;

    public UpdateType getType() {
        if (tick % 20 == 0) {
            return UpdateType.SECOND;
        }
        if (tick % 1200 == 0) {
            return UpdateType.MINUTE;
        }
        return UpdateType.TICK;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public enum UpdateType {
        TICK, SECOND, MINUTE
    }
}
