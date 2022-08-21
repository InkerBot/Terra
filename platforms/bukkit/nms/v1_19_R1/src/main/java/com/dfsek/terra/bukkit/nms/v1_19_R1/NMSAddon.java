package com.dfsek.terra.bukkit.nms.v1_19_R1;

import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.bukkit.BukkitAddon;
import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.VanillaBiomeProperties;


public class NMSAddon extends BukkitAddon {
    public NMSAddon(PlatformImpl terraBukkitPlugin) {
        super(terraBukkitPlugin);
    }
    
    @Override
    public void initialize() {
        terraBukkitPlugin.getEventManager()
                         .getHandler(FunctionalEventHandler.class)
                         .register(this, ConfigurationLoadEvent.class)
                         .then(event -> {
                             if(event.is(Biome.class)) {
                                 event.getLoadedObject(Biome.class).getContext().put(event.load(new VanillaBiomeProperties()));
                             }
                         })
                         .global();
    }
}
