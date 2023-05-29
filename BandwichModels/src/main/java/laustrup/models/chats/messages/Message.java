package laustrup.models.chats.messages;

import laustrup.models.Model;
import laustrup.dtos.chats.messages.MessageDTO;
import laustrup.models.users.User;
import laustrup.utilities.parameters.Plato;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static laustrup.services.DTOService.convertFromDTO;

/** An abstract class that contains common attributes for Messages. */
public abstract class Message extends Model {

    /** The User that wrote the Message. */
    @Getter
    protected User _author;

    /** The content of the written Message. */
    @Getter @Setter
    protected String _content;

    /** True if the Message is sent. */
    @Getter @Setter
    protected boolean _sent;

    /**
     * A Plato object, that will be true if the Message has been edited.
     * Undefined if it hasn't been yet and not sent, but false if it is sent and also not edited.
     */
    @Getter
    protected Plato _edited;

    /** Can be switch between both true and false, if true the message is public for every User. */
    @Getter @Setter
    protected boolean _public;

    /**
     * Converts a Data Transport Object into this object.
     * @param message The Data Transport Object that will be converted.
     */
    public Message(MessageDTO message) {
        super(message.getPrimaryId(), "Message-"+message.getPrimaryId(), message.getTimestamp());
        _author = convertFromDTO(message.getAuthor());
        _content = message.getContent();
        _sent = message.isSent();
        _edited = new Plato(message.getIsEdited());
        _public = message.isPublic();
    }

    /**
     * With all the values as parameters, is meant for being constructed from database.
     * @param id The unique id of this Message.
     * @param author The creator of this Message.
     * @param content The descriptive content of this Message.
     * @param isSent Is this Mail sent?
     * @param isEdited Is this Mail edited after it is sent?
     * @param isPublic Is this Mail public at the moment?
     * @param timestamp The date and time this Mail was sent.
     */
    public Message(long id, User author, String content, boolean isSent, Plato isEdited, boolean isPublic,
                   LocalDateTime timestamp) {
        super(id, "Message-"+id,timestamp);
        _author = author;
        _content = content;
        _sent = isSent;
        _edited = isEdited;
        _public = isPublic;
    }

    /**
     * For creating this Message.
     * @param author The creator of this Message.
     */
    public Message(User author) {
        super("Message-by:" + author.get_title());
        _author = author;
        _content = new String();
        _sent = false;
        _edited = new Plato();
        _public = false;
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
}
