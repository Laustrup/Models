package laustrup.models.chats.messages;

import laustrup.models.Model;
import laustrup.dtos.chats.messages.BulletinDTO;
import laustrup.models.users.User;
import laustrup.utilities.parameters.Plato;

import lombok.Getter;

import java.time.LocalDateTime;

import static laustrup.services.DTOService.convertFromDTO;

/** A kind of post, that can be attached to every kind of Model. */
public class Bulletin extends Message {

    /** The Model class that this Bulletin will be posted to. */
    @Getter
    public Model _receiver;

    /**
     * Converts a Data Transport Object into this object.
     * @param bulletin The Data Transport Object that will be converted.
     */
    public Bulletin(BulletinDTO bulletin) {
        super(bulletin.getPrimaryId(), convertFromDTO(bulletin.getAuthor()),
                bulletin.getContent(), bulletin.isSent(), new Plato(bulletin.getIsEdited()), bulletin.isPublic(),
                bulletin.getTimestamp());
        _receiver = convertFromDTO(bulletin.getReceiver());
    }

    /**
     * Contains all its values as parameters, are meant for building from database.
     * @param id The unique id of this Bulletin.
     * @param author The creator of this Bulletin.
     * @param receiver The Model that the Bulletin is meant for.
     * @param content The message content of this Bulletin.
     * @param isSent Determines if it has been sent yet or is a draft.
     * @param isEdited Determines if it has been edited or changed after it has been changes.
     * @param isPublic Determines if it is allowed to be view by other than the author.
     * @param timestamp The date and time this Bulletin was sent.
     */
    public Bulletin(long id, User author, Model receiver, String content,
                    boolean isSent, Plato isEdited, boolean isPublic,
                    LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _receiver = receiver;
    }

//    /**
//     * Without author or receiver.
//     * @param id The unique id of this Bulletin.
//     * @param content The message content of this Bulletin.
//     * @param isSent Determines if it has been sent yet or is a draft.
//     * @param isEdited Determines if it has been edited or changed after it has been changes.
//     * @param isPublic Determines if it is allowed to be view by other than the author.
//     * @param timestamp The date and time this Bulletin was sent.
//     */
//    public Bulletin(long id, String content, boolean isSent, Plato isEdited, boolean isPublic, LocalDateTime timestamp) {
//        super(id, null, content, isSent, isEdited, isPublic, timestamp);
//    }

    public Bulletin(User author, String content) {
        super(author);
        _content = content;
        _sent = false;
        _edited = new Plato();
    }

    /**
     * For when a creating a new Bulletin.
     * @param author The creator of this Bulletin.
     * @param receiver The Model that the Bulletin is meant for.
     * @param content The message content of this Bulletin.
     */
    public Bulletin(User author, User receiver, String content) {
        super(author);
        _receiver = receiver;
        _content = content;
        _sent = false;
        _edited = new Plato();
    }

    @Override
    public String toString() {
        return "Bulletin(" +
                    "id:" + _primaryId +
                    ",content:" + _content +
                    ",isSent:" + _sent +
                    (_edited == null ? "" : ",isEdited:" + _edited.get_argument()) +
                    ",isPublic:" + _public +
                ")";
    }
}
