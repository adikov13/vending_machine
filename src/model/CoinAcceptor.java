package model;

public class CoinAcceptor implements PaymentAcceptor {
    private int balance;

    public CoinAcceptor(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public int getCurrentBalance() {
        return balance;
    }

    @Override
    public void addFunds(int amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Принято монет на сумму: " + amount);
        }
    }

    @Override
    public boolean deductFunds(int amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String getPaymentMethodName() {
        return "Монеты";
    }
}