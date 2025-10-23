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
            System.out.print("\nEnter product sku to add item to cart: ");
            sku = scanner.nextLine().trim();
            if (sku.isBlank()) {
                System.out.println("\nProduct id cannot be empty.");
                continue;
            }
            if (sku.equalsIgnoreCase("x")) {
                return;
            }
            break;
        }

        // Check if sku matches each product and add matching products to cart
        boolean isFound = false;
        String productName = "";
        for (Product product : inventory) {
            if (product.getSku().equalsIgnoreCase(sku)) {
                productName = product.getName();
                cart.add(product);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("\nNo products matching sku: " + sku + ".");
        } else {
            System.out.println("\nSuccessfully added \"" + productName + "\" to cart!");
        }
    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        // TODO:
        //   • list each product in the cart
        //   • compute the total cost
        //   • ask the user whether to check out (C) or return (X)
        //   • if C, call checkOut(cart, totalAmount, scanner)

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
        System.out.printf("%-5s %-40s | %8.2f%n", "Subtotal", " ", totalAmount);

        // Prompt user to check out or return
        String choice = "";
        while (!choice.equalsIgnoreCase("x")) {
            System.out.print("\nEnter C if you'd like to check out, X to return: ");
            choice = scanner.nextLine().trim();
            if (!choice.equalsIgnoreCase("c")) {
                System.out.println("\nInvalid choice, please enter C or X.");
                continue;
            }
            checkOut(cart, totalAmount, scanner);
            // Break?
        }

    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        // TODO: implement steps listed above
    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // TODO: loop over the list and compare ids
        // METHOD NECESSARY?
        return null;
    }


    /**
     * Helper method to iterate through and print products in given arraylist
     */
    private static void printProduct(Product product) {
        System.out.printf("%-5s | %-40s | %8.2f%n",
                product.getSku(),
                product.getName(),
                product.getPrice());
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

 