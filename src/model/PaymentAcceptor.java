package model;

public interface PaymentAcceptor {
    int getCurrentBalance();
    void addFunds(int amount);
    boolean deductFunds(int amount);
    String getPaymentMethodName();
}