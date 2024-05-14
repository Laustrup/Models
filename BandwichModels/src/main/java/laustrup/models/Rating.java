package laustrup.models;

import laustrup.models.users.User;

import laustrup.services.DTOService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.UUID;

import static laustrup.models.users.User.UserDTO;
import static laustrup.services.ObjectService.ifExists;

/**
 * Can be added to a model to indicate the rating that the model is appreciated.
 * Is created by a user.
 */
@Getter
@FieldNameConstants
public class Rating extends Model {

    /**
     * The id of the user, that has received this rating.
     * Is used for when inserting the rating to the user.
     */
    private User _appointed;

    /**
     * The id of the user, that has given this rating.
     * Is used for when inserting the rating to the user.
     */
    private User _judge;

    /**
     * The value of the rating that is appointed.
     * Must be between 0 and 5.
     */
    private int _value;

    /**
     * Is not meant to be necessary, but can be added by the judge.
     */
    @Setter
    private String _comment;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param rating The transport object to be transformed.
     */
    public Rating(DTO rating) throws InputMismatchException {
        super(rating);
        _value = set_value(rating.getValue());
        _appointed = (User) ifExists(rating.getAppointed(), e -> DTOService.convert((UserDTO) e));
        _judge = (User) ifExists(rating.getJudge(), e -> DTOService.convert((UserDTO) e));
    }

    public Rating(int value, UUID appointedId, UUID judgeId, String comment, LocalDateTime timestamp) {
        super(appointedId, judgeId, appointedId+"-"+judgeId, timestamp);
        _comment = comment;
        _value = set_value(value);
    }

    /**
     * Sets the value of this Rating.
     * @param value The specific value, that is wished to be set as the value of the Rating.
     * @return The new value of the current Rating.
     * @throws InputMismatchException Will be thrown if the value is not between 0 and 5.
     */
    public int set_value(int value) throws InputMismatchException {
        if (0 < value && value <= 5 )
            _value = value;
        else
            throw new InputMismatchException();

        return _value;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Fields._appointed,
                Fields._judge,
                Fields._value,
                Model.Fields._timestamp
            },
            new String[] {
                get_appointed() != null ? get_appointed().get_title() : String.valueOf(get_primaryId()),
                get_judge() != null ? get_judge().get_title() : String.valueOf(get_secondaryId()),
                String.valueOf(get_value()),
                String.valueOf(get_timestamp())
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO extends ModelDTO {

        /**
         * The id of the user, that has received this rating.
         * Is used for when inserting the rating to the user.
         */
        private UserDTO appointed;

        /**
         * The id of the user, that has given this rating.
         * Is used for when inserting the rating to the user.
         */
        private UserDTO judge;

        /**
         * The value of the rating that is appointed.
         * Must be between 0 and 5.
         */
        private int value;

        /**
         * Is not meant to be necessary, but can be added by the judge.
         */
        private String comment;

        public DTO(Rating rating) {
            super(rating);
            value = rating.get_value();
            comment = rating.get_comment();
            appointed = DTOService.convert(rating.get_appointed());
            judge = DTOService.convert(rating.get_judge());
        }
    }

}
