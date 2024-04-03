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
import laustrup.utilities.console.Printer;

import lombok.Getter;

import java.util.Arrays;
import java.util.Random;

/** Contains the elements needed for TestItem. */
public abstract class TestCollections {

    /** Will be used to create values for attributes. */
    protected final Random _random = new Random();

    /** Length determine of array collection. */
    @Getter
    protected int
        _ratingAmount = 100,
        _participantAmount = 10,
        _artistAmount = 15,
        _bandAmount = 10,
        _venueAmount = 3,
        _eventAmount = 8,
        _albumAmount = (_artistAmount + _bandAmount + _participantAmount) * 2,
        _addressAmount = _artistAmount + _participantAmount + _venueAmount + 5,
        _phoneNumberAmount = _artistAmount + _participantAmount + _venueAmount + 5,
        _contactInfoAmount = _albumAmount,
        _chatRoomAmount = _bandAmount * _venueAmount * _artistAmount;

    /** A collection of the generated Participants. */
    @Getter protected Participant[] _participants;

    /** A collection of the generated Artists. */
    @Getter protected Artist[] _artists;

    /** A collection of the generated Bands. */
    @Getter protected Band[] _bands;

    /** A collection of the generated Venues. */
    @Getter protected Venue[] _venues;

    /** A collection of the generated Events. */
    @Getter protected Event[] _events;

    /** A collection of the generated Countries. */
    @Getter protected Country[] _countries;

    /** A collection of the generated phone numbers. */
    @Getter protected Phone[] _phones;

    /** A collection of the generated Addresses. */
    @Getter protected Address[] _addresses;

    /** A collection of the generated ContactInfos. */
    @Getter protected ContactInfo[] _contactInfo;

    /** A collection of the generated Albums. */
    @Getter protected Album[] _albums;

    /** A collection of the generated Ratings. */
    @Getter protected Rating[] _ratings;

    /** A collection of the generated ChatRooms. */
    @Getter protected ChatRoom[] _chatRooms;

    /**
     * Uses Printer to make a String of each collection in TestItems.
     * @return The made String of collections.
     */
    public String showItems() {
        return Printer.toString(new Object[]{
                Arrays.toString(_participants),
                Arrays.toString(_artists),
                Arrays.toString(_bands),
                Arrays.toString(_venues),
                Arrays.toString(_events),
                Arrays.toString(_countries),
                Arrays.toString(_phones),
                Arrays.toString(_addresses),
                Arrays.toString(_contactInfo),
                Arrays.toString(_albums),
                Arrays.toString(_ratings),
                Arrays.toString(_chatRooms)
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
