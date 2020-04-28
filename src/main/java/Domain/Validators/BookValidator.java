package Domain.Validators;

import Domain.Book;

import java.util.Optional;

public class BookValidator implements Validator<Book>{
    /**
     * @param entity must not be null
     * @throws ValidatorException in case of invalid field
     */
    @Override
    public void validate(Book entity) throws ValidatorException {
        Optional.ofNullable(entity.getTitle()).orElseThrow(() -> new ValidatorException("Book title can not be null!"));
        Optional.of(entity.getTitle()).filter((e) -> e.matches(".*[,?/.;:!].*")).ifPresent(i -> {
            throw new ValidatorException("Book title must not contain symbols!");
        });

        Optional.ofNullable(entity.getAuhtor()).orElseThrow(() -> new ValidatorException("Book author can not be null!"));
        Optional.of(entity.getAuhtor()).filter((e) -> e.matches(".*[,?/.;:!0-9].*")).ifPresent(i -> {
            throw new ValidatorException("Book Author must not contain symbols!");
        });

        Optional.ofNullable(entity.getPrice()).orElseThrow(() -> new ValidatorException("Price can not be null!"));
        Optional.of(entity.getPrice()).filter((i) -> i < 0).ifPresent((i) -> {
            throw new ValidatorException("Invalid value for Price!");
        });


    }
}
