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
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
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
        displayProductReceipt("--Product List--");
        for (Product product : inventory) {
            printProduct(product);
        }

        // Prompts user for product sku and validates input
        String sku;
        while (true) {
            System.out.print("\nEnter product sku to find product: ");
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

        // Checks if sku matches each product and adds matching products to cart
        boolean isFound = false;
        for (Product product : inventory) {
            if (product.getSku().equalsIgnoreCase(sku)) {
                cart.add(product);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No products matching sku: " + sku + ".");
        }
        System.out.println();
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

 