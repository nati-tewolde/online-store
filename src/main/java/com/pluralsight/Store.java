package com.pluralsight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {

    public static void main(String[] args) {

        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        loadInventory("products.csv", inventory);

        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("\nInvalid choice: please enter 1, 2, or 3.");
                scanner.nextLine();
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> displayProducts(inventory, cart, scanner);
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("\nThank you for shopping with us!");
                default -> System.out.println("\nInvalid choice!");
            }
        }
        scanner.close();
    }

    /**
     * Reads product data from a file and populates the inventory list
     */
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        try {
            // Creates new file object if file isn't found
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            // Reads each line and splits line based on pipe location
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Validates line content
                if (parts.length != 4) {
                    System.out.println("\nError extracting file content, please check " +
                            fileName + " for corrupted data.");
                    continue;
                }

                // Extracts necessary fields
                String sku = parts[0];
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                String department = parts[3];

                inventory.add(new Product(sku, name, price, department));
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println("\nError reading file.");
        }

    }

    /**
     * Displays all products and lets the user add one to the cart, returns if user enters X
     */
    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {
        if (inventory.isEmpty()) {
            System.out.println("\nProduct inventory is currently empty.");
            return;
        }

        // Display product list
        displayProductReceipt("--Products--");
        for (Product product : inventory) {
            printProduct(product);
        }

        // Prompt user for product sku and validate input
        String sku;
        while (true) {
            System.out.print("\nEnter product sku to add item to cart (X to return): ");
            sku = scanner.nextLine().trim();
            if (sku.isBlank()) {
                System.out.println("\nProduct sku cannot be empty.");
                continue;
            }
            if (sku.equalsIgnoreCase("x")) {
                return;
            }
            break;
        }

        // Check if sku matches each product and add matching products to cart
        Product foundProduct = findProductById(sku, inventory);

        if (foundProduct != null) {
            cart.add(foundProduct);
            System.out.println("\nSuccessfully added \"" + foundProduct.getName() + "\" to cart!");
        } else {
            System.out.println("\nNo products matching sku: " + sku + ".");
        }
    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is currently empty.");
            return;
        }

        // Compute total amount and display products in cart with subtotal
        double totalAmount = 0;
        displayProductReceipt("--Cart--");
        for (Product product : cart) {
            printProduct(product);
            totalAmount += product.getPrice();
        }
        System.out.println("-".repeat(60));
        System.out.printf("%-5s %-40s | %8s%n", "Subtotal", " ", String.format("$%.2f", totalAmount));

        // Prompt user to check out or return
        while (true) {
            System.out.print("\nEnter C if you'd like to check out, X to return: ");
            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("x")) {
                return;
            } else if (!choice.equalsIgnoreCase("c")) {
                System.out.println("\nInvalid choice, please enter C or X.");
                continue;
            }
            checkOut(cart, totalAmount, scanner);
            break;
        }
    }

    /**
     * Prompts user for payment, calculates change and displays sales receipt
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        // Prompt user for payment amount and validate input
        double payment;
        while (true) {
            System.out.print("\nEnter payment amount: ");
            if (!scanner.hasNextDouble()) {
                System.out.println("\nInvalid amount, please enter a valid payment amount.");
                scanner.nextLine();
                continue;
            }
            payment = scanner.nextDouble();
            if (payment < totalAmount) {
                System.out.println("\nPayment amount cannot be less than order subtotal.");
                continue;
            }
            break;
        }

        // Calculate change and display sales receipt with change
        double change = totalAmount - payment;
        displayProductReceipt("--Sales Receipt--");
        for (Product product : cart) {
            printProduct(product);
        }
        System.out.println("-".repeat(60));
        System.out.printf("%-5s %-40s | %8s%n", "Subtotal", " ", String.format("$%.2f", totalAmount));
        System.out.printf("%-5s %-43s | %8s%n", "Cash", " ", String.format("$%.2f", payment));
        System.out.printf("%-5s %-42s | %8s%n", "Change", " ", String.format("$%.2f", Math.abs(change)));
        System.out.println("\nThank you for your purchase!\n");

        cart.clear();
    }

    /**
     * Searches a list for a product by its id.
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        for (Product product : inventory) {
            if (product.getSku().equalsIgnoreCase(id)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Helper method to iterate through and print products in given arraylist
     */
    private static void printProduct(Product product) {
        System.out.printf("%-5s | %-40s | %8s%n",
                product.getSku(),
                product.getName(),
                String.format("$%.2f", product.getPrice()));
    }


    /**
     * Helper method to format and print product receipt
     */
    private static void displayProductReceipt(String title) {
        System.out.printf("%n%40s%n%n", title);
        System.out.printf("%-6s | %-40s | %8s%n",
                "SKU", "Product Name", "Price");
        System.out.println("-".repeat(60));
    }
}

 