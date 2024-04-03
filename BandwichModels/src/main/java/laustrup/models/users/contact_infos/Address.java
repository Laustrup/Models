package laustrup.models.users.contact_infos;

import lombok.Getter;
import lombok.Setter;

/** Contains values that determines address attributes. */
@Setter @Getter
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
        String info = "";

        info += get_street() != null ? get_street() + ", " : "";
        info += get_floor() != null ? get_floor() + ", " : "";
        info += get_postal() != null ? get_postal() + " " : "";
        info += get_city() != null ? get_city() : "";

        return info;
    }

    /** Contains values that determines address attributes. */
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
