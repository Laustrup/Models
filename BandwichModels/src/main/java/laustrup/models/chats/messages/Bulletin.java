package laustrup.models.chats.messages;

import laustrup.models.Model;
import laustrup.models.User;
import laustrup.utilities.parameters.Plato;
import laustrup.services.DTOService;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A kind of post that can be posted at any Model Object.
 */
@Getter @FieldNameConstants
public class Bulletin extends Message {

    /**
     * The Model receiver that are having the Bulletin posted at its dashboard.
     */
    public Model _receiver;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param bulletin The transport object to be transformed.
     */
    public Bulletin(DTO bulletin) {
        super(bulletin);
        _receiver = DTOService.convert(bulletin.getReceiver());
    }

    /**
     * A constructor with all the values of a Bulletin.
     * @param id The primary id that identifies this unique Object.
     * @param author The User that wrote the Message.
     * @param receiver The Model receiver that are having the Bulletin posted at its dashboard.
     * @param content The content of the written Message.
     * @param isSent True if the Message is sent.
     * @param isEdited A Plato object, that will be true if the Message has been edited.
     *                 Undefined if it hasn't been yet and not sent, but false if it is sent and also not edited.
     * @param isPublic Can be switched between both true and false, if true the message is public for every User.
     * @param timestamp Specifies the time this entity was created.
     */
    public Bulletin(
            UUID id,
            User author,
            Model receiver,
            String content,
            boolean isSent,
            Plato isEdited,
            boolean isPublic,
            LocalDateTime timestamp
    ) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _receiver = receiver;
    }

    /**
     * Generating a new Bulletin as a draft.
     * Timestamp will be of now.
     * @param author The User that wrote the Message.
     * @param receiver The Model receiver that are having the Bulletin posted at its dashboard.
     * @param content The content of the written Message.
     */
    public Bulletin(
            User author,
            Model receiver,
            String content
    ) {
        super(author, content, false, new Plato(false), false);
        _receiver = receiver;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Model.Fields._primaryId,
                Message.Fields._content,
                Message.Fields._sent,
                Message.Fields._edited,
                Message.Fields._public,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(_primaryId),
                _content,
                String.valueOf(_sent),
                String.valueOf(_edited),
                String.valueOf(_public),
                String.valueOf(_timestamp)
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    public static class DTO extends Message.DTO {

        /**
         * The Model that are receiving this Bulletin.
         */
        public ModelDTO receiver;

        /**
         * Converts into this DTO Object.
         * @param bulletin The Object to be converted.
         */
        public DTO(Bulletin bulletin) {
            super(bulletin);
            receiver = DTOService.convert(bulletin.get_receiver());
        }
    }
}
