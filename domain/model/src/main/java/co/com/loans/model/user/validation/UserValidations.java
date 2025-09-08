package co.com.loans.model.user.validation;

import co.com.loans.model.exceptions.DataFieldException;
import co.com.loans.model.user.User;

public class UserValidations {
    public static void validate(User user) {
        if (user.getIdCard() == null || user.getIdCard().isBlank()) {
            throw new DataFieldException("Id card is required.");
        }
    }
}
