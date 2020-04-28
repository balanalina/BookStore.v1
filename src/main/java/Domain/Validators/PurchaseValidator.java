package Domain.Validators;

import Domain.Purchase;

import java.util.Optional;

public class PurchaseValidator implements Validator<Purchase> {
    @Override
    public void validate(Purchase entity) throws ValidatorException {
        Optional.ofNullable(entity.getId()).orElseThrow(() -> new ValidatorException("ID can not be null!"));
        Optional.of(entity.getId()).filter((i) -> i < 0).ifPresent((i) -> {
            throw new ValidatorException("Invalid value for ID!");
        });

        Optional.ofNullable(entity.getBookID()).orElseThrow(() -> new ValidatorException("BookID can not be null!"));
        //Optional.of(entity.getBookID()).filter((i) ->i.matches(".*[,?/.;:!0-9].*")).ifPresent((i) -> {
           // throw new ValidatorException("Invalid value for BookID!");
        //});

       // Optional.ofNullable(entity.getClientID()).orElseThrow(() -> new ValidatorException("ClientID can not be null!"));
        //Optional.of(entity.getClientID()).filter((i) -> i.matches(".*[,?/.;:!0-9].*")).ifPresent((i) -> {
           // throw new ValidatorException("Invalid value for ClientID!");
        //});

        Optional.ofNullable(entity.getQuantity()).orElseThrow(() -> new ValidatorException("Quantity can not be null!"));
        Optional.of(entity.getQuantity()).filter((i) -> i < 0).ifPresent((i) -> {
            throw new ValidatorException("Invalid value for Quantity!");
        });


    }
}
