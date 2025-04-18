import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;

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
        coinAcceptor = new CoinAcceptor(100);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        print("Монет на сумму: " + coinAcceptor.getAmount());

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
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
                print("Введите сумму для пополнения:");
                int money = Integer.parseInt(fromConsole());
                if (money > 0) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() + money);
                    print("Баланс пополнен. Текущий баланс: " + coinAcceptor.getAmount());
                } else {
                    print("Ошибка: сумма должна быть больше 0!");
                }
            }
            else if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            }
            else {
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getActionLetter() == actionLetter) {
                        if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                            coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                            print("Вы купили " + products.get(i).getName());
                        } else {
                            print("Недостаточно средств!");
                        }
                        break;
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попробуйте еще раз.");
        }
    }




    private void showActions(UniversalArray<Product> products) {
        List<Product> allActions = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            allActions.add(products.get(i));
        }
        allActions.add(new Product("Пополнить баланс", ActionLetter.A, 0));
        allActions.sort(Comparator.comparing(p -> p.getActionLetter().getValue()));
        for (Product p : allActions) {
            print(String.format(" %s - %s", p.getActionLetter().getValue(), p.getName()));
        }
        print(" h - Выйти");
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
