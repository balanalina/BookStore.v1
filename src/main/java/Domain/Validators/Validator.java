package Domain.Validators;


public interface Validator<T> {
    /**
     * @param entity
     *               must not be null
     * @throws ValidatorException
     */
    void validate(T entity) throws ValidatorException;
}
