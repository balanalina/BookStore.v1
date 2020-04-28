package Service;

import Domain.Book;
import Repository.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BookService  {
    private Repository<Long, Book> repo;

    /**
     * @param r must be an repository, not null
     */
    public BookService(Repository<Long,Book> r){
        this.repo = r;
    }

    /** Saves a book, if valid, into the repository.
     * @param book not null
     */
    public void addBook(Book book){
        repo.save(book);
    }

    public void updateBook(Book book) {
        repo.update(book);
    }

    public void deleteBook(Long id) {
        repo.delete(id);
    }

    public Long findID(String title){
        Iterable<Book> books = repo.findAll();
        return StreamSupport.stream(books.spliterator(),false).filter(book->book.getTitle().equals(title)).map(book->book.getId()).reduce((long) 0,Long::sum);
    }

    /**
     * Returns all books in the repository.
     * @return an {@code Set<Book>}
     */
    public Set<Book> getAllBooks(){
        Iterable<Book> books = repo.findAll();
        return StreamSupport.stream(books.spliterator(),false).collect(Collectors.toSet());
    }


    /**
     * Returns a set containing books that contain the given string in their title.
     * @param t not null
     * @return an {@code Set<Book>}
     */
    public Set<Book> filerbyTitle(String t){
    Iterable<Book> books = repo.findAll();
    Set<Book> filteredSet = StreamSupport.stream(books.spliterator(),false).filter(book->book.getTitle().contains(t)).collect(Collectors.toSet());
    return filteredSet;
    }

    /**
     * Returns a set containing books that are written by the given author.
     * @param name not null
     * @return an {@code Set<Book>}
     */
    public Set<Book> filterbyAuthor(String name){
        Iterable<Book> books = repo.findAll();
        Set<Book> filteredSet = StreamSupport.stream(books.spliterator(),false).filter(book -> book.getAuhtor().equals(name)).collect(Collectors.toSet());
        return filteredSet;
    }

    /**
     * Returns a set containing books that have a price lower than the given one.
     * @param price not null
     * @return an {@code Set<Book>}
     */
    public Set<Book> filterbyLowerPrice(double price){
        Iterable<Book> books = repo.findAll();
        Set<Book> filteredSet = StreamSupport.stream(books.spliterator(),false).filter(book -> book.getPrice() <= price).collect(Collectors.toSet());
        return filteredSet;
    }

    /**
     * Returns a set containing books that have a price higher than the given one.
     * @param price not null
     * @return an {@code Set<Book>}
     */
    public Set<Book> filterbyHigherPrice(double price){
        Iterable<Book> books = repo.findAll();
        Set<Book> filteredSet = StreamSupport.stream(books.spliterator(),false).filter(book -> book.getPrice() >= price).collect(Collectors.toSet());
        return filteredSet;
    }

}
