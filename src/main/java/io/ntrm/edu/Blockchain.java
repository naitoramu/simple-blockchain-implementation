package io.ntrm.edu;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private static final int MINING_REWARD = 100;
    private static final KeyPair SYSTEM_KEY_PAIR = generateSystemKeyPair();


    private final List<Block> blockchain;
    private final List<Transaction> pendingTransactions;
    private final int difficulty;

    public Blockchain(int difficulty) {
        this.blockchain = new ArrayList<>();
        this.pendingTransactions = new ArrayList<>();
        this.difficulty = difficulty;

        Block genesisBlock = new Block("0", List.of());
        genesisBlock.mineBlock(difficulty);
        blockchain.add(genesisBlock);
    }

    private static KeyPair generateSystemKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            keyGen.initialize(256);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create system private key", e);
        }
    }

    public void addTransaction(Transaction transaction) {
        double senderBalance = getBalance(transaction.getSender());
        if (transaction.getAmount() > senderBalance) {
            System.out.println("Transaction rejected: Insufficient funds!");
            return;
        }
        if (transaction.verifySignature()) {
            pendingTransactions.add(transaction);
        } else {
            System.out.println("Invalid transaction!");
        }
    }


    public void minePendingTransactions(PublicKey miningRewardRecipient) {
        Transaction rewardTx = new Transaction(
                systemPublicKey(),
                miningRewardRecipient,
                MINING_REWARD,
                systemPrivateKey()
        );
        List<Transaction> transactionsToMine = new ArrayList<>(pendingTransactions);
        transactionsToMine.add(rewardTx);

        Block newBlock = new Block(blockchain.getLast().getHash(), transactionsToMine);
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);

        pendingTransactions.clear();
    }

    public double getBalance(PublicKey walletPublicKey) {
        double balance = 0;
        for (Block block : blockchain) {
            for (Transaction tx : block.getTransactions()) {
                if (tx.getRecipient().equals(walletPublicKey)) {
                    balance += tx.getAmount();
                }
                if (tx.getSender() != null && tx.getSender().equals(walletPublicKey)) {
                    balance -= tx.getAmount();
                }
            }
        }
        return balance;
    }

    public boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    private static PrivateKey systemPrivateKey() {
        return SYSTEM_KEY_PAIR.getPrivate();
    }

    public static PublicKey systemPublicKey() {
        return SYSTEM_KEY_PAIR.getPublic();
    }

}
