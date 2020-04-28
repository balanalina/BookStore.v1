package Domain;
import Domain.BaseEntity;

public class Purchase extends BaseEntity<Long> {
    private String bookID;
    private Long clientID;
    private int quantity;
    private int purchaseNumber;


    public Purchase(String bid, Long cid, int q, int pn){
        this.bookID = bid;
        this.clientID = cid;
        this.quantity = q;
        this.purchaseNumber = pn;
    }

    public Purchase() {

    }

    public String getBookID(){
        return bookID;
    }
    public Long getClientID(){
        return clientID;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public int getPurchaseNumber() { return purchaseNumber; }
    public void setBookID(String bid){
        this.bookID = bid;
    }
    public void setClientID(Long cid){
        this.clientID = cid;
    }
    public void setQuantity(int q){
        this.quantity = q;
    }
    public void setPurchaseNumber(int purchaseNumber) { this.purchaseNumber = purchaseNumber; }

    @Override
    public String toString(){
        return "Purchase: \nBook Title: "+this.bookID+"\nClient ID: "+this.clientID +
                "\nQuantity: "+Integer.toString(this.quantity) + "\n Purchase Number: " + Integer.toString(purchaseNumber);
     }

    @Override
    public int hashCode() {
        int result = bookID.hashCode();
        result = 30 * result + 2*quantity ;
        return result;
    }
}
