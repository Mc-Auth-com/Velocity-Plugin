package com.mc_auth.velocity_plugin;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import de.sprax2013.lime.configuration.Config;
import de.sprax2013.lime.configuration.ConfigEntry;
import de.sprax2013.lime.configuration.validation.IntEntryValidator;
import de.sprax2013.lime.configuration.validation.StringEntryValidator;

import java.io.IOException;
import java.nio.file.Path;

@Singleton
public class SettingsFile {
    private final Config config = new Config(
            null,
            """
                    Mc-Auth.com (Velocity Plugin)
                    
                    Support: https://Sprax.me/Discord
                    More details about the plugin: https://github.com/Mc-Auth-com/Velocity-Plugin"""
    )
            .withCommentEntry("PostgreSQL", "This plugins requires access to the same database as the Mc-Auth backend");

    public final ConfigEntry psqlHost = this.config.createEntry("PostgreSQL.Host", "127.0.0.1")
            .setEntryValidator(StringEntryValidator.get());
    public final ConfigEntry psqlPort = this.config.createEntry("PostgreSQL.Port", 5432)
            .setEntryValidator(IntEntryValidator.get());
    public final ConfigEntry psqlDatabase = this.config.createEntry("PostgreSQL.Database", "mcauth")
            .setEntryValidator(StringEntryValidator.get());

    public final ConfigEntry psqlUser = this.config.createEntry("PostgreSQL.Username", "mcauth")
            .setEntryValidator(StringEntryValidator.get());
    public final ConfigEntry psqlPassword = this.config.createEntry("PostgreSQL.Password", "v3ryS3cr3t")
            .setEntryValidator(StringEntryValidator.get());

    public final ConfigEntry alternateOtpHostname = this.config.createEntry(
                    "AlternateOtp.Hostname", "mc-alt.mc-auth.com",
                    "The hostname to use for the alternate OTP mode. This is used to determine if a player is in alternate OTP mode.")
            .setEntryValidator(StringEntryValidator.get());

    public final ConfigEntry kickSuccess = this.config.createEntry(
                    "Kick.Success", """
                            <dark_aqua><u>Mc-Auth.com</u></dark_aqua>
                            
                            <gold><username></gold><yellow>'s authentication code</yellow>
                            
                            <gold><one_time_password></gold>
                            """,
                    "Available variables: <username>, <user_uuid>, <one_time_password>")
            .setEntryValidator(StringEntryValidator.get());
    public final ConfigEntry kickError = this.config.createEntry(
                    "Kick.Error",
                    """
                            <dark_aqua><u>Mc-Auth.com</u></dark_aqua>
                            
                            <red>An error occurred!
                            Please try again shortly or contact Sprax</red>
                            
                            <dark_aqua>https://sprax2013.de</dark_aqua>""",
                    "Available variables: <username>, <user_uuid>")
            .setEntryValidator(StringEntryValidator.get());

    @Inject
    public SettingsFile(@DataDirectory Path dataDirectory) {
        this.config.setFile(dataDirectory.resolve("config.yml").toFile());
    }

    public void load() throws IOException {
        this.config.loadWithException();
        this.config.saveWithException();
    }
}
