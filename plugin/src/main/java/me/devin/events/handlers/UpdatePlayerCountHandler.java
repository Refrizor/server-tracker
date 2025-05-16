package me.devin.events.handlers;

import me.devin.cache.GlobalPlayerCountCache;
import me.devin.ServerTrackerPlugin;
import me.devin.events.core.UpdateCountEvent;

public class UpdatePlayerCountHandler implements EventHandler<UpdateCountEvent> {

    public UpdatePlayerCountHandler() {
    }

    @Override
    public void handle(UpdateCountEvent event) {
        if(event.getSenderId().equalsIgnoreCase("api")){
            GlobalPlayerCountCache.updateGlobalPlayerCount(event.getVisible());
            ServerTrackerPlugin.getInstance().getLogger().severe("[Debug] Global player count: " + event.getVisible());
        }
    }

    @Override
    public Class<UpdateCountEvent> getPayloadType() {
        return UpdateCountEvent.class;
    }
}

