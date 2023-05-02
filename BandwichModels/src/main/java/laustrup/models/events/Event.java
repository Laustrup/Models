package laustrup.models.events;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.console.Printer;
import laustrup.models.Model;
import laustrup.models.albums.Album;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Bulletin;
import laustrup.dtos.albums.AlbumDTO;
import laustrup.dtos.chats.RequestDTO;
import laustrup.dtos.chats.messages.BulletinDTO;
import laustrup.dtos.events.EventDTO;
import laustrup.dtos.events.GigDTO;
import laustrup.dtos.events.ParticipationDTO;
import laustrup.models.users.User;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.sub_users.Performer;
import laustrup.models.users.sub_users.venues.Venue;
import laustrup.utilities.parameters.Plato;
import laustrup.services.DTOService;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.InputMismatchException;

/**
 * An Event is placed a gig, where a venue is having bands playing at specific times.
 */
public class Event extends Model {

    /**
     * Is when the participants can enter the event,
     * not necessarily the same time as when the gigs start.
     * It must be before or same time as the gigs start.
     */
    @Getter
    private LocalDateTime _openDoors;

    /**
     * This DateTime is determining when the first gig will start.
     * Is being calculated automatically.
     */
    @Getter
    private LocalDateTime _start;

    /**
     * This DateTime is determining when the last gig will end.
     * Is being calculated automatically.
     */
    @Getter
    private LocalDateTime _end;

    /**
     * The amount of time it will take the gigs in total.
     * Is being calculated automatically.
     */
    @Getter
    private long _length;

    /**
     * The description of the Event, that can be edited by Performers or Venue
     */
    @Getter @Setter
    private String _description;

    /**
     * This Event is paid or voluntary.
     */
    @Getter @Setter
    private Plato _voluntary;

    /**
     * If this is a public Event, other Users can view and interact with it.
     */
    @Getter
    private Plato _public;

    /**
     * Will be true, if this Event is cancelled.
     * Can only be cancelled by the Venue.
     */
    @Getter
    private Plato _cancelled;

    /**
     * This is marked if there is no more tickets to sell.
     */
    @Getter @Setter
    private Plato _soldOut;

    /**
     * This is the address or place, whether the Event will be held.
     * Will be used to be search at in Google Maps.
     */
    @Getter @Setter
    private String _location;

    /**
     * The cost of a ticket.
     * Can be multiple prices for tickets, but this is just meant as the cheapest.
     */
    @Getter @Setter
    private double _price;

    /**
     * A link for where the tickets can be sold.
     * This might be altered later on,
     * since it is wished to be sold through Bandwich.
     * If the field isn't touched, it will use the Venue's location.
     */
    @Getter @Setter
    private String _ticketsURL;

    /**
     * Different information of contacting.
     */
    @Getter
    private ContactInfo _contactInfo;

    /**
     * The gigs with times and acts of the Event.
     */
    @Getter
    private Liszt<Gig> _gigs;

    /**
     * This venue is the ones responsible for the Event,
     * perhaps even the place it is held, but not necessarily.
     */
    @Getter
    private Venue _venue;

    /**
     * These requests are needed to make sure, everyone wants to be a part of the Event.
     */
    @Getter
    private Liszt<Request> _requests;

    /**
     * The people that will participate in the Event,
     * not including venues or acts.
     */
    @Getter
    private Liszt<Participation> _participations;

    /**
     * Post from different people, that will mention contents.
     */
    @Getter
    private Liszt<Bulletin> _bulletins;

    /**
     * An Album of images, that can be used to promote this Event.
     */
    @Getter @Setter
    private Liszt<Album> _albums;

