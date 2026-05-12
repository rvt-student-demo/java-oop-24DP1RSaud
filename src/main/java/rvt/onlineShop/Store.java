package rvt.onlineShop;

import java.util.Scanner;

public class Store {

    private Warehouse warehouse;
    private Scanner scanner;

    public Store(Warehouse warehouse, Scanner scanner) {
        this.warehouse = warehouse;
        this.scanner = scanner;
    }

    public void shop(String customer) {
        Shoppingcart cart = new Shoppingcart();
        System.out.println("Welcome to the store " + customer);
        System.out.println("our selection:");

        for (String product : this.warehouse.products()) {
            System.out.println(product);
        }

        while (true) {
            System.out.print("What to put in the cart (press enter to go to the register): ");
            String product = scanner.nextLine();
            if (product.isEmpty()) {
                break;
            }

            // Check if the product exists and has stock
            if (warehouse.stock(product) > 0) {
                warehouse.take(product);
                cart.add(product, warehouse.price(product));
                System.out.println(product + " added to cart!");
            } else {
                System.out.println("Product not available: " + product);
            }
        }

        System.out.println("your shoppingcart contents:");
        cart.print();
        System.out.println("total: " + cart.price());
    }
}
