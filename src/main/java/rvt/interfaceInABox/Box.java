package rvt.interfaceInABox;

import java.util.ArrayList;

public class Box implements Packable {
    private ArrayList<Packable> items;
    private double capacity;

    public Box(double capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    public void add(Packable item) {
        if (this.weight() + item.weight() <= this.capacity) {
            this.items.add(item);
        }
    }

    @Override
    public double weight() {
        double weight = 0;
        for (Packable item : this.items) {
            weight += item.weight();
        }
        return weight;
    }

    @Override
    public String toString() {
        return "Box: " + this.items.size() + " items, total weight " + this.weight() + " kg";
    }
}
