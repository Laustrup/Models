package laustrup.models.users;

import laustrup.models.Model;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Rating;
import laustrup.models.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.Event;
import laustrup.models.User;
import laustrup.utilities.collections.sets.Seszt;
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
    private Seszt<Artist> _members;

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

        _members = new Seszt<>();
        for (Artist.DTO member : band.getMembers())
            _members.add(new Artist(member));

        _runner = band.getRunner();
    }

    /**
     * A constructor with all the values of this Object.
     * @param id The primary id that identifies this unique Object.
     * @param username The title of the user, that the user uses to use as a title for the profile.
     * @param description This is what the user uses to describe itself.
     * @param contactInfo An object that has the different attributes,
     *                    that can be used to contact this Band.
     * @param albums An album consisting of images.
     * @param ratings Ratings made from other users on this user based on a value.
     * @param events The Events that this user is included in.
     * @param gigs Describes all the gigs, that the Performer is a part of an act.
     * @param chatRooms These ChatRooms can be used to communicate with other users.
     * @param subscription The Subscription of this Artist.
     * @param bulletins Messages by other Users.
     * @param members Contains all the Artists, that are members of this band.
     * @param runner A description of the gear, that the Artist possesses and what they require for an Event.
     * @param fans All the participants that are following this Performer, is included here.
     * @param idols The people that are following this Object.
     * @param timestamp The date and time this ContactInfo was created.
     */
    public Band(
            UUID id,
            String username,
            String description,
            ContactInfo contactInfo,
            Liszt<Album> albums,
            Liszt<Rating> ratings,
            Seszt<Event> events,
            Seszt<Event.Gig> gigs,
            Seszt<ChatRoom> chatRooms,
            Subscription subscription,
            Liszt<Bulletin> bulletins,
            Seszt<Artist> members,
            String runner,
            Seszt<User> fans,
            Seszt<User> idols,
            LocalDateTime timestamp
    ) {
        super(id, username, description, contactInfo, Authority.BAND, albums, ratings, events, gigs, chatRooms,
                subscription, bulletins, fans, idols, timestamp);
        if (members.isEmpty())
            throw new InputMismatchException();

        _username = username;
        _members = members;
        _runner = runner;
    }

    /**
     * Adds an Artist to the Liszt of members.
     * @param artist An object of Artist, that is wished to be added.
     * @return The whole Liszt of members.
     */
    public Seszt<Artist> add(Artist artist) {
        return add(new Artist[]{artist});
    }

    /**
     * Adds Artists to the Liszt of members.
     * @param artists An array of artists, that is wished to be added.
     * @return The whole Liszt of members.
     */
    public Seszt<Artist> add(Artist[] artists) {
        return _members.Add(artists);
    }

    /**
     * Removes an Artist of the Liszt of members.
     * @param artist An object of Artist, that is wished to be removed.
     * @return The whole Liszt of members.
     */
    public Seszt<Artist> remove(Artist artist) {
        return remove(new Artist[]{artist});
    }

    /**
     * Removes Artists of the Liszt of members.
     * @param artists An array of artists, that is wished to be removed.
     * @return The whole Liszt of members.
     */
    public Seszt<Artist> remove(Artist[] artists) {
        return _members.remove(artists);
    }

    /**
     * Removes a Fan of the Liszt of fans.
     * @param fan An object of Fan, that is wished to be removed.
     * @return The whole Liszt of fans.
     */
    public Seszt<User> remove(Participant fan) {
        return _fans.remove(new Participant[]{fan});
    }

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
