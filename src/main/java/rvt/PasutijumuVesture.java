package rvt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class PasutijumuVesture {
    public static void main(String[] args) {
        // Norādām faila nosaukumu
        String csvFile = "data/orders.csv";
        String line;
        String csvSplitBy = ",";
        double totalSum = 0.0;

        // Izmantojam try-with-resources, lai automātiski aizvērtu failu pēc nolasīšanas
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            // 3. Prasība: Izlaižam virsraksta rindu
            br.readLine();

            // 2. Prasība: Lasām failu rindu pa rindai
            while ((line = br.readLine()) != null) {
                // Sadalām rindu, izmantojot komatu kā atdalītāju
                String[] order = line.split(csvSplitBy);

                // Datu iegūšana un formatēšana (noņemam liekās atstarpes ar trim())
                String orderId = order[0].trim();
                String customer = order[1].trim();
                String product = order[2].trim();
                int quantity = Integer.parseInt(order[3].trim());
                double price = Double.parseDouble(order[4].trim());

                // Aprēķinām konkrētā pasūtījuma summu
                double orderTotal = quantity * price;

                // Pieskaitām kopējai summai
                totalSum += orderTotal;

                // 4. Prasība: Izdrukājam katru pasūtījumu prasītajā formātā
                // Izmantojam Locale.US, lai decimāldaļas atdalītājs būtu punkts (piem., 25.50)
                System.out.printf(Locale.US, "Pasūtījums #%s: %s pasūtīja %d x %s (%.2f EUR) -> Kopā: %.2f EUR%n",
                        orderId, customer, quantity, product, price, orderTotal);
            }

            // 5. Prasība: Aprēķina un izdrukā kopējo pasūtījumu summu
            System.out.println("--------------------------------------------------");
            System.out.printf(Locale.US, "Kopēja pasūtījumu summa: %.2f EUR%n", totalSum);

        } catch (IOException e) {
            System.out.println("Kļūda lasot failu: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Kļūda skaitļu formatējumā failā: " + e.getMessage());
        }
    }
}
