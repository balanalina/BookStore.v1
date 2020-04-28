package Repository.File;

import Domain.Book;
import Domain.Client;
import Domain.Client;
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

public class ClientFileRepository extends InMemoryRepository<Long, Client> {
    private String fileName;

    public ClientFileRepository(Validator<Client> validator, String fileName) {
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
                String name = items.get(0);
                String address = items.get(1);
                Long cnp =  Long.getLong(items.get(2));



                Client Client = new Client(name,address,cnp);
                Client.setId(Integer.toUnsignedLong(Client.hashCode()));

                try {
                    super.save(Client);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        Optional<Client> optional = super.save(entity);
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
    public Optional<Client> update(Client entity) throws ValidatorException {
        Optional<Client> optionalClient = super.update(entity);
        try(PrintWriter pw = new PrintWriter(fileName)){
            pw.write("");
        }catch(FileNotFoundException e){
            throw new BookStoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveToFile);
        return optionalClient;
    }

    /**
     * @param id must not be null.
     * @return an {@code Optional} encapsulating the deleted entity, if existent
     * @throws BookStoreException
     */
    @Override
    public Optional<Client> delete(Long id)throws BookStoreException {
        Optional<Client> optionalClient = super.delete(id);
        try (PrintWriter pw = new PrintWriter(fileName)){
            pw.write("");
        } catch(FileNotFoundException e){
            throw new BookStoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveToFile);
        return optionalClient;
    }

    private void saveToFile(Client entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write( entity.getName() + "," +
                    entity.getAddress() + "," + entity.getCnp() );
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
