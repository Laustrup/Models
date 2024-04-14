package laustrup.models.users.subscriptions;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.InputMismatchException;
import java.util.UUID;

/**
 * Contains different information about credit cards, that are needed for curtain subscriptions.
 */
@Getter
@ToString
@FieldNameConstants
public class Card {

    /**
     * This id is the id from the database.
     * Subscription only contains the id of this card for security reasons.
     */
    private UUID _id;

    /** An enum of different credit card providers or brands. */
    @Setter
    private Type _type;

    /** The owner of the credit card. */
    @Setter
    private String _owner;

    /**
     * This pin code is the password information to the card.
     * Must be a curtain amount of numbers.
     */
    private long _cardNumbers;

    /**
     * The month that the card will expire.
     * Only two digits allowed.
     */
    private int _expirationMonth;

    /**
     * The year that the card will expire.
     * Only two digits allowed.
     */
    @Setter
    private int _expirationYear;

    /** The three digits on the backside of the credit card. */
    private int _cVV;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param card The transport object to be transformed.
     */
    public Card(DTO card) throws InputMismatchException {
        _id = card.getId();
        _type = Type.valueOf(card.getType().toString());
        _owner = card.getOwner();
        set_cardNumbers(card.getCardNumbers());
        set_expirationMonth(card.getExpirationMonth());
        _expirationYear = card.getExpirationYear();
        set_cVV(card.getCVV());
    }

    /**
     * A constructor with all the values of this Object.
     * @param id The primary id that identifies this unique Object.
     * @param type An enum of different credit card providers or brands.
     * @param owner The owner of the credit card.
     * @param numbers The numbers for the card.
     * @param expirationMonth The month that the card expires.
     * @param expirationYear The year the card expires.
     * @param cVV The safety numbers for the card.
     * @throws InputMismatchException Will be thrown, if the month length isn't between 0 -> 12.
     */
    public Card(
            UUID id,
            Type type,
            String owner,
            long numbers,
            int expirationMonth,
            int expirationYear,
            int cVV
    ) throws InputMismatchException {
        _id = id;
        _type = type;
        _owner = owner;
        set_cardNumbers(numbers);
        set_expirationMonth(expirationMonth);
        _expirationYear = expirationYear;
        set_cVV(cVV);
    }

    /**
     * Will set the pin code of the current card.
     * @param pin The pin code the is wished to be set.
     * @return The updated pin code of current card.
     * @throws InputMismatchException Will be thrown, if the digit length of pin isn't 4 * 5 * 10.
     */
    public long set_cardNumbers(long pin) throws InputMismatchException {
        if (pin <= 4 * 4 * 10 && pin >= 4 * 4 * 10) _cardNumbers = pin;
        else throw new InputMismatchException();
        return _cardNumbers;
    }

    /**
     * Will set the expiration month of the current card.
     * @param month The expiration month the is wished to be set.
     * @return The updated expiration month of current card.
     * @throws InputMismatchException Will be thrown, if the month length isn't between 0 -> 12.
     */
    public int set_expirationMonth(int month) throws InputMismatchException {
        if (month > 0 && month <= 12) _expirationMonth = month;
        else throw new InputMismatchException();
        return month;
    }

    /**
     * Will set the control digits of the current card.
     * @param digits The control digits the is wished to be set.
     * @return The updated control digits of current card.
     * @throws InputMismatchException Will be thrown, if the digit length of isn't 3.
     */
    public int set_cVV(int digits) throws InputMismatchException {
        if (digits <= 999 && digits > 0) _cVV = digits;
        else throw new InputMismatchException();
        return _cVV;
    }

    /** Are a set of enums with values of different credit card providers. */
    public enum Type {
        VISA,
        AMERICAN_EXPRESS,
        DANCARD,
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @NoArgsConstructor
    @Data
    public static class DTO {

        /**
         * This id is the id from the database.
         * Subscription only contains the id of this card for security reasons.
         */
        private UUID id;

        /** An enum of different credit card providers or brands. */
        private Type type;

        /** The owner of the credit card. */
        private String owner;

        /**
         * This pin code is the password information to the card.
         * Must be a curtain amount of numbers.
         */
        private long cardNumbers;

        /**
         * The month that the card will expire.
         * Only two digits allowed.
         */
        private int expirationMonth;

        /**
         * The year that the card will expire.
         * Only two digits allowed.
         */
        private int expirationYear;

        /**
         * The three digits on the backside of the credit card.
         */
        private int cVV;

        public DTO(Card card) {
            id = card.get_id();
            type = Type.valueOf(card.get_type().toString());
            owner = card.get_owner();
            cardNumbers = card.get_cardNumbers();
            expirationMonth = card.get_expirationMonth();
            expirationYear = card.get_expirationYear();
            cVV = card.get_cVV();
        }

        /**
         * Are a set of enums with values of different credit card providers.
         */
        public enum Type {
            VISA,
            AMERICAN_EXPRESS,
            DANCARD,
        }
    }

}
