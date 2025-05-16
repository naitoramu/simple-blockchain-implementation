package io.ntrm.edu;

public class Main {

    public static void main(String[] args) {
        // Initialize blockchain with difficulty level
        Blockchain blockchain = new Blockchain(4);

        // Create wallets
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet walletC = new Wallet();

        // Display initial balances
        System.out.println("Initial balance of Wallet A: " + blockchain.getBalance(walletA.publicKey()));
        System.out.println("Initial balance of Wallet B: " + blockchain.getBalance(walletB.publicKey()));
        System.out.println("Initial balance of Wallet C: " + blockchain.getBalance(walletC.publicKey()));

        // Mine the first block to reward Wallet A
        System.out.println("\nWallet A is mining block...");
        blockchain.minePendingTransactions(walletA.publicKey());
        System.out.println("Balance of Wallet A after mining: " + blockchain.getBalance(walletA.publicKey()));

        // Wallet A sends funds to Wallet B
        System.out.println("\nWallet A sends 50 units to Wallet B...");
        Transaction transaction1 = walletA.send(walletB.publicKey(), 50, blockchain);
        blockchain.addTransaction(transaction1);

        // Mine the second block
        System.out.println("\nWallet C is mining block...");
        blockchain.minePendingTransactions(walletC.publicKey());

        // Display balances after the transaction
        System.out.println("Balance of Wallet A: " + blockchain.getBalance(walletA.publicKey()));
        System.out.println("Balance of Wallet B: " + blockchain.getBalance(walletB.publicKey()));
        System.out.println("Balance of Wallet C: " + blockchain.getBalance(walletC.publicKey()));

        // Attempt an invalid transaction (insufficient funds)
        System.out.println("\nWallet A attempts to send 300 units to Wallet B...");
        try {
            Transaction transaction2 = walletA.send(walletB.publicKey(), 300, blockchain);
            blockchain.addTransaction(transaction2);
        } catch (IllegalArgumentException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }

        // Mine the third block
        System.out.println("\nWallet C is mining block...");
        blockchain.minePendingTransactions(walletC.publicKey());

        // Final balances
        System.out.println("\nFinal balance of Wallet A: " + blockchain.getBalance(walletA.publicKey()));
        System.out.println("Final balance of Wallet B: " + blockchain.getBalance(walletB.publicKey()));
        System.out.println("Final balance of Wallet C: " + blockchain.getBalance(walletC.publicKey()));

        // Validate the blockchain
        System.out.println("\nIs blockchain valid? " + blockchain.isChainValid());
    }
}