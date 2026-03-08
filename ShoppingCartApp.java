import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Mini Project:Console based E-Commerce Shopping Cart
 * Features:
 * --> Encapsulation for product details
 * --> Polymorphism for discounts
 * --> Interface for payment
 * --> ArrayList for managing cart
 */

public class ShoppingCartApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //taking number of products as input
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Product> cart = new ArrayList<>();
        // taking product details from user
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] parts = line.split("\\s+");
            String name = parts[0];
            double price = Double.parseDouble(parts[1]);
            int qty = Integer.parseInt(parts[2]);
            cart.add(new Product(name, price, qty));
        }
        String discountType = sc.nextLine().trim().toLowerCase();

        // Calculate total
        double total = 0.0;
        for (Product p : cart) {
            total += p.getPrice() * p.getQuantity();
        }

        // Apply discount
        Discount discount;
        if (discountType.equals("festive")) {
            discount = new FestiveDiscount();
        } else if (discountType.equals("bulk")) {
            discount = new BulkDiscount();
        } else {
            discount = new NoDiscount();
        }

        double discountedTotal = discount.applyDiscount(total, cart);
        Payment payment = new CashPayment();
        payment.pay(discountedTotal);

        // Print output
        for (Product p : cart) {
            System.out.println("Product: " + p.getName() +
                    ", Price: " + p.getPrice() +
                    ", Quantity: " + p.getQuantity());
        }
        System.out.printf("Total Amount Payable: %.2f%n", discountedTotal);

        sc.close();
    }
}

class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}
abstract class Discount {
    public abstract double applyDiscount(double total, List<Product> products);
}

class FestiveDiscount extends Discount {
    @Override
    public double applyDiscount(double total, List<Product> products) {
        return total * 0.9; // 10% off
    }
}

class BulkDiscount extends Discount {
    @Override
    public double applyDiscount(double total, List<Product> products) {
        boolean bulk = false;
        for (Product p : products) {
            if (p.getQuantity() > 5) {
                bulk = true;
                break;
            }
        }
        if (bulk) {
            return total * 0.8; // 20% off
        }
        return total;
    }
}

class NoDiscount extends Discount {
    @Override
    public double applyDiscount(double total, List<Product> products) {
        return total;
    }
}
interface Payment {
    void pay(double amount);
}

class CashPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.printf("Processing cash payment of: %.2f%n", amount);
    }
}
