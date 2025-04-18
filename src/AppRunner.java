import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {
    private final UniversalArray<Product> products = new UniversalArrayImpl<>();
    private PaymentAcceptor paymentAcceptor;
    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        if (paymentAcceptor == null) {
            selectPaymentMethod();
        }

        print("\nВ автомате доступны:");
        showProducts(products);
        print(paymentAcceptor.getPaymentMethodName() + " на сумму: " + paymentAcceptor.getCurrentBalance());

        UniversalArray<Product> allowProducts = getAllowedProducts();
        chooseAction(allowProducts);
    }

    private void selectPaymentMethod() {
        print("\nВыберите способ оплаты:");
        print(" 1 - Монеты");
        print(" 2 - Банковская карта");
        print("Выберите вариант:");

        String choice = fromConsole();
        switch (choice) {
            case "1":
                paymentAcceptor = new CoinAcceptor(100);
                break;
            case "2":
                paymentAcceptor = new CardAcceptor();
                break;
            default:
                print("Некорректный выбор, попробуйте снова");
                selectPaymentMethod();
        }
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (paymentAcceptor.getCurrentBalance() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        showActions(products);
        String action = fromConsole().substring(0, 1);

        try {
            ActionLetter actionLetter = ActionLetter.valueOf(action.toUpperCase());

            if (actionLetter == ActionLetter.A) {
                handleAddFunds();
            }
            else if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            }
            else {
                handleProductPurchase(products, actionLetter);
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая команда. Попробуйте еще раз.");
        }
    }

    private void handleAddFunds() {
        print("Введите сумму для пополнения:");
        try {
            int amount = Integer.parseInt(fromConsole());
            if (amount > 0) {
                paymentAcceptor.addFunds(amount);
                print("Баланс пополнен. Текущий баланс: " + paymentAcceptor.getCurrentBalance());
            } else {
                print("Сумма должна быть положительной!");
            }
        } catch (NumberFormatException e) {
            print("Ошибка: введите число!");
        }
    }

    private void handleProductPurchase(UniversalArray<Product> products, ActionLetter actionLetter) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getActionLetter() == actionLetter) {
                int price = products.get(i).getPrice();
                if (paymentAcceptor.deductFunds(price)) {
                    print("Вы купили " + products.get(i).getName());
                } else {
                    print("Недостаточно средств!");
                }
                return;
            }
        }
        print("Товар не найден");
    }

    private void showActions(UniversalArray<Product> products) {
        print("\nДоступные действия:");
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s (%d)",
                    products.get(i).getActionLetter(),
                    products.get(i).getName(),
                    products.get(i).getPrice()));
        }
        print(" A - Пополнить баланс");
        print(" H - Выйти");
        print("Выберите действие:");
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}