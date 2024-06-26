package laustrup.models;

import laustrup.models.users.User;

import laustrup.services.DTOService;
import lombok.Getter;
import lombok.Setter;

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

    public Rating(DTO rating) throws InputMismatchException {
        super(rating);
        _value = set_value(rating.getValue());
        _appointed = (User) ifExists(rating.getAppointed(), e -> DTOService.convert((UserDTO) e));
        _judge = (User) ifExists(rating.getJudge(), e -> DTOService.convert((UserDTO) e));
    }
    public Rating(int value) {
        _value = value;
    }
    public Rating(int value, User appointed, User judge, LocalDateTime timestamp) throws InputMismatchException {
        super(appointed.get_primaryId(), judge.get_primaryId(), appointed.get_username()+"-"+judge.get_username(), timestamp);
        _value = set_value(value);
        _appointed = appointed;
        _judge = judge;
    }

    public Rating(int value, UUID appointedId, UUID judgeId, LocalDateTime timestamp) throws InputMismatchException {
        super(appointedId, judgeId, appointedId+"-"+judgeId, timestamp);
        _value = set_value(value);
    }

    public Rating(int value, UUID appointedId, UUID judgeId, String comment, LocalDateTime timestamp) throws InputMismatchException {
        super(appointedId, judgeId, appointedId+"-"+judgeId, timestamp);
        _comment = comment;
        _value = set_value(value);
    }

    public Rating(int value, User appointed, User judge) throws InputMismatchException {
        super(appointed.get_username() + "-" + judge.get_username() + "-" + value);
        _value = set_value(value);
        _appointed = appointed;
        _judge = judge;
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
        if (_appointed == null || _judge == null)
            return "Rating(value:" + _value +
                    ",appointed:" + _primaryId +
                    ",judge:" + _secondaryId +
                    ",timestamp" + _timestamp +
                    ")";
        else
            return "Rating(value:" + _value +
                    ",appointed:" + _appointed.get_title() +
                    ",judge:" + _judge.get_title() +
                    ",timestamp" + _timestamp +
                    ")";
    }

    /**
     * Can be added to a model to indicate the rating that the model is appreciated.
     * Is created by a user.
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
