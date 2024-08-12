package laustrup.models.chats.messages;

import laustrup.models.Model;
import laustrup.models.User;
import laustrup.services.DTOService;
import laustrup.utilities.parameters.Plato;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

import static laustrup.models.User.UserDTO;

/**
 * An abstract class that contains common attributes for Messages.
 */
@Getter @FieldNameConstants
public abstract class Message extends Model {

    /**
     * The User that wrote the Message.
     */
    @Getter
    protected User _author;

    /**
     * The content of the written Message.
     */
    @Setter
    protected String _content;

    /**
     * True if the Message is sent.
     */
    @Setter
    protected boolean _sent;

    /**
     * A Plato object, that will be true if the Message has been edited.
     * Undefined if it hasn't been yet and not sent, but false if it is sent and also not edited.
     */
    protected Plato _edited;

    /**
     * Can be switched between both true and false, if true the message is public for every User.
     */
    @Setter
    protected boolean _public;

    /**
     * Converts a Data Transport Object into this object.
     * @param message The Data Transport Object that will be converted.
     */
    public Message(DTO message) {
        super(message);
        _author = DTOService.convert(message.getAuthor());
        _content = message.getContent();
        _sent = message.isSent();
        _edited = new Plato(message.getIsEdited());
        _public = message.isPublic();
    }

    /**
     * A constructor with all the values of this Object.
     * @param id The primary id that identifies this unique Object.
     * @param author The User that wrote the Message.
     * @param content The content of the written Message.
     * @param isSent True if the Message is sent.
     * @param isEdited A Plato object, that will be true if the Message has been edited.
     *                 Undefined if it hasn't been yet and not sent, but false if it is sent and also not edited.
     * @param isPublic Can be switched between both true and false, if true the message is public for every User.
     * @param timestamp Specifies the time this entity was created.
     */
    public Message(
            UUID id,
            User author,
            String content,
            boolean isSent,
            Plato isEdited,
            boolean isPublic,
            LocalDateTime timestamp
    ) {
        super(id, "Message-"+id,timestamp);
        _author = author;
        _content = content;
        _sent = isSent;
        _edited = isEdited;
        _public = isPublic;
    }

    /**
     * Generating a new Message as a draft.
     * Timestamp will be of now.
     * @param author The User that wrote the Message.
     * @param content The content of the written Message.
     * @param isSent True if the Message is sent.
     * @param isEdited A Plato object, that will be true if the Message has been edited.
     *                 Undefined if it hasn't been yet and not sent, but false if it is sent and also not edited.
     * @param isPublic Can be switched between both true and false, if true the message is public for every User.
     */
    public Message(User author, String content, boolean isSent, Plato isEdited, boolean isPublic) {
        super(null, "New Message");
        _author = author;
        _content = content;
        _sent = isSent;
        _edited = isEdited;
        _public = isPublic;
    }

    /**
     * Will set the sent attribute to true.
     * @return The sent attribute.
     */
    public boolean doSend() {
        _sent = true;
        return _sent;
    }

    /**
     * Will edit the content and also set edited to true, if it is sent.
     * @param content The updated content, that is wished to be replaced with the old content.
     * @return The content of the Message.
     */
    public String edit(String content) {
        _content = content;
        if (_sent && !_edited.get_truth())
            _edited.set_argument(true);

        return _content;
    }

    /**
     * An abstract class that contains common attributes for Messages.
     */
    @Getter
    protected static class DTO extends ModelDTO {

        /**
         * The User that wrote the Message.
         */
        protected UserDTO author;

        /**
         * The content of the written Message.
         */
        protected String content;

        /**
         * True if the Message is sent.
         */
        protected boolean isSent;

        /**
         * A Plato object, that will be true if the Message has been edited.
         * Undefined if it hasn't been yet and not sent, but false if it is sent and also not edited.
         */
        protected Plato.Argument isEdited;

        /**
         * Can be switched between both true and false, if true the message is public for every User.
         */
        protected boolean isPublic;

        /**
         * Converts into this DTO Object.
         * @param message The Object to be converted.
         */
        public DTO(Message message) {
            super(message);
            this.author = DTOService.convert(message.get_author());
            this.content = message.get_content();
            this.isSent = message.is_sent();
            this.isEdited = message.get_edited().get_argument();
            this.isPublic = message.is_public();
        }
    }
}
