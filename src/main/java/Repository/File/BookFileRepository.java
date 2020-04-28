package Repository.File;

import Domain.Book;
import Domain.Validators.BookStoreException;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Repository.InMemory.InMemoryRepository;

import javax.print.event.PrintEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BookFileRepository extends InMemoryRepository<Long, Book> {
    private String fileName;

    public BookFileRepository(Validator<Book> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        loadData();
    }

    private void loadData() {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                //Long id = Long.valueOf(items.get(0));
                String title = items.get(0);
                String author = items.get((1));
                double price = Double.parseDouble(items.get(2));
                String category = items.get(3);
                int year = Integer.parseInt(items.get(4));


                Book Book = new Book(title, author, price,category,year);
                Book.setId(Integer.toUnsignedLong(Book.hashCode()));

                try {
                    super.save(Book);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Book> save(Book entity) throws ValidatorException {
        Optional<Book> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    /**
     * @param entity must not be null.
     * @return an {@code Optional} encapsulating the entity before update
     * @throws ValidatorException
     */
    @Override
    public Optional<Book> update(Book entity) throws ValidatorException {
        Optional<Book> optionalBook = super.update(entity);
        try(PrintWriter pw = new PrintWriter(fileName)){
            pw.write("");
        }catch(FileNotFoundException e){
            throw new BookStoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveToFile);
        return optionalBook;
    }


    /**
     * @param id must not be null.
     * @return an {@code Optional} encapsulating the deleted entity
     * @throws BookStoreException
     */
    @Override
    public Optional<Book> delete(Long id)throws BookStoreException {
        Optional<Book> optionalBook = super.delete(id);
        try (PrintWriter pw = new PrintWriter(fileName)){
            pw.write("");
        } catch(FileNotFoundException e){
            throw new BookStoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveToFile);
        return optionalBook;
    }


    private void saveToFile(Book entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(entity.getTitle() + "," +
                    entity.getAuhtor() + "," + entity.getPrice() + "," + entity.getCategory() + "," + entity.getYear());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
