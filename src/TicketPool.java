import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<Ticket> tickets;
    private final int maxTicketCapacity;


    public TicketPool(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.tickets = new LinkedList<>();
    }

    // Synchronized method to add tickets
    public synchronized void addTickets(Ticket ticket) {
        while (tickets.size() >= maxTicketCapacity) {
            try {
                System.out.println("Ticket pool full. Vendor is waiting.");
                wait();
            } catch (InterruptedException e) {
                System.out.println("Add tickets interrupted.");
                return;
            }
        }
        this.tickets.add(ticket);
        System.out.println("Ticket added by " + Thread.currentThread().getName() + " | Total Tickets: " + tickets.size());
        notifyAll();
    }

    // Synchronized method to remove tickets
    public synchronized void removeTicket() {
        while (tickets.isEmpty()) {
            try {
                System.out.println("No tickets available. Customer is waiting...");
                wait();
            } catch (InterruptedException e) {
                System.out.println("Remove tickets interrupted.");
                return;
            }
        }
        Ticket ticket = tickets.poll();
        System.out.println("Ticket bought by " + Thread.currentThread().getName() + " | Remaining Tickets: " + tickets.size());
        notifyAll();
    }
}