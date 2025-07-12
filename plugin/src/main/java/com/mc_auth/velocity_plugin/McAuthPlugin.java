package com.mc_auth.velocity_plugin;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;

@Plugin(
        id = "mc-auth-velocity-plugin",
        name = BuildConstants.NAME,
        version = BuildConstants.VERSION,
        authors = {"SpraxDev"},
        url = "https://github.com/Mc-Auth-com/"
)
public class McAuthPlugin {
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
