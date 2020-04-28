package Ui;

import Domain.Book;
import Domain.Client;
import Domain.Purchase;
import Domain.Validators.BookStoreException;
import Domain.Validators.ValidatorException;
import Service.BookService;
import Service.ClientService;
import Service.PurchaseService;
import sun.tools.jconsole.JConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Set;
import java.util.function.DoubleToIntFunction;

enum Commands{
    c1{
        @Override
        public void execute(Console console){
            console.addBook();
        }
    },
    c2{
        @Override
        public void execute(Console console){
            console.deleteBook();
        }
    },
    c3{
        @Override
        public void execute(Console console){
            console.updateBook();
        }
    },
    c4{
        @Override
        public void execute(Console console){
            console.getAllBooks();
        }
    },
    c5{
        @Override
        public void execute(Console console){
            console.addClient();
        }
    },
    c6{
        @Override
        public void execute(Console console){
            console.deleteClient();
        }
    },
    c7{
        @Override
        public void execute(Console console){
            console.updateClient();
        }
    },
    c8{
        @Override
        public void execute(Console console){
            console.getAllClients();
        }
    },
    c13{
        @Override
        public void execute(Console console){
            console.filterByTitle();
        }
    },
    c14{
        @Override
        public void execute(Console console){
            console.filterByAuthor();
        }
    },
    c15{
        @Override
        public void execute(Console console){
            console.filterByHigherPrice();
        }
    },
    c16{
        @Override
        public void execute(Console console){
            console.filterByLowerPrice();
        }
    },
    c9{
        public void execute(Console console) { console.addPurchase();}
    },
    c10{
      public void execute(Console console){ console.deletePurchase(); }
    },
    c11{
      public void execute(Console console){ console.updatePurchase();}
    },
    c12{
      public void execute(Console console){ console.getAllPurchases();}
    },
    c17{
        public void execute(Console console){ console.filterByName(); }
    },
    c18{
      public void execute(Console console){ console.filetrByAddress();}
    },
    c19{
      public void execute(Console console){ console.filterByBookID();}
    },/*
    c20{
      public void execute(Console console){ console.filterByClientAddress();}
    },*/
    c20{
      public void execute(Console console){ console.filterByQuantity(); }
    },
    x{
        @Override
        public void execute(Console console){

            System.exit(0);
        }
    };
    public abstract void execute(Console console);
}

public class Console {
    private BookService bookService;
    private ClientService clientService;
    private PurchaseService purchaseService;

    public Console(BookService bookService, ClientService clientService, PurchaseService purchaseService)
    {
        this.bookService = bookService;
        this.clientService = clientService;
        this.purchaseService = purchaseService;
    }

    public void runConsole() {
        command();
        runConsole();
    }

    public void showCommands(){
        System.out.println("-----------------------------------------");
        System.out.println("x. Exit.");
        System.out.println("c1. Add a book.");
        System.out.println("c2. Delete a book.");
        System.out.println("c3. Update a book.");
        System.out.println("c4. Show all books.");
        System.out.println("c5. Add a client.");
        System.out.println("c6. Delete a client.");
        System.out.println("c7. Update a client.");
        System.out.println("c8. Show all clients");
        System.out.println("c9. Add a purchase.");
        System.out.println("c10. Delete a purchase.");
        System.out.println("c11. Update a purchase.");
        System.out.println("c12. Show all purchases.");
        System.out.println("c13. Filter all books by title");
        System.out.println("c14. Filter all books by author");
        System.out.println("c15. Filter all books that have the price higher");
        System.out.println("c16. Filter all books that have the price smaller");
        System.out.println("c17. Filter all clients by name");
        System.out.println("C18. Filter all clients by address");
        System.out.println("C19. Filter all purchases by book title");
        //System.out.println("C20. Filter all purchases by client address");
        System.out.println("C20. Filter all purchases by quantity");

        System.out.println("-----------------------------------------");
    }


     private void command(){
        showCommands();
        System.out.println("Enter your command: ");
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        try{
            String command = reader.readLine();
            Commands.valueOf(command).execute(this);
        }
        catch (IOException exc){
            System.out.println("Command Reading failed!");
        }
        catch (BookStoreException exc){
            System.out.println(exc.getMessage());
        }
        catch (IllegalArgumentException illExc){
            System.out.println("Wrong Command!");
        }

    }

