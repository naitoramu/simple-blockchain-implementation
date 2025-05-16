package io.ntrm.edu;

import io.ntrm.edu.utils.StringUtil;

import java.util.Date;
import java.util.List;

public class Block {

    private String hash;
    private final String previousHash;
    private final List<Transaction> transactions;
    private final long timeStamp;
    private int nonce;

    public Block(String previousHash, List<Transaction> transactions) {
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.timeStamp = new Date().getTime();
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        StringBuilder txData = new StringBuilder();
        for (Transaction tx : transactions) {
            txData.append(tx.getTransactionHash());
        }
        String input = previousHash + txData + timeStamp + nonce;
        return StringUtil.applySha256(input);
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Data block mined! Hash: " + hash);
    }

    public String getHash() { return hash; }
    public String getPreviousHash() { return previousHash; }
    public List<Transaction> getTransactions() { return transactions; }
}
