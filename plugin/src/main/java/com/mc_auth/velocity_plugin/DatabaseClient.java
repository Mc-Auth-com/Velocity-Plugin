package com.mc_auth.velocity_plugin;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Singleton
public class DatabaseClient {
    @Inject
    private SettingsFile settingsFile;

    private Connection con;

    public void updateAccount(UUID playerUuid, String name) throws SQLException {
        try (PreparedStatement ps = getConnection()
                .prepareStatement("INSERT INTO accounts(id,name,last_login) VALUES(?,?,CURRENT_TIMESTAMP) " +
                        "ON CONFLICT(id) DO UPDATE SET name=?,last_login=CURRENT_TIMESTAMP;")) {
            ps.setString(1, playerUuid.toString().replace("-", ""));
            ps.setString(2, name);
            ps.setString(3, name);

            ps.executeUpdate();
        }
    }

    public int getOtp(UUID playerUuid) throws SQLException {
        try (PreparedStatement ps = getConnection()
                .prepareStatement("SELECT code FROM otps WHERE account =? AND issued >= CURRENT_TIMESTAMP - INTERVAL '5 MINUTES';")) {
            ps.setString(1, playerUuid.toString().replace("-", ""));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("code");
            }
        }

        return -1;
    }

    public void setOtp(UUID playerUuid, int otp) throws SQLException {
        try (PreparedStatement ps = getConnection()
                .prepareStatement("INSERT INTO otps VALUES(?,?) ON CONFLICT(account) DO UPDATE SET (code,issued) = (?,CURRENT_TIMESTAMP);")) {
            ps.setString(1, playerUuid.toString().replace("-", ""));
            ps.setInt(2, otp);
            ps.setInt(3, otp);

            ps.executeUpdate();
        }
    }

    public void setAlternateOtp(UUID playerUuid, String otpPrefix, int otp) throws SQLException {
        try (PreparedStatement ps = getConnection()
                .prepareStatement("INSERT INTO alternate_otps VALUES(?,?,?) ON CONFLICT(account) DO UPDATE SET (code_prefix,code,issued) = (?,?,CURRENT_TIMESTAMP);")) {
            ps.setString(1, playerUuid.toString().replace("-", ""));
            ps.setString(2, otpPrefix);
            ps.setInt(3, otp);
            ps.setString(4, otpPrefix);
            ps.setInt(5, otp);

            ps.executeUpdate();
        }
    }

    public boolean isValid() throws SQLException {
        return this.con != null && !this.con.isClosed() && this.con.isValid(3);
    }

    public Connection getConnection() throws SQLException {
        if (!isValid()) {
            this.con = null;

            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Unable to load PostgreSQL JDBC driver", ex);
            }

            String url = "jdbc:postgresql://" + this.settingsFile.psqlHost.getValueAsString() +
                    ":" + this.settingsFile.psqlPort.getValueAsInt() +
                    "/" + this.settingsFile.psqlDatabase.getValueAsString();
            String user = this.settingsFile.psqlUser.getValueAsString();
            String password = this.settingsFile.psqlPassword.getValueAsString();

            this.con = DriverManager.getConnection(url, user, password);
        }

        return this.con;
    }
}
