package rvt.onlineShop;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Warehouse {

    private Map<String, Integer> prices;
    private Map<String, Integer> stock;

    public Warehouse() {
        this.prices = new HashMap<>();
        this.stock = new HashMap<>();
    }

    public void addProduct(String product, int price, int stock) {
        this.prices.put(product, price);
        this.stock.put(product, stock);
    }

    public int price(String product) {
        return this.prices.getOrDefault(product, -99);
    }

    public int stock(String product) {
        return this.stock.getOrDefault(product, 0);
    }

    public boolean take(String product) {
        if (this.stock(product) <= 0) {
            return false;
        }
        this.stock.put(product, this.stock(product) - 1);
        return true;
    }

    public Set<String> products() {
        return this.prices.keySet();
    }
}
