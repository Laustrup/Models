package laustrup.models.chats.messages;

import laustrup.models.Model;
import laustrup.dtos.chats.messages.BulletinDTO;
import laustrup.models.users.User;
import laustrup.utilities.parameters.Plato;
import laustrup.services.DTOService;

import lombok.Getter;

import java.time.LocalDateTime;

public class Bulletin extends Message {

    @Getter
    public Model _receiver;

    public Bulletin(BulletinDTO bulletin) {
        super(bulletin.getPrimaryId(), DTOService.get_instance().convertFromDTO(bulletin.getAuthor()),
                bulletin.getContent(), bulletin.isSent(), new Plato(bulletin.getIsEdited()), bulletin.isPublic(),
                bulletin.getTimestamp());
        _receiver = DTOService.get_instance().convertFromDTO(bulletin.getReceiver());
    }
    public Bulletin(long id, User author, Model receiver, String content,
                    boolean isSent, Plato isEdited, boolean isPublic,
                    LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _receiver = receiver;
    }

    public Bulletin(long id, String content, boolean isSent, Plato isEdited, boolean isPublic, LocalDateTime timestamp) {
        super(id, null, content, isSent, isEdited, isPublic, timestamp);
        _assembling = true;
    }

    public Bulletin(User author, String content) {
        super(author);
        _content = content;
    }

    public Model set_reciever(Model reciever) {
        if (_assembling)
            _receiver = reciever;
        return _receiver;
    }

    public User set_author(User author) {
        if (_assembling)
            _author = author;
        return _author;
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
