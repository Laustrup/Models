package laustrup.models.users.contact_infos;

import laustrup.models.Model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Contains information that people need in order to contact the User.
 */
@Getter
public class ContactInfo extends Model {

    /** The email that the User wants to be contacted through outside the application. */
    @Setter
    private String _email;

    /** A Phone object that is used to have information about how to contact the User through Phone. */
    private Phone _phone;

    /** An Address object with info about the location of the User. */
    @Setter
    private Address _address;

    /** A Country object for the information of which Country the User is living in. */
    private Country _country;

    public ContactInfo(DTO contactInfo) {
        super(contactInfo);
        _email = contactInfo.getEmail();
        _phone = new Phone(contactInfo.getPhone());
        _address = new Address(contactInfo.getAddress());
        _country = new Country(contactInfo.getCountry());
    }
    public ContactInfo(UUID id, String email, Phone phone, Address address, Country country, LocalDateTime timestamp) {
        super(id, "Contact-info: "+id, timestamp);
        _email = email;
        _phone = phone;
        _address = address;
        _country = country;
    }

    public ContactInfo(String email, Phone phone, Address address, Country country) {
        super();
        _email = email;
        _phone = phone;
        _address = address;
        _country = country;
        _title = getAddressInfo();
    }

    /**
     * Collects the details of the Address as a one liner String.
     * @return The collected one liner String of the Address.
     */
    public String getAddressInfo() {
        return _address.toString();
    }

    @Override
    public String toString() {
        return "ContactInfo(" +
                    "id:" + _primaryId +
                    ",email:" + _email +
                    ",address:" + getAddressInfo() +
                    ",phone:" + _phone.toString() +
                    ",country:" + _country.toString() +
                ")";
    }

    /** Contains information that people need in order to contact the User. */
    @Getter @Setter
    public static class DTO extends ModelDTO {

        /** The email that the User wants to be contacted through outside the application. */
        private String email;

        /** A Phone object that is used to have information about how to contact the User through Phone. */
        private Phone.DTO phone;

        /** An Address object with info about the location of the User. */
        private Address.DTO address;

        /** A Country object for the information of which Country the User is living in. */
        private Country.DTO country;

        /**
         * Converts into this DTO Object.
         * @param contactInfo The Object to be converted.
         */
        public DTO(ContactInfo contactInfo) {
            super(contactInfo);
            email = contactInfo.get_email();
            phone = new Phone.DTO(contactInfo.get_phone());
            address = new Address.DTO(contactInfo.get_address());
            country = new Country.DTO(contactInfo.get_country());
        }
    }
}
