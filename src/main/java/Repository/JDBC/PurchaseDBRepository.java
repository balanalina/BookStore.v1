package Repository.JDBC;

import Domain.Purchase;
import Domain.Validators.BookStoreException;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PurchaseDBRepository implements Repository<Long, Purchase> {
    private static final String URL="jdbc:postgresql://localhost:5432/BookStore";
    private String userName;
    private String password;
    private Validator<Purchase> validator;


    public PurchaseDBRepository(Validator<Purchase> validator,String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.validator = validator;
    }

    private Purchase createPurchase(ResultSet resultSet) throws SQLException {

        Purchase result = new Purchase();
        Long PurchaseID = resultSet.getLong("id");
        String BookID = resultSet.getString("bookid");
        Long ClientID = resultSet.getLong("clientid");
        int quantity = resultSet.getInt("quantity");
        int purchaseNumber = resultSet.getInt("purchasenumber");
        result.setId(PurchaseID);
        result.setBookID(BookID);
        result.setClientID(ClientID);
        result.setQuantity(quantity);
        result.setPurchaseNumber(purchaseNumber);
        return result;
    }

    @Override
    public Optional<Purchase> findOne(Long id) throws BookStoreException {
        String sqlCommandString = "select * from purchase where id=?";
        Purchase result = new Purchase();

        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            PreparedStatement statement = connection.prepareStatement(sqlCommandString);
            statement.setLong(1,id);
            ResultSet resultSet = statement.executeQuery();
            //set up Purchase, according to querry result
            if(resultSet.next()){
                result = createPurchase(resultSet);
                return Optional.of(result);
            }

        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Purchase> findAll() throws BookStoreException{

        String sqlCommand = "select * from purchase";
        Set<Purchase> set = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(URL,userName,password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet  resultSet = statement.executeQuery();
            while (resultSet.next()){
                set.add(createPurchase(resultSet));
            }
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return set;
    }

    @Override
    public Optional<Purchase> save(Purchase entity) throws ValidatorException,BookStoreException {
        validator.validate(entity);
        Optional<Purchase> exists = this.findOne(entity.getId());
        if(exists.isPresent())
            return Optional.of(entity);
        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            String sqlCommand = "insert into purchase(id,bookid,clientid,quantity,purchasenumber) values (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,entity.getId());
            statement.setString(2,entity.getBookID());
            statement.setLong(3,entity.getClientID());
            statement.setInt(4,entity.getQuantity());
            statement.setInt(5,entity.getPurchaseNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Purchase> delete(Long id) throws BookStoreException {
        Optional<Purchase> result = this.findOne(id);
        try(Connection connection = DriverManager.getConnection(URL,userName,password)) {
            String sqlCommand = "delete from purchase where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Purchase> update(Purchase entity) throws ValidatorException,BookStoreException {
        validator.validate(entity);
        Optional<Purchase> result = this.findOne(entity.getId());
        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            String sqlCommand = "update purchase set bookid=?, clientid=?, quantity=?, purchasenumber=? where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1,entity.getBookID());
            statement.setLong(2,entity.getClientID());
            statement.setInt(3,entity.getQuantity());
            statement.setInt(4,entity.getPurchaseNumber());
            statement.setLong(5,entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return result;
    }
}
