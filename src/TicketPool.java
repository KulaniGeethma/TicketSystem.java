import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {

    private final List<Integer> tickets = Collections.synchronizedList(new ArrayList<>());
    private final int maxTicketCapacity;
    private final  int totalTickets;
    private   int totalTicketReleased;

    public TicketPool(int maxTicketCapacity, int totalTickets) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
        this.totalTicketReleased = 0;
    }

    public synchronized void addTickets(int ticketCount) {
        if (this.totalTicketReleased >= totalTickets) {
            System.out.println("All tickets have been sold. No more tickets to release.");
            return;
        }
        while (tickets.size() + ticketCount > this.maxTicketCapacity) {
            System.out.println("Ticket pool is full. Vendor is waiting.");
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Add tickets interrupted.");
            }
        }
        this.totalTicketReleased += ticketCount;
        for (int i = 0; i <= ticketCount; i++) {
            tickets.add(1);
        }
        System.out.println("Vendor added " + ticketCount + " tickets to the pool.");
        notifyAll();
    }

    public synchronized void removeTickets() {
        while (tickets.isEmpty()) {
            System.out.println("No tickets are available to purchase. Customer is waiting.");
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Remove tickets interrupted.");
            }
        }
        tickets.removeFirst();
        System.out.println("Customer purchased a ticket.");
        System.out.println("Tickets remaining : " + tickets.size());
        notifyAll();
    }
}
