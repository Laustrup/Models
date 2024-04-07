package laustrup.models.users.sub_users.bands;

import laustrup.models.Model;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.events.Event;
import laustrup.models.events.Gig;
import laustrup.models.users.User;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.sub_users.Performer;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.subscriptions.Subscription;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.UUID;

/** Extends performer and contains Artists as members */
@Getter @FieldNameConstants
public class Band extends Performer {

    /** Contains all the Artists, that are members of this band. */
    private Liszt<Artist> _members;

    /** A description of the gear, that the band possesses and what they require for an Event. */
    @Setter
    private String _runner;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param band The transport object to be transformed.
     */
    public Band(Band.DTO band) {
        super(band);
        _username = band.getUsername();

        _members = new Liszt<>();
        for (Artist.DTO member : band.getMembers())
            _members.add(new Artist(member));

        _runner = band.getRunner();
    }

    public Band(UUID id, String username, String description, ContactInfo contactInfo, Liszt<Album> albums,
                Liszt<Rating> ratings, Liszt<Event> events, Liszt<Gig> gigs, Liszt<ChatRoom> chatRooms,
                Subscription subscription, Liszt<Bulletin> bulletins, Liszt<Artist> members,
                String runner, Liszt<User> fans, Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, description, contactInfo, Authority.BAND, albums, ratings, events, gigs, chatRooms,
                subscription, bulletins, fans, idols, timestamp);
        _username = username;
        _members = members;
        _runner = runner;
    }

    public Band(String username, String description, Subscription subscription, ContactInfo contactInfo,
                Liszt<Artist> members) throws InputMismatchException {
        super(username, description, subscription, Authority.BAND);
        _username = username;
        _contactInfo = contactInfo;

        _members = members;
        if (_members.isEmpty())
            throw new InputMismatchException();
    }

    /**
     * Adds an Artist to the Liszt of members.
     * @param artist An object of Artist, that is wished to be added.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> addMember(Artist artist) { return addMembers(new Artist[]{artist}); }

    /**
     * Adds Artists to the Liszt of members.
     * @param artists An array of artists, that is wished to be added.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> addMembers(Artist[] artists) { return _members.Add(artists); }

    /**
     * Removes an Artist of the Liszt of members.
     * @param artist An object of Artist, that is wished to be removed.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> remove(Artist artist) { return remove(new Artist[]{artist}); }

    /**
     * Removes Artists of the Liszt of members.
     * @param artists An array of artists, that is wished to be removed.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> remove(Artist[] artists) { return _members.remove(artists); }

    /**
     * Removes a Fan of the Liszt of fans.
     * @param fan An object of Fan, that is wished to be removed.
     * @return The whole Liszt of fans.
     */
    public Liszt<User> remove(Participant fan) { return _fans.remove(new Participant[]{fan}); }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._primaryId,
                User.Fields._username,
                User.Fields._description,
                Fields._runner,
                Model.Fields._timestamp
            },
            new String[] {
                String.valueOf(get_primaryId()),
                get_username(),
                get_description(),
                get_runner(),
                Model.Fields._timestamp
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO extends PerformerDTO {

        /** Contains all the Artists, that are members of this band. */
        private Artist.DTO[] members;

        /** A description of the gear, that the band possesses and what they require for an Event. */
        private String runner;

        public DTO(Band band) {
            super(band);
            members = new Artist.DTO[band.get_members().size()];
            for (int i = 0; i < members.length; i++)
                members[i] = new Artist.DTO(band.get_members().Get(i+1));

            runner = band.get_runner();
        }
    }
}