    public Event(EventDTO event) {
        super(event.getPrimaryId(), event.getTitle(), event.getTimestamp());

        _description = event.getDescription();

        _gigs = new Liszt<>();
        for (GigDTO gig : event.getGigs())
            _gigs.add(new Gig(gig));

        if (!_gigs.isEmpty())
            try {
                calculateTime();
            } catch (InputMismatchException e) {
                Printer.get_instance().print("End date is before beginning date of " + _title + "...", e);
            }
        else {
            _start = event.getOpenDoors() != null ? event.getOpenDoors() : null;
            _end = event.getEnd() != null ? event.getEnd() : (event.getOpenDoors() != null ? event.getOpenDoors() : null);
            _length = _start != null && _end != null ? Duration.between(_start,_end).toMinutes() : 0;
        }

        if (_start != null && _end != null)
            if (Duration.between(event.getOpenDoors(), _start).toMinutes() >= 0)
                _openDoors = event.getOpenDoors();
            else
                throw new InputMismatchException();

        _voluntary = new Plato(event.getIsVoluntary());
        _public = new Plato(event.getIsPublic());
        _cancelled = new Plato(event.getIsCancelled());
        _soldOut = new Plato(event.getIsSoldOut());
        _price = event.getPrice();
        _ticketsURL = event.getTicketsURL();
        _contactInfo = new ContactInfo(event.getContactInfo());
        _venue = (Venue) DTOService.get_instance().convertFromDTO(event.getVenue());

        _location = event.getLocation() == null || event.getLocation().isEmpty() ?
                (event.getVenue() != null ? (event.getLocation() != null ? event.getLocation() : null)
                        : null) : event.getLocation();

        _requests = new Liszt<>();
        for (RequestDTO request : event.getRequests())
            _requests.add(new Request(request));

        _participations = new Liszt<>();
        for (ParticipationDTO participation : event.getParticipations())
            _participations.add(new Participation(participation));

        _bulletins = new Liszt<>();
        for (BulletinDTO bulletin : event.getBulletins())
            _bulletins.add(new Bulletin(bulletin));

        _albums = new Liszt<>();
        for (AlbumDTO album : event.getAlbums())
            _albums.add(new Album(album));
    }
    public Event(long id) {
        super(id);
    }

    public Event(long id, String title, String description, LocalDateTime openDoors, Plato isVoluntary, Plato isPublic,
                 Plato isCancelled, Plato isSoldOut, String location, double price, String ticketsURL,
                 ContactInfo contactInfo, Liszt<Gig> gigs, Venue venue, Liszt<Request> requests,
                 Liszt<Participation> participations, Liszt<Bulletin> bulletins, Liszt<Album> albums,
                 LocalDateTime timestamp) throws InputMismatchException {
        super(id, title, timestamp);

        _description = description;
        _gigs = gigs;

        if (!_gigs.isEmpty())
            try {
                calculateTime();
            } catch (InputMismatchException e) {
                Printer.get_instance().print("End date is before beginning date of " + _title + "...", e);
            }
        else
            _start = openDoors;

        if (_start != null && _end != null)
            if (Duration.between(openDoors, _start).toMinutes() >= 0)
                _openDoors = openDoors;
            else
                throw new InputMismatchException();

        _voluntary = isVoluntary;
        _public = isPublic;
        _cancelled = isCancelled;
        _soldOut = isSoldOut;
        _price = price;
        _ticketsURL = ticketsURL;
        _contactInfo = contactInfo;
        _venue = venue;

        _location = location == null || location.isEmpty() ?
                (venue != null ? (venue.get_location() != null ? venue.get_location() : null)
                    : null) : location;

        _requests = requests;
        _participations = participations;
        _bulletins = bulletins;
        _albums = albums;

        _assembling = true;
    }

    public Event(String title, User user) {
        super(title);

        _gigs = new Liszt<>();
        if (user.get_authority() == User.Authority.BAND || user.get_authority() == User.Authority.ARTIST)
            _gigs.add(new Gig(new Performer[]{(Performer) user}));
        else _venue = (Venue) user;

        _participations = new Liszt<>();
        _bulletins = new Liszt<>();
        _albums = new Liszt<>();

        _length = 0;
        _cancelled = new Plato();
    }

    /**
     * Sets the Users of Requests.
     * Will only be done, if it is under assembling.
     * @return The Requests of this Artist.
     */
    public Liszt<Request> set_requestEvents() {
        if (_assembling)
            for (int i = 1; i <= _requests.size(); i++)
                _requests.Get(i).set_event(this);
        return _requests;
    }

