package Repository.JDBC;

import Domain.Client;
import Domain.Validators.BookStoreException;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ClientDBRepository implements Repository<Long, Client> {
    private static final String URL="jdbc:postgresql://localhost:5432/BookStore";
    private String userName;
    private String password;
    private Validator<Client> validator;


    public ClientDBRepository(Validator<Client> validator,String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.validator = validator;
    }

    private Client createClient(ResultSet resultSet) throws SQLException {

        Client result = new Client();
        Long ClientID = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String address = resultSet.getString("address");
        Long cnp = resultSet.getLong("cnp");
        result.setId(ClientID);
        result.setName(name);
        result.setAddress(address);
        result.setCnp(cnp);
        return result;
    }

    @Override
    public Optional<Client> findOne(Long id) throws BookStoreException {
        String sqlCommandString = "select * from client where id=?";
        Client result = new Client();

        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            PreparedStatement statement = connection.prepareStatement(sqlCommandString);
            statement.setLong(1,id);
            ResultSet resultSet = statement.executeQuery();
            //set up Client, according to querry result
            if(resultSet.next()){
                result = createClient(resultSet);
                return Optional.of(result);
            }

        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Client> findAll() throws BookStoreException{

        String sqlCommand = "select * from client";
        Set<Client> set = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(URL,userName,password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet  resultSet = statement.executeQuery();
            while (resultSet.next()){
                set.add(createClient(resultSet));
            }
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return set;
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException,BookStoreException {
        validator.validate(entity);
        Optional<Client> exists = this.findOne(entity.getId());
        if(exists.isPresent())
            return Optional.of(entity);
        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            String sqlCommand = "insert into client(id,name,cnp,address) values (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,entity.getId());
            statement.setString(2,entity.getName());
            statement.setLong(3,entity.getCnp());
            statement.setString(4,entity.getAddress());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> delete(Long id) throws BookStoreException {
        Optional<Client> result = this.findOne(id);
        try(Connection connection = DriverManager.getConnection(URL,userName,password)) {
            String sqlCommand = "delete from client where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException,BookStoreException {
        validator.validate(entity);
        Optional<Client> result = this.findOne(entity.getId());
        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            String sqlCommand = "update Client set name=?, cnp=?, address=? where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1,entity.getName());
            statement.setLong(2,entity.getCnp());
            statement.setString(3,entity.getAddress());
            statement.setLong(6,entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return result;
    }
}
