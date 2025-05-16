package io.ntrm.edu;

import java.security.*;

public class Wallet {

    private final KeyPair keyPair;

    public Wallet() {
        keyPair = generateKeyPair();
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            keyGen.initialize(256);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Transaction send(PublicKey recipient, double amount, Blockchain blockchain) {
        double balance = blockchain.getBalance(publicKey());
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds!");
        }
        return new Transaction(publicKey(), recipient, amount, privateKey());
    }

    public PublicKey publicKey() {
        return keyPair.getPublic();
    }

    private PrivateKey privateKey() {
        return keyPair.getPrivate();
    }

}
