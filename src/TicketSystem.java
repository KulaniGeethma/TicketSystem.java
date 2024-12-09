import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketSystem {

    private static List<Thread> vendorThreads = new ArrayList<>();
    private static List<Thread> customerThreads = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int totalTickets = getAndValidateUserInputs(scanner, "Enter total tickets : ");
        int ticketReleaseRate = getAndValidateUserInputs(scanner, "Enter ticket release rate (ms) : ");
        int customerRetrievalRate = getAndValidateUserInputs(scanner, "Enter customer retrieval rate (ms): ");
        int maxTicketCapacity = getAndValidateUserInputs(scanner, "Enter max ticket capacity : ");

        Configuration configuration = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        saveConfiguration(configuration);
        TicketPool ticketPool = new TicketPool(maxTicketCapacity);



        int noOfVendors = getAndValidateUserInputs(scanner, "Enter number of vendors : ");
        int noOfCustomers = getAndValidateUserInputs(scanner, "Enter number of customers : ");

        // Start vendor threads
        for (int i = 1; i <= noOfVendors; i++) {
            int noOfTicketsPerVendor = configuration.getTotalTickets() / noOfVendors;
            if (i == noOfVendors - 1) {
                noOfTicketsPerVendor += configuration.getTotalTickets() % noOfVendors;
            }
            Vendor vendor = new Vendor(ticketPool, configuration.getTicketReleaseRate(), noOfTicketsPerVendor);
            Thread vendorThread = new Thread(vendor, "Vendor-" + i);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Start customer threads
        for (int i = 1; i <= noOfCustomers; i++) {
            Customer customer = new Customer(ticketPool, configuration.getCustomerRetrievalRate());
            Thread customerThread = new Thread(customer, "Customer-" + i);
            customerThreads.add(customerThread);
            customerThread.start();
        }


    }
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

}