    public Book readBook() throws BookStoreException{
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter the book's title: ");
            String title = reader.readLine();
            System.out.println("Enter the book's author: ");
            String author = reader.readLine();
            System.out.println("Enter the book's price: ");
            Double price = Double.parseDouble(reader.readLine());
            System.out.println("Enter the book's category: ");
            String category = reader.readLine();
            System.out.println("Enter the book's aparition year: ");
            int year = Integer.parseInt(reader.readLine());


            Book book = new Book(title, author, price,category,year);
            book.setId(Integer.toUnsignedLong(book.hashCode()));
            return book;
        }
        catch(IOException e){
            throw new BookStoreException("Book read error!");
        }
        catch (NumberFormatException e){
            throw new BookStoreException("Invalid input!");
        }
    }

    public Client readClient()  throws BookStoreException{
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter the client's name: ");
            String name = reader.readLine();
            System.out.println("Enter the client's address: ");
            String address = reader.readLine();
            System.out.println("Enter the client's cnp: ");
            Long cnp = Long.parseLong(reader.readLine());

            Client client = new Client(name,address,cnp);
            client.setId(Integer.toUnsignedLong(client.hashCode()));
            return client;
        }
        catch(IOException e){
            throw new BookStoreException("Client read error!");
        }
        catch (NumberFormatException e){
            throw new BookStoreException("Invalid input!");
        }
    }

    public Purchase readPurchase(){
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter the book's title: ");
            String title = reader.readLine();
            System.out.println("Enter the client's cnp: ");
            Long cnp = Long.parseLong(reader.readLine());
            System.out.println("Enter the quantity: ");
            int q = Integer.parseInt(reader.readLine());
            System.out.println("Enter the purchase number: ");
            int pn = Integer.parseInt(reader.readLine());

            Purchase purchase = new Purchase(title,cnp,q,pn);
            purchase.setId(Integer.toUnsignedLong(purchase.hashCode()));
            purchaseService.validatePurchase(purchase);
            return purchase;
        }
        catch(IOException e){
            throw new BookStoreException("Purchase read error!");
        }
        catch (NumberFormatException e){
            throw new BookStoreException("Invalid input!");
        }
    }


    public void addBook() throws BookStoreException,ValidatorException{
        Book book = readBook();
        bookService.addBook(book);
    }

    public void addClient() throws BookStoreException,ValidatorException{
        Client client = readClient();
        clientService.addClient(client);
    }

    public void addPurchase() throws BookStoreException,ValidatorException {
        Purchase purchase = readPurchase();
        purchaseService.validatePurchase(purchase);
        purchaseService.addPurchase(purchase);

    }

    public void getAllBooks(){
        System.out.println("All books: \n");
        Set<Book> books = bookService.getAllBooks();
        books.forEach(book -> System.out.println(book));
    }

    public void getAllClients(){
        System.out.println("All clients: \n");
        Set<Client> clients = clientService.getAllClients();
        clients.forEach(client -> System.out.println(client));
    }

    public void getAllPurchases(){
        System.out.println("All purchases: \n");
        Set<Purchase> purchases = purchaseService.getAllPurchases();
        purchases.forEach(System.out::println);
    }

    public void updateBook(){
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        Long id;
        String input;
        try{
            System.out.println("Enter the title of the book you want to update: ");
            input = buff.readLine();
            id = bookService.findID(input);
            Book book = readBook();
            book.setId(id);
            bookService.updateBook(book);
        }
        catch (IOException ex){
            throw new BookStoreException("Book Read Failed!");
        }
        catch (NumberFormatException exc){
            throw new BookStoreException("Invalid value for ID!");
        }
    }

    public void updateClient(){
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        Long id;
        String input;
        try{
            System.out.println("Enter the name of the client you want to update: ");
            input = buff.readLine();
            id = clientService.findId(input);
            Client client = readClient();
            client.setId(id);
            clientService.updateClient(client);
        }
        catch (IOException ex){
            throw new BookStoreException("Client Read Failed!");
        }
        catch (NumberFormatException exc){
            throw new BookStoreException("Invalid value for ID!");
        }
    }

    public void updatePurchase(){
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        Long id;
        String input;
        try{
            System.out.println("Enter the number of the purchase you want to update: ");
            input = buff.readLine();
            id = purchaseService.findID(Integer.parseInt(input));
            Purchase purchase = readPurchase();
            purchase.setId(id);
            purchaseService.updatePurchase(purchase);
        }
        catch (IOException ex){
            throw new BookStoreException("Purchase Read Failed!");
        }
        catch (NumberFormatException exc){
            throw new BookStoreException("Invalid value for ID!");
        }

    }

    public void deleteBook(){
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        Long id;
        String input;
        try{
            System.out.println("Enter the title of the book you want to delete: ");
            input = buff.readLine();
            id = bookService.findID(input);
            if(id != 0)
                bookService.deleteBook(id);
        }
        catch (IOException ex){
            throw new BookStoreException("Book Read Failed!");
        }
        catch (NumberFormatException exc){
            throw new BookStoreException("Invalid value for ID!");
        }


    }


    public void deleteClient(){
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        Long id;
        String input;
        try{
            System.out.println("Enter the name of the client you want to delete: ");
            input = buff.readLine();
            id = clientService.findId(input);
            if(id != 0)
                clientService.deleteClient(id);
        }
        catch (IOException ex){
            throw new BookStoreException("Client Read Failed!");
        }
        catch (NumberFormatException exc){
            throw new BookStoreException("Invalid value for ID!");
        }

    }

    public void deletePurchase(){
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        Long id;
        String input;
        try{
            System.out.println("Enter the number of the purchase you want to delete: ");
            input = buff.readLine();
            id = purchaseService.findID(Integer.parseInt(input));
            if(id != 0)
                purchaseService.deletePurchase(id);
        }
        catch (IOException ex){
            throw new BookStoreException("Purchase Read Failed!");
        }
        catch (NumberFormatException exc){
            throw new BookStoreException("Invalid value for ID!");
        }

    }

    public void filterByAuthor() throws BookStoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter search key:");
            String author = buffer.readLine();
            bookService.filterbyAuthor(author).forEach(System.out::println);
        }
        catch(IOException e){
            throw new BookStoreException("Failed to read serach key!");
        }
    }

    public void filterByTitle() throws BookStoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter search key:");
            String title = buffer.readLine();
            bookService.filerbyTitle(title).forEach(System.out::println);
        }
        catch(IOException e){
            throw new BookStoreException("Failed to read serach key!");
        }
    }

    public void filterByLowerPrice() throws BookStoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter search key:");
            Double price = Double.parseDouble(buffer.readLine());
            bookService.filterbyLowerPrice(price).forEach(System.out::println);
        }
        catch(IOException e){
            throw new BookStoreException("Failed to read serach key!");
        }
    }

    public void filterByHigherPrice() throws BookStoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter search key:");
            Double price = Double.parseDouble(buffer.readLine());
            bookService.filterbyHigherPrice(price).forEach(System.out::println);
        }
        catch(IOException e){
            throw new BookStoreException("Failed to read serach key!");
        }
    }

    public void filterByName() throws BookStoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter search key: ");
            String name = buffer.readLine();
            clientService.filterbyName(name).forEach(System.out::println);
        }catch(IOException e){
            throw new BookStoreException("Failed to read search key!");
        }
    }

    public void filetrByAddress() throws BookStoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter the search key: ");
            String address =buffer.readLine();
            clientService.filterbyAddress(address).forEach(System.out::println);
        }catch(IOException e){
            throw new BookStoreException("Failed to read the search key!");
        }
    }

    public void filterByBookID() throws BookStoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter the search key: ");
            String bookID = buffer.readLine();
            purchaseService.filterbyBookId(bookID).forEach(System.out::println);
        }catch(IOException e){
            throw new BookStoreException("Failed to read the search key!");
        }
    }
/*
    public void filterByClientAddress() throws BookStoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter the search key: ");
            String address = buffer.readLine();
            purchaseService.filterbyAddress(address).forEach(System.out::println);
        }catch(IOException e){
            throw new BookStoreException("Failed to read the search key!");
        }

    }*/

    public void filterByQuantity() throws BookStoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter the search key: ");
            int q = Integer.parseInt(buffer.readLine());
            purchaseService.filterbyQuantity(q).forEach(System.out::println);
        }catch(IOException e){
            throw new BookStoreException("Failed to rad the search key!");
        }
    }

}
