package laustrup.models.users.contact_infos;

import lombok.*;
import lombok.experimental.FieldNameConstants;

/** An object with information about a curtain Country. */
@Getter @ToString @FieldNameConstants
public class Country {

    /** The name of the Country. */
    private String _title;

    /** The two digits indexes of the Country. */
    private CountryIndexes _indexes;

    /** The value of the first few digits of a phone number. */
    private int _firstPhoneNumberDigits;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param country The transport object to be transformed.
     */
    public Country(DTO country) {
        _title = country.getTitle();
        _indexes = CountryIndexes.valueOf(country.getIndexes().toString());
        _firstPhoneNumberDigits = country.getFirstPhoneNumberDigits();
    }

    /**
     The primary id that identifies this unique Object.
     * @param title The name of the Country.
     * @param indexes The two digits indexes of the Country.
     * @param firstPhoneNumberDigits The value of the first few digits of a phone number.
     */
    public Country(String title, CountryIndexes indexes, int firstPhoneNumberDigits) {
        _title = title;
        _indexes = indexes;
        _firstPhoneNumberDigits = firstPhoneNumberDigits;
    }

    /** An enum with indexes of the Country. */
    public enum CountryIndexes { DK, SE, DE }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO {

        /** The name of the Country. */
        private String title;

        /** The two digits indexes of the Country. */
        private CountryIndexes indexes;

        /** The value of the first few digits of a phone number. */
        private int firstPhoneNumberDigits;

        /**
         * Converts into this DTO Object.
         * @param country The Object to be converted.
         */
        public DTO(Country country) {
            title = country.get_title();
            indexes = CountryIndexes.valueOf(country.get_indexes().toString());
            firstPhoneNumberDigits = country.get_firstPhoneNumberDigits();
        }

        /** An enum with indexes of the Country. */
        public enum CountryIndexes {
            DK,
            SE,
            DE
        }
    }
}