    /**
     * Sets the Participations.
     * Will only be done, if it is under assembling.
     * @param participations The Participations that will be set.
     * @return The Participations of the Event.
     */
    public Liszt<Participation> set_participations(Liszt<Participation> participations) {
        if (_assembling)
            _participations = participations;
        return _participations;
    }

    /**
     * Sets the Gigs. Is only allowed under assembling.
     * @param gigs The Gigs that will be set to be the Gigs.
     * @return All the Gigs.
     */
    public Liszt<Gig> set_gigs(Liszt<Gig> gigs) {
        if (_assembling)
            _gigs = gigs;
        return _gigs;
    }

    /**
     * Adds the given Gig to gigs of current Event.
     * @param gig Determines a specific Gig of one MusicalUser for a specific time.
     * @return All the Gigs of the current Event.
     */
    public Liszt<Gig> add(Gig gig) { return add(new Gig[]{gig}); }

    /**
     * Adds multiple given Gigs to the Liszt of Gigs in the current Event.
     * @param gigs Determines some specific Gigs of one MusicalUser for a specific time.
     * @return All the Gigs of the current Event.
     */
    public Liszt<Gig> add(Gig[] gigs) {
        gigs = filterGigs(gigs);

        if (gigs.length > 0) {
            _gigs.add(gigs);
            add(createRequests(gigs));

            try {
                calculateTime();
            } catch (InputMismatchException e) {
                Printer.get_instance().print("End date is before beginning date of " + _title + "...", e);
                _gigs.remove(gigs);
            }
        }
        return _gigs;
    }

    /**
     * Removes all the Gigs, that already exists in this Event.
     * @param gigs The Gigs that should be filtered.
     * @return The filtered Gigs.
     */
    private Gig[] filterGigs(Gig[] gigs) {
        Gig[] storage = new Gig[gigs.length];

        for (int i = 0; i < gigs.length; i++)
            for (Performer stranger : gigs[i].get_act())
                for (Gig gig : _gigs)
                    for (Performer performer : gig.get_act())
                        if (stranger.get_primaryId() != performer.get_primaryId() &&
                            !gigs[i].get_start().isEqual(gig.get_start()) &&
                            !gigs[i].get_end().isEqual(gig.get_end()))
                            storage[i] = gigs[i];

        int length = 0;
        for (int i = 0; i < storage.length; i++)
            if (storage[i]!=null)
                length++;

        int index = 0;
        Gig[] filtered = new Gig[length];
        for (int i = 0; i < storage.length; i++) {
            if (storage[i]!=null) {
                filtered[index] = storage[i];
                index++;
            }
        }

        return filtered;
    }

    /**
     * Removes the given Gig from gigs of current Event.
     * @param gig Determines a specific gig, that is wished to be removed.
     * @return All the gigs of the current Event.
     */
    public Liszt<Gig> remove(Gig gig) { return remove(new Gig[]{gig}); }

    /**
     * Removes some given Gigs from the Liszt of gigs from current Event.
     * Also removes the Requests of the given Gigs.
     * @param gigs Determines some specific gigs, that is wished to be removed.
     * @return All the Gigs of the current Event.
     */
    public Liszt<Gig> remove(Gig[] gigs) {
        _gigs.remove(gigs);
        for (Gig gig : gigs)
            for (Performer performer : gig.get_act())
                if (!isPerformerInOtherGigs(performer))
                    removeRequest(performer);

        calculateTime();

        return _gigs;
    }

    private boolean isPerformerInOtherGigs(Performer performer) {
        boolean isInOtherGig = false;
        for (Gig gig : _gigs)
            for (Performer gigPerformer : gig.get_act())
                if (gigPerformer.get_primaryId() == performer.get_primaryId())
                    isInOtherGig = true;

        return isInOtherGig;
    }

    private Liszt<Request> removeRequest(Performer performer) {
        for (int i = 1; i <= _requests.size(); i++) {
            if (_requests.Get(i).get_user().get_primaryId() == performer.get_primaryId()) {
                _requests.remove(i);
                break;
            }
        }
        return _requests;
    }

