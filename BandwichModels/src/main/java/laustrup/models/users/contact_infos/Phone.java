package laustrup.models.users.contact_infos;

import lombok.*;

/** Details about phone contacting information. */
@Getter @ToString
public class Phone {

    /** A country object, that represents the nationality of this PhoneNumber. */
    @Setter
    private Country _country;

    /** The contact numbers for the Phone. */
    @Setter
    private long _numbers;

    /** True if the number is for a mobile. */
    private boolean _mobile;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param phone The transport object to be transformed.
     */
    public Phone(DTO phone) {
        _country = new Country(phone.getCountry());
        _numbers = phone.getNumbers();
        _mobile = phone.isMobile();
    }
    public Phone(Country country, long numbers, boolean mobile) {
        _country = country;
        _numbers = numbers;
        _mobile = mobile;
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO {

        /** A country object, that represents the nationality of this PhoneNumber. */
        private Country.DTO country;

        /** The contact numbers for the Phone. */
        private long numbers;

        /** True if the number is for a mobile. */
        private boolean mobile;

        /**
         * Converts into this DTO Object.
         * @param phone The Object to be converted.
         */
        public DTO(Phone phone) {
            country = new Country.DTO(phone.get_country());
            numbers = phone.get_numbers();
            mobile = phone.is_mobile();
        }
    }
}
