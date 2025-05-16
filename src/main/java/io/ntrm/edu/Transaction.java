package io.ntrm.edu;

import io.ntrm.edu.utils.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class Transaction {

    private final String transactionHash;
    private final PublicKey sender;
    private final PublicKey recipient;
    private final double amount;
    private final byte[] signature;

    public Transaction(PublicKey sender, PublicKey recipient, double amount, PrivateKey privateKey) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.transactionHash = calculateHash();
        if (sender.equals(Blockchain.systemPublicKey())) {
            this.signature = new byte[0];
        } else {
            this.signature = sign(privateKey);
        }
    }

    private String calculateHash() {
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender),
                StringUtil.getStringFromKey(recipient),
                String.valueOf(amount)
        );
    }

    private byte[] sign(PrivateKey privateKey) {
        try {
            Signature ecdsa = Signature.getInstance("SHA256withECDSA");
            ecdsa.initSign(privateKey);
            ecdsa.update(transactionHash.getBytes());
            return ecdsa.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifySignature() {
        if (sender.equals(Blockchain.systemPublicKey())) {
            return true;
        }
        try {
            Signature ecdsa = Signature.getInstance("SHA256withECDSA");
            ecdsa.initVerify(sender);
            ecdsa.update(transactionHash.getBytes());
            return ecdsa.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public PublicKey getSender() {
        return sender;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public double getAmount() {
        return amount;
    }
}