    /**
     * Adds the given Request to requests of current Event.
     * @param request Determines a specific Request, that is wished to be added.
     * @return All the Requests of the current Event.
     */
    public Liszt<Request> add(Request request) { return add(new Request[]{request}); }

    /**
     * Adds some given Requests to the Liszt of requests from current Event.
     * @param requests Determines some specific requests, that is wished to be added.
     * @return All the requests of the current Event.
     */
    public Liszt<Request> add(Request[] requests) {
        for (Request request : requests)
            if (!_requests.contains(request))
                _requests.add(request);

        return _requests;
    }

    /**
     * Creates some requests for the gigs that are about to be created to this Event.
     * Must be done after add of gigs.
     * @param gigs The gigs that are about to be created a Request for the current gig.
     * @return All the created Requests.
     */
    private Request[] createRequests(Gig[] gigs) {
        Request[] requests = new Request[gigs.length];

        for (int i = 0; i < gigs.length; i++) {
            for (User user : gigs[i].get_act()) {
                boolean requestAlreadyExist = false;
                for (Request request : _requests) {
                    if (request.get_user().get_primaryId() == user.get_primaryId()) {
                        requestAlreadyExist = true;
                        break;
                    }
                }
                if (!requestAlreadyExist)
                    requests[i] = new Request(user,this, new Plato());
            }
        }

        return requests;
    }

    /**
     * Accepts the request by using a toString() to find the Request.
     * Afterwards using the approve() method to set approved to true.
     * @param request The Request that is wished to have its approved set to true.
     * @return The Request that is changed. If it is not changed, it returns null.
     */
    public Request acceptRequest(Request request) {
        return _requests.set(request, new Request(request.get_user(), request.get_event(), new Plato(true)));
    }

    /**
     * Checks if the Venue has approved the Request.
     * @return True if the venue has approved.
     */
    public boolean venueHasApproved() {
        for (Request request : _requests)
            if (request.get_user().getClass() == Venue.class && request.get_approved().get_truth())
                return true;

        return false;
    }

    /**
     * Sets the Venue and also replaces the request of former Venue to a new request.
     * This means that the Event will become private instead of public, since the
     * Venue needs to approve the Event, in order to host it.
     * @param venue The Venue, that is wished to be set, as the Events new Venue.
     * @return The Venue that is set of the Event.
     */
    public Venue set_venue(Venue venue) {
        for (Request request : _requests) {
            if (request.get_user().getClass() == Venue.class
                    && request.get_user().get_primaryId() == _venue.get_primaryId()) {
                _requests.remove(request);
                break;
            }
        }

        _public.set_argument(false);
        _venue = venue;
        _requests.add(new Request(venue, this, new Plato(Plato.Argument.UNDEFINED)));

        return _venue;
    }

    /**
     * Will cancel the event, if it is the Venue in the input.
     * @param venue Will check if this Venue has the same id as the Venue of this Event.
     * @return The isCancelled Plato value.
     */
    public Plato changeCancelledStatus(Venue venue) {
        if (venue.get_primaryId() == _venue.get_primaryId())
            _cancelled.set_argument(!_cancelled.get_truth());

        return _cancelled;
    }

    /**
     * Adds the given Participation to participations of current Event.
     * @param participation Determines a specific participant, that is wished to be added.
     * @return All the Participations of the current Event.
     */
    public Liszt<Participation> addParticipation(Participation participation) {
        return addParticpations(new Participation[]{participation});
    }

    /**
     * Adds some given Participations to the Liszt of participations from current Event.
     * @param participations Determines some specific participants, that is wished to be added.
     * @return All the Participations of the current Event.
     */
    public Liszt<Participation> addParticpations(Participation[] participations) {
        _participations.add(participations);
        return _participations;
    }

    /**
     * Removes the given Participation from participations of current Event.
     * @param participation Determines a specific participant, that is wished to be removed.
     * @return All the Participations of the current Event.
     */
    public Liszt<Participation> removeParticipation(Participation participation) {
        return removeParticpations(new Participation[]{participation});
    }

