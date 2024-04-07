package laustrup.models.users.contact_infos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.Arrays;
import java.util.Objects;

/** Contains values that determines address attributes. */
@Setter @Getter @FieldNameConstants
public class Address {

    /** The street and street number. */
    private String _street;

    /** The floor, if in an apartment, also include left or right. */
    private String _floor;

    /** Some digits describing the city. */
    private String _postal;

    /** The city of the postal. */
    private String _city;

    public Address(DTO address) {
        _street = address.getStreet();
        _floor = address.getFloor();
        _postal = address.getPostal();
        _city = address.getCity();
    }

    public Address(String street, String floor, String postal, String city) {
        _street = street;
        _floor = floor;
        _postal = postal;
        _city = city;
    }

    @Override
    public String toString() {
        return String.join(", ", Arrays.stream(new String[] {
            get_street(),
            get_floor(),
            get_postal(),
            get_city()
        }).filter(Objects::nonNull).toList());
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO {

        /** The street and street number. */
        private String street;

        /** The floor, if in an apartment, also include left or right. */
        private String floor;

        /** Some digits describing the city. */
        private String postal;

        /** The city of the postal. */
        private String city;

        /**
         * Converts into this DTO Object.
         * @param address The Object to be converted.
         */
        public DTO(Address address) {
            street = address.get_street();
            floor = address.get_floor();
            postal = address.get_postal();
            city = address.get_city();
        }
    }
}
