package laustrup.items;

import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.events.Event;
import laustrup.models.users.contact_infos.Address;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.contact_infos.Country;
import laustrup.models.users.contact_infos.Phone;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.sub_users.venues.Venue;
import laustrup.utilities.collections.sets.Seszt;
import laustrup.utilities.console.Printer;

import lombok.Getter;

import java.util.Arrays;
import java.util.Random;

/** Contains the elements needed for TestItem. */
public abstract class TestCollections {

    /** Will be used to create values for attributes. */
    protected final Random _random = new Random();

    /** A collection of the generated Participants. */
    @Getter protected Seszt<Participant> _participants;

    /** A collection of the generated Artists. */
    @Getter protected Seszt<Artist> _artists;

    /** A collection of the generated Bands. */
    @Getter protected Seszt<Band> _bands;

    /** A collection of the generated Venues. */
    @Getter protected Seszt<Venue> _venues;

    /** A collection of the generated Events. */
    @Getter protected Seszt<Event> _events;

    /** A collection of the generated Countries. */
    @Getter protected Seszt<Country> _countries;

    /** A collection of the generated phone numbers. */
    @Getter protected Seszt<Phone> _phones;

    /** A collection of the generated Addresses. */
    @Getter protected Seszt<Address> _addresses;

    /** A collection of the generated ContactInfos. */
    @Getter protected Seszt<ContactInfo> _contactInfo;

    /** A collection of the generated Albums. */
    @Getter protected Seszt<Album> _albums;

    /** A collection of the generated Ratings. */
    @Getter protected Seszt<Rating> _ratings;

    /** A collection of the generated ChatRooms. */
    @Getter protected Seszt<ChatRoom> _chatRooms;

    /**
     * Uses Printer to make a String of each collection in TestItems.
     * @return The made String of collections.
     */
    public String showItems() {
        return Printer.toString(new Object[]{
            _participants.toString(),
            _artists.toString(),
            _bands.toString(),
            _venues.toString(),
            _events.toString(),
            _countries.toString(),
            _phones.toString(),
            _addresses.toString(),
            _contactInfo.toString(),
            _albums.toString(),
            _ratings.toString(),
            _chatRooms.toString()
        });
    }

    /** Will set all values to null. */
    protected void reset() {
        _participants = null;
        _artists = null;
        _bands = null;
        _venues = null;
        _events = null;
        _countries = null;
        _phones = null;
        _addresses = null;
        _contactInfo = null;
        _albums = null;
        _ratings = null;
        _chatRooms = null;
    }
}