    /**
     * Removes some given Participations from the Liszt of participations from current Event.
     * @param participations Determines some specific participants, that is wished to be removed.
     * @return All the Participations of the current Event.
     */
    public Liszt<Participation> removeParticpations(Participation[] participations) {
        _participations.remove(participations);
        return _participations;
    }

    /**
     * Adds the given Bulletin to bulletins of current Event.
     * @param bulletin Determines a specific bulletin, that is wished to be added.
     * @return All the bulletins of the current Event.
     */
    public Liszt<Bulletin> add(Bulletin bulletin) {
        return add(new Bulletin[]{bulletin});
    }

    /**
     * Adds some given Bulletins to the Liszt of bulletins from current Event.
     * @param bulletins Determines some specific bulletins, that is wished to be added.
     * @return All the bulletins of the current Event.
     */
    public Liszt<Bulletin> add(Bulletin[] bulletins) {
        _bulletins.add(bulletins);
        return _bulletins;
    }

    /**
     * Removes the given Bulletin from bulletins of current Event.
     * @param bulletin Determines a specific bulletin, that is wished to be removed.
     * @return All the bulletins of the current Event.
     */
    public Liszt<Bulletin> removeBulletin(Bulletin bulletin) {
        return removeBulletins(new Bulletin[]{bulletin});
    }

    /**
     * Removes some given Bulletins from the Liszt of bulletins from current Event.
     * @param bulletins Determines some specific bulletins, that is wished to be removed.
     * @return All the bulletins of the current Event.
     */
    public Liszt<Bulletin> removeBulletins(Bulletin[] bulletins) {
        _bulletins.remove(bulletins);
        return _bulletins;
    }

    /**
     * Sets a Participation, by comparing the participants' ids.
     * @param participation The Participation that is wished to be set.
     * @return If the Participation is set successfully, it will return the Participation, else it will return null.
     */
    public Participation setParticipation(Participation participation) {
        for (int i = 1; i <= _participations.size(); i++) {
            if (_participations.Get(i).get_participant().get_primaryId() == participation.get_participant().get_primaryId()) {
                _participations.Get(i).set_type(participation.get_type());
                return _participations.Get(i);
            }
        }
        return null;
    }

    /**
     * Will set the values of a Gig in the Event.
     * @param gig The Gig, that is wished to be set.
     * @return If the Gig is set successfully, it will return the Gig, else it will return null.
     */
    public Gig setGig(Gig gig) {
        for (int i = 1; i <= _gigs.size(); i++) {
            int sharedActs = 0;

            for (int j = 0; j < _gigs.Get(i).get_act().length; j++) {
                for (Performer performer : gig.get_act()) {
                    if (_gigs.Get(i).get_act()[j].get_primaryId() == performer.get_primaryId()) {
                        sharedActs++;
                    }
                }
            }
            if (sharedActs == _gigs.Get(i).get_act().length) {
                _gigs.Get(i).set_start(gig.get_start());
                _gigs.Get(i).set_end(gig.get_end());
            }
        }
        return null;
    }

    /**
     * Sets the beginning and end of the event to match all the current gigs.
     * Important to use after each change of gigs.
     * @return The calculated length between first beginning and latest end in milliseconds.
     * @throws InputMismatchException In case that the end is before the beginning.
     */
    private long calculateTime() throws InputMismatchException {
        _start = _gigs.isEmpty() ? null : _gigs.Get(1).get_start();
        _end = _gigs.isEmpty() ? null : _gigs.Get(1).get_end();

        if ((_end != null && _start != null) && Duration.between(_end, _start).getSeconds() > 0)
            throw new InputMismatchException();

        if (_start != null && _end != null)
            if (_end.isAfter(_start)) {

                if (_gigs.size() > 1)
                    for (Gig gig : _gigs) {
                        if (gig.get_start().isBefore(_start)) _start = gig.get_start();
                        if (gig.get_end().isAfter(_end)) _end = gig.get_end();
                    }

                _length = Duration.between(_start, _end).toMillis();
            }

        return _length;
    }

    @Override
    public String toString() {
        return "Event(id="+_primaryId+
                ",title="+_title+
                ",description="+_description+
                ",price="+_price+
                ",timestamp="+_timestamp+
                ")";
    }
}
