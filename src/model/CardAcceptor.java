package model;

import java.util.Scanner;

public class CardAcceptor implements PaymentAcceptor {
    private int balance = 0;
    private boolean authorized;

    @Override
    public int getCurrentBalance() {
        return authorized ? balance : 0;
    }

    @Override
    public void addFunds(int amount) {
        if (!authorized) {
            authorize();
        }
        if (authorized && amount > 0) {
            balance += amount;
            System.out.println("С карты списано: " + amount);
        }
    }

    private void authorize() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите номер карты (16 цифр): ");
        String cardNumber = scanner.nextLine();
        System.out.print("Введите SMS-код: ");
        String smsCode = scanner.nextLine();


        authorized = cardNumber.matches("\\d{16}") && smsCode.matches("\\d{4,6}");

        if (authorized) {
            balance = 0;
            System.out.println("Карта авторизована");
        } else {
            System.out.println("Ошибка авторизации");
        }
    }

    @Override
    public boolean deductFunds(int amount) {
        return authorized && amount > 0 && balance >= amount && (balance -= amount) >= 0;
    }

    @Override
    public String getPaymentMethodName() {
        return "Банковская карта";
    }
}