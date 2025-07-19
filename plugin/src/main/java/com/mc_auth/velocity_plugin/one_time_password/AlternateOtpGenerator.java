package com.mc_auth.velocity_plugin.one_time_password;

import com.google.common.io.BaseEncoding;
import com.google.inject.Singleton;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

@Singleton
public class AlternateOtpGenerator {
    private final String secretSalt = TimeBasedOneTimePasswordUtil.generateBase32Secret();
    private MessageDigest sha256;
    private final SecureRandom secureRandom = new SecureRandom();

    public int generate() throws GeneralSecurityException {
        return TimeBasedOneTimePasswordUtil.generateCurrentNumber(generateSecret(UUID.randomUUID()), 6);
    }

    public String generatePrefix() {
        String letters = "ABCDEFGHJKMNOPQRSTUVWXYZ";
        return String.valueOf(letters.charAt(this.secureRandom.nextInt(letters.length())));
    }

    private String generateSecret(UUID uuid) throws NoSuchAlgorithmException {
        if (this.sha256 == null) {
            this.sha256 = MessageDigest.getInstance("SHA-256");
        }

        return BaseEncoding.base32().encode(this.sha256.digest((uuid.toString() + this.secretSalt).getBytes()));
    }
}
