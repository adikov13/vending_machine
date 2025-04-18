package model;

public class MoneyAcceptor {
    private int amount;

    public MoneyAcceptor(int amount) {
        this.amount = amount;
    }

    public int getAmountForMoney() {
        return amount;
    }

    public void setAmountForMoney(int amount) {
        this.amount = amount;
    }
}
