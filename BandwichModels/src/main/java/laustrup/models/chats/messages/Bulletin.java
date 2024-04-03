package laustrup.models.chats.messages;

import laustrup.models.Model;
import laustrup.models.users.User;
import laustrup.utilities.parameters.Plato;
import laustrup.services.DTOService;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Bulletin extends Message {

    public Model _receiver;

    public Bulletin(DTO bulletin) {
        super(bulletin);
        _receiver = DTOService.convert(bulletin.getReceiver());
    }
    public Bulletin(UUID id, User author, Model receiver, String content,
                    boolean isSent, Plato isEdited, boolean isPublic,
                    LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _receiver = receiver;
    }

    public Bulletin(UUID id, String content, boolean isSent, Plato isEdited, boolean isPublic, LocalDateTime timestamp) {
        super(id, null, content, isSent, isEdited, isPublic, timestamp);
    }

    public Bulletin(User author, User receiver, String content) {
        super(author);
        _receiver = receiver;
        _content = content;
    }

    @Override
    public String toString() {
        return defineToString(
                getClass().getSimpleName(),
                new String[]{
                        "id",
                        "content",
                        "isSent",
                        "isEdited",
                        "isPublic",
                        "timestamp"
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

    @Getter
    public static class DTO extends Message.DTO {

        /** The Model that are receiving this Bulletin. */
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
