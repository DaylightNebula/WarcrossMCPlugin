package daylightnebula.warcrossmcplugin.utils;

import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class ArrowManager {
    public static Map<Arrow, ArrowRunnable> arrowEvents = new HashMap<>();

    public static void RegisterArrow(Arrow arrow, ArrowRunnable runnable) {
        arrowEvents.put(arrow, runnable);
    }

    public abstract static class ArrowRunnable {
        public abstract void run(EntityDamageByEntityEvent event);
    }
}
