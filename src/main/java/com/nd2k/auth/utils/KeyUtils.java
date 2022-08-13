package com.nd2k.auth.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Objects;

@Component
@Slf4j
public class KeyUtils {

    private final Environment environment;
    private final String accessTokenPrivateKeyPath;
    private final String accessTokenPublicKeyPath;
    private final String refreshTokenPrivateKeyPath;
    private final String refreshTokenPublicKeyPath;

    private KeyPair accessTokenKeyPair;
    private KeyPair refreshTokenKeyPair;

    public KeyUtils(Environment environment,
                    @Value("${security.access-token.private}") String accessTokenPrivateKeyPath,
                    @Value("${security.access-token.public}") String accessTokenPublicKeyPath,
                    @Value("${security.refresh-token.private}") String refreshTokenPrivateKeyPath,
                    @Value("${security.refresh-token.public}")String refreshTokenPublicKeyPath) {
        this.environment = environment;
        this.accessTokenPrivateKeyPath = accessTokenPrivateKeyPath;
        this.accessTokenPublicKeyPath = accessTokenPublicKeyPath;
        this.refreshTokenPrivateKeyPath = refreshTokenPrivateKeyPath;
        this.refreshTokenPublicKeyPath = refreshTokenPublicKeyPath;
    }

    private KeyPair getAccessTokenKeyPair() {
        if (Objects.isNull(accessTokenKeyPair)) {
            accessTokenKeyPair = getKeyPair(accessTokenPublicKeyPath, accessTokenPrivateKeyPath);
        }
        return accessTokenKeyPair;
    }

    private KeyPair getRefreshTokenKeyPair() {
        if (Objects.isNull(refreshTokenKeyPair)) {
            refreshTokenKeyPair = getKeyPair(refreshTokenPublicKeyPath, refreshTokenPrivateKeyPath);
        }
        return refreshTokenKeyPair;
    }

    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {
        KeyPair keyPair;
        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);
        if (publicKeyFile.exists() && privateKeyFile.exists()) {
            log.info("Loading keys from file: {}, {}", publicKeyFile, privateKeyFile);
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
                keyPair = new KeyPair(publicKey, privateKey);
                return keyPair;
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                throw new RuntimeException("Public & Private keys are not existing");
            }
        }

        File directory = new File("access-refresh-token-keys");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            log.info("Generating new public and private keys: {}, {}", publicKeyPath, privateKeyPath);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
            try (FileOutputStream fileOutputStream = new FileOutputStream(publicKeyPath)) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                fileOutputStream.write(keySpec.getEncoded());
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(privateKeyFile)) {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                fileOutputStream.write(keySpec.getEncoded());
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
        return keyPair;
    }

    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }
    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }
    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
    }
    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
    }
}
