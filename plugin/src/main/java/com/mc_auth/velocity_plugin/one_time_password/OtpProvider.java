package com.mc_auth.velocity_plugin.one_time_password;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mc_auth.velocity_plugin.DatabaseClient;

import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.UUID;

@Singleton
public class OtpProvider {
    @Inject
    private OtpGenerator otpGenerator;
    @Inject
    private AlternateOtpGenerator alternateOtpGenerator;

    @Inject
    private DatabaseClient databaseClient;

    public String provide(UUID playerUuid) throws SQLException, GeneralSecurityException {
        int otp = this.databaseClient.getOtp(playerUuid);
        if (otp == -1) {
            otp = this.otpGenerator.generate(playerUuid);
            this.databaseClient.setOtp(playerUuid, otp);
        }

        return OtpFormatter.formatOTP(otp);
    }

    public String provideAlternate(UUID playerUuid) throws GeneralSecurityException, SQLException {
        String otpPrefix = this.alternateOtpGenerator.generatePrefix();
        int otp = this.alternateOtpGenerator.generate();
        this.databaseClient.setAlternateOtp(playerUuid, otpPrefix, otp);

        return otpPrefix + " " + OtpFormatter.formatOTP(otp);
    }
}
