package rvt.todolist;

import java.util.ArrayList;

public class TodoList {
    private ArrayList<String> tasks;

    public TodoList() {
        this.tasks = new ArrayList<>();
    }

    public void add(String task) {
        this.tasks.add(task);
    }

    public void print() {
        for (int i = 0; i < this.tasks.size(); i++) {
            // Indekss + 1, lai lietotājam rādītu sarakstu sākot no 1
            System.out.println((i + 1) + ": " + this.tasks.get(i));
        }
    }

    public void remove(int number) {
        // Lietotājs ievada skaitli (piem. 2), bet ArrayList tas ir indekss (2-1 = 1)
        if (number > 0 && number <= this.tasks.size()) {
            this.tasks.remove(number - 1);
        }
    }
}
