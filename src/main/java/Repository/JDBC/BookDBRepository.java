package Repository.JDBC;

import Domain.Book;
import Domain.Validators.BookStoreException;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BookDBRepository implements Repository<Long, Book> {
    private static final String URL="jdbc:postgresql://localhost:5432/BookStore";
    private String userName;
    private String password;
    private Validator<Book> validator;


    public BookDBRepository(Validator<Book> validator,String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.validator = validator;
    }

    private Book createbook(ResultSet resultSet) throws SQLException {

        Book result = new Book();
        Long bookID = resultSet.getLong("id");
        String title = resultSet.getString("title");
        double price = resultSet.getDouble("price");
        String author = resultSet.getString("author");
        String category = resultSet.getString("category");
        int year = resultSet.getInt("year");
        result.setId(bookID);
        result.setTitle(title);
        result.setPrice(price);
        result.setAuhtor(author);
        result.setCategory(category);
        result.setYear(year);
        return result;
    }

    @Override
    public Optional<Book> findOne(Long id) throws BookStoreException {
        String sqlCommandString = "select * from book where id=?";
        Book result = new Book();

        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            PreparedStatement statement = connection.prepareStatement(sqlCommandString);
            statement.setLong(1,id);
            ResultSet resultSet = statement.executeQuery();
            //set up book, according to querry result
            if(resultSet.next()){
                result = createbook(resultSet);
                return Optional.of(result);
            }

        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Book> findAll() throws BookStoreException{

        String sqlCommand = "select * from book";
        Set<Book> set = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(URL,userName,password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet  resultSet = statement.executeQuery();
            while (resultSet.next()){
                set.add(createbook(resultSet));
            }
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return set;
    }

    @Override
    public Optional<Book> save(Book entity) throws ValidatorException,BookStoreException {
        validator.validate(entity);
        Optional<Book> exists = this.findOne(entity.getId());
        if(exists.isPresent())
            return Optional.of(entity);
        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            String sqlCommand = "insert into book(id,title,author,category,price,year) values (?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,entity.getId());
            statement.setString(2,entity.getTitle());
            statement.setString(3,entity.getAuhtor());
            statement.setString(4,entity.getCategory());
            statement.setDouble(5,entity.getPrice());
            statement.setInt(6,entity.getYear());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> delete(Long id) throws BookStoreException {
        Optional<Book> result = this.findOne(id);
        try(Connection connection = DriverManager.getConnection(URL,userName,password)) {
            String sqlCommand = "delete from book where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Book> update(Book entity) throws ValidatorException,BookStoreException {
        validator.validate(entity);
        Optional<Book> result = this.findOne(entity.getId());
        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            String sqlCommand = "update book set title=?, author=?, category=?, price=?, year=? where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1,entity.getTitle());
            statement.setString(2,entity.getAuhtor());
            statement.setString(3,entity.getCategory());
            statement.setDouble(4,entity.getPrice());
            statement.setInt(5,entity.getYear());
            statement.setLong(6,entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookStoreException(e.getMessage());
        }
        return result;
    }
}
