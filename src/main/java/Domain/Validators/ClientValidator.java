package Domain.Validators;

import Domain.Client;

import java.util.Optional;

public class ClientValidator implements Validator<Client> {
    /**
     * @param entity must not be null
     * @throws ValidatorException in case of invalid field
     */
    @Override
    public void validate(Client entity) throws ValidatorException {
        Optional.ofNullable(entity.getId()).orElseThrow(() -> new ValidatorException("ID can not be null!"));
        Optional.of(entity.getId()).filter((i) -> i < 0).ifPresent((i) -> {
            throw new ValidatorException("Invalid value for ID!");
        });

        Optional.ofNullable(entity.getName()).orElseThrow(() -> new ValidatorException("Client name can not be null!"));
        Optional.of(entity.getName()).filter((e) -> e.matches(".*[,?/.;:!0-9].*")).ifPresent(i -> {
            throw new ValidatorException("Client name must not contain symbols!");
        });

        Optional.ofNullable(entity.getAddress()).orElseThrow(() -> new ValidatorException("Client Address can not be null!"));

    }
}
