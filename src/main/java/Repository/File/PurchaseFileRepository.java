package Repository.File;

import Domain.Client;
import Domain.Purchase;
import Domain.Validators.BookStoreException;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Repository.InMemory.InMemoryRepository;

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

public class PurchaseFileRepository extends InMemoryRepository<Long,Purchase> {
    private String fileName;

    public PurchaseFileRepository(Validator<Purchase> validator, String fileName) {
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
                String bookId = items.get(0);
                Long clientId = Long.parseLong(items.get(1));
                int quantity = Integer.parseInt(items.get(2));
                int pn = Integer.parseInt(items.get(3));


                Purchase Purchase = new Purchase(bookId,clientId,quantity,pn);
                Purchase.setId(Integer.toUnsignedLong( Purchase.hashCode()));

                try {
                    super.save(Purchase);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Purchase> save(Purchase entity) throws ValidatorException {
        Optional<Purchase> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    @Override
    public Optional<Purchase> delete(Long id)throws BookStoreException {
        Optional<Purchase> optionalPurchase = super.delete(id);
        try (PrintWriter pw = new PrintWriter(fileName)){
            pw.write("");
        } catch(FileNotFoundException e){
            throw new BookStoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveToFile);
        return optionalPurchase;
    }

    @Override
    public Optional<Purchase> update(Purchase entity) throws ValidatorException {
        Optional<Purchase> optionalPurchase = super.update(entity);
        try(PrintWriter pw = new PrintWriter(fileName)){
            pw.write("");
        }catch(FileNotFoundException e){
            throw new BookStoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveToFile);
        return optionalPurchase;
    }

    private void saveToFile(Purchase entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(entity.getBookID() + "," +
                    entity.getClientID() + "," + entity.getQuantity() + "," + entity.getPurchaseNumber() );
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
