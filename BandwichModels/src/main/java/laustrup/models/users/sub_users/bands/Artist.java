package laustrup.models.users.sub_users.bands;

import laustrup.models.Model;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.events.Event;
import laustrup.models.events.Gig;
import laustrup.models.users.User;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.sub_users.Performer;
import laustrup.models.users.subscriptions.Subscription;

import laustrup.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * An Artist can either be a solo Performer or member of a Band, which changes the Subscription, if it ain't freemium.
 * Extends from Performer.
 */
@Getter @FieldNameConstants
public class Artist extends Performer {

    /** The Bands that the Artist is a member of. */
    private Seszt<Band> _bands;

    /** A description of the gear, that the Artist possesses and what they require for an Event. */
    @Setter
    private String _runner;

    /** The Requests requested for this Artist. */
    private Liszt<Request> _requests;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param artist The transport object to be transformed.
     */
    public Artist(DTO artist) {
        super(artist);
        _bands = new Seszt<>();
        for (Band.DTO band : artist.getBands())
            _bands.add(new Band(band));

        _runner = artist.getRunner();

        _requests = new Liszt<>();
        for (Request.DTO request : artist.getRequests())
            _requests.add(new Request(request));
    }

    /**
     * A constructor with all the values of this Object.
     * @param id The primary id that identifies this unique Object.
     * @param username The title of the user, that the user uses to use as a title for the profile.
     * @param firstName The real first name of the user's name.
     * @param lastName The real last name of the user's name.
     * @param description This is what the user uses to describe itself.
     * @param contactInfo An object that has the different attributes,
     *                    that can be used to contact this user.
     * @param albums An album consisting of images.
     * @param ratings Ratings made from other users on this user based on a value.
     * @param events The Events that this user is included in.
     * @param gigs Describes all the gigs, that the Performer is a part of an act.
     * @param chatRooms These ChatRooms can be used to communicate with other users.
     * @param subscription The Subscription of this Artist.
     * @param bulletins Messages by other Users.
     * @param bands The Bands that the Artist is a member of.
     * @param runner A description of the gear, that the Artist possesses and what they require for an Event.
     * @param fans All the participants that are following this Performer, is included here.
     * @param idols The people that are following this Object.
     * @param requests The Requests requested for this Artist.
     * @param timestamp The date and time this ContactInfo was created.
     */
    public Artist(
            UUID id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Liszt<Album> albums,
            Liszt<Rating> ratings,
            Liszt<Event> events,
            Liszt<Gig> gigs,
            Liszt<ChatRoom> chatRooms,
            Subscription subscription,
            Liszt<Bulletin> bulletins,
            Seszt<Band> bands,
            String runner,
            Liszt<User> fans,
            Liszt<User> idols,
            Liszt<Request> requests,
            LocalDateTime timestamp
    ) {
        super(id, username, firstName, lastName, description, contactInfo, Authority.ARTIST, albums, ratings,
                events, gigs, chatRooms, subscription, bulletins, fans, idols, timestamp);
        _bands = bands;
        _runner = runner;
        _requests = requests;
    }

    /**
     * Adds a Band to the Liszt of bands.
     * @param band A specific Band, that is wished to be added.
     * @return The whole Liszt of bands.
     */
    public Seszt<Band> add(Band band) {
        return add(new Band[]{band});
    }

    /**
     * Adds multiple Bands to the Liszt of bands.
     * @param bands Some specific Bands, that is wished to be added.
     * @return The whole Liszt of bands.
     */
    public Seszt<Band> add(Band[] bands) {
        return _bands.Add(bands);
    }

    /**
     * Removes a Band from the Liszt of bands.
     * @param band A specific Band, that is wished to be removed.
     * @return The whole Liszt of bands.
     */
    public Seszt<Band> remove(Band band) {
        return remove(new Band[]{band});
    }

    /**
     * Removes multiple Bands from the Liszt of bands.
     * @param bands Some specific Bands, that is wished to be removed.
     * @return The whole Liszt of bands.
     */
    public Seszt<Band> remove(Band[] bands) {
        return _bands.remove(bands);
    }

    /**
     * Adds a Request to the Liszt of Requests.
     * @param request An object of Request, that is wished to be added.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> add(Request request) {
        return _requests.Add(request);
    }

    /**
     * Removes a Request of the Liszt of Requests.
     * @param request An object of Request, that is wished to be removed.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> remove(Request request) {
        return _requests.remove(new Request[]{request});
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
                String.valueOf(get_timestamp())
            });
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO extends PerformerDTO {

        /** The Bands that the Artist is a member of. */
        private Band.DTO[] bands;

        /** A description of the gear, that the Artist possesses and what they require for an Event. */
        private String runner;

        /** The Requests requested for this Artist. */
        private Request.DTO[] requests;

        /**
         * Converts into this DTO Object.
         * @param artist The Object to be converted.
         */
        public DTO(Artist artist) {
            super(artist);
            bands = new Band.DTO[artist.get_bands().size()];
            for (int i = 0; i < bands.length; i++)
                bands[i] = new Band.DTO(artist.get_bands().Get(i+1));
            runner = artist.get_runner();
            requests = new Request.DTO[artist.get_requests().size()];
            for (int i = 0; i < requests.length; i++)
                requests[i] = new Request.DTO(artist.get_requests().Get(i+1));
        }
    }
}
