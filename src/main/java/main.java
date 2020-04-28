import Domain.Book;
import Domain.Client;
import Domain.Purchase;
import Domain.Validators.BookValidator;
import Domain.Validators.ClientValidator;
import Domain.Validators.PurchaseValidator;
import Domain.Validators.Validator;
import Repository.Repository;
import Repository.JDBC.BookDBRepository;
import Repository.JDBC.ClientDBRepository;
import Repository.JDBC.PurchaseDBRepository;
import Service.BookService;
import Service.ClientService;
import Service.PurchaseService;
import Ui.Console;

public class main {
    public static void main(String args[]) {

        Validator<Book> bookValidator = new BookValidator();
        //Repository<Long, Book> bookRepository = new InMemoryRepository<>(bookValidator);
        //Repository<Long,Book> bookRepository = new BookFileRepository(bookValidator,"./data./Books");
        //Repository<Long,Book> bookRepository = new BookXMLRepository(bookValidator,"./data./Books.xml");
        Repository<Long,Book> bookRepository = new BookDBRepository(bookValidator, "postgres", "admin");
        BookService bookService = new BookService(bookRepository);

        Validator<Client> clientValidator = new ClientValidator();
        //Repository<Long, Client> clientRepository = new InMemoryRepository<>(clientValidator);
        //Repository<Long,Client> clientRepository = new ClientFileRepository(clientValidator,"./data./Clients");
        //Repository<Long,Client> clientRepository = new ClientXMLRepository(clientValidator,"./data./Clients.xml");
        Repository<Long,Client> clientRepository = new ClientDBRepository(clientValidator,"postgres", "admin");
        ClientService clientService = new ClientService(clientRepository);

        Validator<Purchase> purchaseValidator = new PurchaseValidator();
        //Repository<Long, Purchase> purchaseRepository = new PurchaseFileRepository(purchaseValidator,"./data./Purchases");
        //Repository<Long, Purchase> purchaseRepository = new PurchaseXMLRepository(purchaseValidator,"./data./Purchases.xml");
        Repository<Long,Purchase> purchaseRepository = new PurchaseDBRepository(purchaseValidator,"postgres", "admin");
        PurchaseService purchaseService = new PurchaseService(purchaseRepository, bookService,clientService);


        Console console = new Console(bookService, clientService, purchaseService);
        console.runConsole();

        System.out.println("Hello, World");
    }
}
