package Thread;
 
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account {
    private int account_id;
    private volatile double balance;
    private Lock lock;

    public Account(int account_id, double balance) {
        this.account_id = account_id;
        this.balance = balance;
        this.lock = new ReentrantLock();
    }

    public int getAccountId() {
        return account_id;
    }

    public double getBalance() {
        return balance;
    }

    public void credit(double amount) {
        lock.lock();
        try {
            double newBalance = balance + amount;
            if (newBalance <= 10000000) {
                balance = newBalance;
                System.out.println("Credit successful. New balance: " + balance);
            } else {
                System.out.println("Credit failed. Account balance cannot exceed 10 million.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void debit(double amount) {
        lock.lock();
        try {
            double newBalance = balance - amount;
            if (newBalance >= 0) {
                balance = newBalance;
                System.out.println("Debit successful. New balance: " + balance);
            } else {
                System.out.println("Debit failed. insuficinat fund.");
            }
        } finally {
            lock.unlock();
        }
    }
}

class User {
    private int user_id;
    private String name;

    public User(int user_id, String name) {
        this.user_id = user_id;
        this.name = name;
    }

    public void doCredit(Account account, double amount) {
        account.credit(amount);
    }

    public void doDebit(Account account, double amount) {
        account.debit(amount);
    }
}

public class BankApp {
    public static void main(String[] args) {
        Account account1 = new Account(1, 50000);  // Account 1 of initial balance of 50 thousand
        Account account2 = new Account(2, 80000); // Account 2 of initial balance of 80 thousand
        Account account3 = new Account(3, 1000); // Account 3 of initial balance of 1 thousand
        
        // creating multiple user
        User user1 = new User(1, "usr1");
        User user2 = new User(2, "usr2");
        User user3 = new User(3, "usr3");
        User user4 = new User(4, "usr4");
        User user5 = new User(5, "usr5");
        User user6 = new User(6, "usr6");

        // multiple user can perform at a time;
        Thread thread1 = new Thread(() -> user1.doCredit(account1, 20000));
        Thread thread2 = new Thread(() -> user2.doDebit(account1, 10000));
        Thread thread3 = new Thread(() -> user3.doCredit(account2, 5000));
        Thread thread4 = new Thread(() -> user4.doDebit(account2, 30000));
        Thread thread5 = new Thread(() -> user5.doDebit(account1, 30000));
        Thread thread6 = new Thread(() -> user6.doDebit(account3, 30000));
        
        // multiple user perform at a time
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        }
}