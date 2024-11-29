public class Vendor implements Runnable{

    private final TicketPool ticketPool;
    private final int ticketReleaseRate;

    public Vendor(TicketPool ticketPool,int ticketReleaseRate){
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        while(true){
            ticketPool.addTickets(ticketReleaseRate);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                System.out.println("Vendor thread interrupted.");
                break;
            }
        }

    }
}
