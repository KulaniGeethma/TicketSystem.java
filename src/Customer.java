public class Customer implements Runnable{

    private  final TicketPool ticketPool;

    public Customer(TicketPool ticketPool){

        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while(true){
            ticketPool.removeTickets();
            try {
                Thread.sleep(1000);
            }catch(InterruptedException e){
                System.out.println("Customer thread interrupted.");
                break;
            }
        }
    }
}
