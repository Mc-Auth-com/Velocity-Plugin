package com.mc_auth.velocity_plugin;

import com.google.inject.Inject;
import com.mc_auth.velocity_plugin.one_time_password.OtpProvider;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

@Plugin(
        id = "mc-auth-velocity-plugin",
        name = BuildConstants.NAME,
        version = BuildConstants.VERSION,
        authors = {"SpraxDev"},
        url = "https://github.com/Mc-Auth-com/"
)
public class McAuthPlugin {
    @Inject
    private Logger logger;
    @Inject
    private OtpProvider otpProvider;
    @Inject
    private SettingsFile settingsFile;
    @Inject
    private DatabaseClient databaseClient;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {
        this.settingsFile.load();
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        try {
            this.databaseClient.updateAccount(event.getPlayer().getUniqueId(), event.getPlayer().getUsername());

            String formattedOtp = determineOtpToDisplay(event.getPlayer());
            this.logger.info("{} successfully requested an One-Time-Password", event.getPlayer().getUsername());

            Component successKickMessage = formatKickMessage(
                    this.settingsFile.kickSuccess.getValueAsString(),
                    event.getPlayer(),
                    formattedOtp
            );
            event.setResult(ResultedEvent.ComponentResult.denied(successKickMessage));
            return;
        } catch (GeneralSecurityException | SQLException ex) {
            this.logger.error("Failed to generate an OTP for {} ({})", event.getPlayer().getUsername(), event.getPlayer().getUniqueId(), ex);
        }

        Component kickMessage = formatKickMessage(this.settingsFile.kickError.getValueAsString(), event.getPlayer(), "");
        event.setResult(ResultedEvent.ComponentResult.denied(kickMessage));
    }

    private String determineOtpToDisplay(Player player) throws SQLException, GeneralSecurityException {
        if (playerConnectedUsingAlternateOtpDomain(player)) {
            return this.otpProvider.provideAlternate(player.getUniqueId());
        }
        return this.otpProvider.provide(player.getUniqueId());
    }

    private Component formatKickMessage(String messageTemplate, Player player, String oneTimePassword) {
        return MiniMessage
                .miniMessage()
                .deserialize(
                        messageTemplate,
                        Placeholder.unparsed("username", player.getUsername()),
                        Placeholder.unparsed("user_uuid", player.getUniqueId().toString()),
                        Placeholder.unparsed("one_time_password", oneTimePassword)
                );
    }

    private boolean playerConnectedUsingAlternateOtpDomain(Player player) {
        String alternateOtpHostname = this.settingsFile.alternateOtpHostname.getValueAsString();
        InetSocketAddress virtualHost = player.getVirtualHost().orElse(null);
        return virtualHost != null &&
                alternateOtpHostname != null &&
                !alternateOtpHostname.isEmpty() &&
                virtualHost.getHostString().equalsIgnoreCase(alternateOtpHostname);
    }
}
