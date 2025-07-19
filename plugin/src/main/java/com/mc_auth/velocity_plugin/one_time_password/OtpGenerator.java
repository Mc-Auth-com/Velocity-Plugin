package com.mc_auth.velocity_plugin.one_time_password;

import com.google.common.io.BaseEncoding;
import com.google.inject.Singleton;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Singleton
public class OtpGenerator {
    private final String secretSalt = TimeBasedOneTimePasswordUtil.generateBase32Secret();
    private MessageDigest sha256;

    public int generate(UUID uuid) throws GeneralSecurityException {
        return TimeBasedOneTimePasswordUtil.generateCurrentNumber(generateSecret(uuid), 6);
    }

    private String generateSecret(UUID uuid) throws NoSuchAlgorithmException {
        if (this.sha256 == null) {
            this.sha256 = MessageDigest.getInstance("SHA-256");
        }

        return BaseEncoding.base32().encode(this.sha256.digest((uuid.toString() + this.secretSalt).getBytes()));
    }
}
