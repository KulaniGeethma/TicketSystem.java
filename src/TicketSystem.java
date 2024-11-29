import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class TicketSystem {

    public static int getAndValidateUserInputs(Scanner scanner, String prompt) {
        int input = -1;
        while (input <= 0) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input <= 0) {
                    System.out.println("Please enter a positive number.");
                }
            } else {
                System.out.println("Please enter a valid positive number");
                scanner.next();
            }
        }

        return input;
    }

    private static void saveConfiguration(Configuration configuration) {
        Gson gson = new Gson();
        try {
            FileWriter fileWriter = new FileWriter("ticket_config.json");
            gson.toJson(configuration, fileWriter);
            System.out.println("Configuration saved successfully");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error saving configuration");
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int totalTickets = getAndValidateUserInputs(scanner, "Enter total tickets : ");
        int ticketReleaseRate = getAndValidateUserInputs(scanner, "Enter ticket release rate : ");
        int customerRetrievalRate = getAndValidateUserInputs(scanner, "Enter customer retrieval rate : ");
        int maxTicketCapacity = getAndValidateUserInputs(scanner, "Enter max ticket capacity : ");

        Configuration configuration = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        saveConfiguration(configuration);

        TicketPool ticketPool = new TicketPool(maxTicketCapacity,totalTickets);

        Vendor vendor = new Vendor(ticketPool,ticketReleaseRate);
        Thread vendorThread = new Thread(vendor);
        vendorThread.start();

        for(int i = 0; i <=  customerRetrievalRate; i++){
            Customer customer = new Customer(ticketPool);
            Thread customerThread = new Thread(customer);
            customerThread.start();
        }
    }
}

