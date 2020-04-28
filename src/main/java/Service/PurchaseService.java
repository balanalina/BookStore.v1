package Service;

import Domain.Client;
import Domain.Purchase;
import Domain.Validators.BookStoreException;
import Repository.Repository;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PurchaseService {
    private Repository<Long, Purchase> repo;
    private BookService bookService;
    private ClientService clientService;

    public PurchaseService(Repository<Long, Purchase> r,BookService b, ClientService c) {
        this.repo = r;
        this.bookService = b;
        this.clientService = c;
    }

    public void addPurchase(Purchase Purchase) {repo.save(Purchase); }

    public void updatePurchase(Purchase purchase){
        repo.update(purchase);
    }

    public void deletePurchase(Long id){
        repo.delete(id);
    }

    public void validatePurchase(Purchase purchase) {
        try{
            String BookId = purchase.getBookID();
            Long ClientId = purchase.getClientID();

            Long resultBId = bookService.findID(BookId);
            Long resultCId = clientService.findIdByCnp(ClientId);

            Optional<Long> Bid = Optional.of(resultBId);
            Optional<Long> Cid = Optional.of(resultCId);

            Long nush = Bid.filter(x -> x != 0).orElseThrow(() -> new BookStoreException("The book title from purchae is not existent."));
            Long nush1 = Bid.filter(x -> x != 0).orElseThrow(() -> new BookStoreException("The client cnp from purchase is not existent."));

        }
        catch (BookStoreException ex){
            throw new BookStoreException(ex);
        }
    }

    public Long findbID(String name){
        Iterable<Purchase> purchases = repo.findAll();
        return StreamSupport.stream(purchases.spliterator(),false).filter(client -> client.getBookID().equals(name)).map(client -> client.getId()).reduce((long) 0,Long::sum);
    }

    public Long findcID(String name){
        Iterable<Purchase> purchases = repo.findAll();
        return StreamSupport.stream(purchases.spliterator(),false).filter(client -> client.getClientID().equals(name)).map(client -> client.getId()).reduce((long) 0,Long::sum);
    }

    public Long findID(int number){
        Iterable<Purchase> purchases = repo.findAll();
        return StreamSupport.stream(purchases.spliterator(),false).filter(p -> p.getPurchaseNumber() == number).map(p -> p.getId()).reduce((long)0,Long::sum);
    }

    public Set<Purchase> getAllPurchases(){
        Iterable<Purchase> Purchases = repo.findAll();
        Set<Purchase> filteredSet = StreamSupport.stream(Purchases.spliterator(), false).collect(Collectors.toSet());
        return filteredSet;
    }

    public Set<Purchase> filterbyBookId(String bookId){
        Iterable<Purchase> Purchases = repo.findAll();
        Set<Purchase> filteredSet = StreamSupport.stream(Purchases.spliterator(),false).filter(Purchase -> Purchase.getBookID().equals(bookId)).collect(Collectors.toSet());
        return filteredSet;
    }
/*
    public Set<Client> filterbyAddress(String clientId){
        Iterable<Purchase> Purchases = repo.findAll();
        Set<String> addressSet = StreamSupport.stream(Purchases.spliterator(),false).flatMap( p -> p.getClientID()).collect(Collectors.toSet());
        Set<Client> clients = clientService.filterbyAddress(clientId);
        Set<Client> filteredSet= StreamSupport.stream(clients.spliterator(),false).filter(client -> addressSet.contains(client.getAddress())).collect(Collectors.toSet());

        return filteredSet;
    }
*/
    public Set<Purchase> filterbyQuantity(int quantity){
        Iterable<Purchase> Purchases = repo.findAll();
        Set<Purchase> filteredSet = StreamSupport.stream(Purchases.spliterator(),false).filter(Purchase -> Purchase.getQuantity() == quantity).collect(Collectors.toSet());
        return filteredSet;
    }
}
