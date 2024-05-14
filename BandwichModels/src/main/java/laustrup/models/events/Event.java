package laustrup.models.events;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.collections.sets.Seszt;
import laustrup.utilities.console.Printer;
import laustrup.utilities.parameters.Plato;
import laustrup.models.Model;
import laustrup.models.albums.Album;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.users.User;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.sub_users.Performer;
import laustrup.models.users.sub_users.venues.Venue;
import laustrup.services.DTOService;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.UUID;

import static laustrup.services.ObjectService.ifExists;

/** An Event is a place for gigs, where a venue is having bands playing at specific times. */
@Getter @FieldNameConstants
public class Event extends Model {

    /**
     * Is when the participants can enter the event,
     * not necessarily the same time as when the gigs start.
     * It must be before or same time as the gigs start.
     */
    private LocalDateTime _openDoors;

    /**
     * This DateTime is determining when the first gig will start.
     * Is being calculated automatically.
     */
    private LocalDateTime _start;

    /**
     * This DateTime is determining when the last gig will end.
     * Is being calculated automatically.
     */
    private LocalDateTime _end;

    /**
     * The amount of time it will take the gigs in total.
     * Is being calculated automatically.
     */
    private long _length;

    /** The description of the Event, that can be edited by Performers or Venue */
    @Setter
    private String _description;

    /** This Event is paid or voluntary. */
    @Setter
    private Plato _voluntary;

    /** If this is a public Event, other Users can view and interact with it. */
    private Plato _public;

    /**
     * Will be true, if this Event is cancelled.
     * Can only be cancelled by the Venue.
     */
    private Plato _cancelled;

    /** This is marked if there is no more tickets to sell. */
    @Setter
    private Plato _soldOut;

    /**
     * This is the address or place, whether the Event will be held.
     * Will be used to be search at in Google Maps.
     */
    private String _location;

    /**
     * Sets the location.
     * In case that the input is null or empty, it will use the location of the Venue.
     * @param location The new location value.
     * @return The location value.
     */
    public String set_location(String location) {
        _location = location == null || location.isEmpty()
            ? (
                _venue != null
                    ? (
                        _venue.get_location() != null
                            ? _venue.get_location()
                            : null
                    )
                    : null
            )
            : location;

        return _location;
    }

    /**
     * The cost of a ticket.
     * Can be multiple prices for tickets, but this is just meant as the cheapest.
     */
    @Setter
    private double _price;

    /**
     * A link for where the tickets can be sold.
     * This might be altered later on,
     * since it is wished to be sold through Bandwich.
     * If the field isn't touched, it will use the Venue's location.
     */
    @Setter
    private String _ticketsURL;

    /** Different information of contacting. */
    private ContactInfo _contactInfo;

    /** The gigs with times and acts of the Event. */
    private Liszt<Gig> _gigs;

    /**
     * This venue is the ones responsible for the Event,
     * perhaps even the place it is held, but not necessarily.
     */
    private Venue _venue;

    /** These requests are needed to make sure, everyone wants to be a part of the Event. */
    private Liszt<Request> _requests;

    /**
     * The people that will participate in the Event,
     * not including venues or acts.
     */
    private Seszt<Participation> _participations;

    /** Post from different people, that will mention contents. */
    private Liszt<Bulletin> _bulletins;

    /** An Album of images, that can be used to promote this Event. */
    @Setter
    private Liszt<Album> _albums;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param event The transport object to be transformed.
     */
    public Event(DTO event) {
        super(event);

        _description = event.getDescription();

        ifExists(event.getGigs(), () -> {
            _gigs = new Liszt<>();
            for (Gig.DTO gig : event.getGigs())
                _gigs.add(new Gig(gig));
        });

        if (_gigs != null && !_gigs.isEmpty())
            try {
                calculateTime();
            } catch (InputMismatchException e) {
                Printer.print("End date is before beginning date of " + _title + "...", e);
            }
        else {
            _start = event.getOpenDoors() != null ? event.getOpenDoors() : null;
            _openDoors = _start;
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
        _contactInfo = (ContactInfo) ifExists(event.getContactInfo(), e -> new ContactInfo(event.getContactInfo()));
        _venue = (Venue) DTOService.convert(event.getVenue());

        set_location(event.getLocation());

        ifExists(event.getRequests(), () -> {
            _requests = new Liszt<>();
            for (Request.DTO request : event.getRequests())
                _requests.add(new Request(request));
        });

        ifExists(event.getParticipations(), () -> {
            _participations = new Seszt<>();
            for (Participation.DTO participation : event.getParticipations())
                _participations.add(new Participation(participation));
        });

        ifExists(event.getBulletins(), () -> {
            _bulletins = new Liszt<>();
            for (Bulletin.DTO bulletin : event.getBulletins())
                _bulletins.add(new Bulletin(bulletin));
        });

        ifExists(event.getAlbums(), () -> {
            _albums = new Liszt<>();
            for (Album.DTO album : event.getAlbums())
                _albums.add(new Album(album));
        });
    }

    /**
     * An Event with all its values as parameters, is meant for being constructed from database.
     * Start and end times will be calculated from the Gigs.
     * @param id The unique id of this Event.
     * @param title The named title of the Event.
     * @param description A typed String explaining details of this Event.
     * @param openDoors When the Event will let people in.
     * @param isVoluntary Determines whether this Event's workers are paid employees.
     * @param isPublic The visibility of this Event.
     * @param isCancelled If true this Event is cancelled.
     * @param isSoldOut Determines the state of the sale of this Event.
     * @param location Where this Event is being held.
     * @param price The price it costs for participants to participate.
     * @param ticketsURL Where people can buy tickets for this Event.
     * @param contactInfo The information of how to contact the people in charge of this Event.
     * @param gigs The shows that are being held in this Event.
     * @param venue The Venue responsible for arranging this Event.
     * @param requests The Requests between the members of the Gigs and this Event.
     * @param participations Who are joining this Event.
     * @param bulletins Messages that are put up on this Event.
     * @param albums Images or Music promoting this Event.
     * @param timestamp The date and time this Event was created.
     * @throws InputMismatchException Will be thrown, if the times don't fit each other correctly.
     */
    public Event(
            UUID id,
            String title,
            String description,
            LocalDateTime openDoors,
            Plato isVoluntary,
            Plato isPublic,
            Plato isCancelled,
            Plato isSoldOut,
            String location,
            double price,
            String ticketsURL,
            ContactInfo contactInfo,
            Liszt<Gig> gigs,
            Venue venue,
            Liszt<Request> requests,
            Seszt<Participation> participations,
            Liszt<Bulletin> bulletins,
            Liszt<Album> albums,
            LocalDateTime timestamp
    ) throws InputMismatchException {
        super(id, title == null || title.isEmpty() ? "Untitled event" : title, timestamp);

        _description = description;
        _gigs = gigs;

        if (!_gigs.isEmpty())
            try {
                calculateTime();
            } catch (InputMismatchException e) {
                Printer.print("End date is before beginning date of " + _title + "...", e);
            }
        else {
            _openDoors = openDoors;
            _start = openDoors;
        }

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

        set_location(location);

        _requests = requests;
        _participations = participations;
        _bulletins = bulletins;
        _albums = albums;
    }

    /**
     * Adds the given Participation to participations of this Event.
     * @param participation A User that will join this Event.
     * @return All the Participations of this Event.
     */
    public Seszt<Participation> add(Participation participation) {
        return _participations.Add(participation);
    }

    /**
     * Adds the given Gig to gigs of this Event.
     * @param gig A specific Gig of one Performer for a specific time.
     * @return All the Gigs of this Event.
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
                Printer.print("End date is before beginning date of " + _title + "...", e);
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
                        if (
                            stranger.get_primaryId() != performer.get_primaryId()
                            && !gigs[i].get_start().isEqual(gig.get_start())
                            && !gigs[i].get_end().isEqual(gig.get_end())
                        )
                            storage[i] = gigs[i];

        int length = 0;
        for (Gig value : storage)
            if (value != null)
                length++;

        int index = 0;
        Gig[] filtered = new Gig[length];
        for (Gig gig : storage) {
            if (gig != null) {
                filtered[index] = gig;
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
                    removeRequests(performer);

        calculateTime();

        return _gigs;
    }

    /**
     * Checks if the Performer is included in any Gig of this Event.
     * @param performer The Performer that might be included in a Gig.
     * @return True if the Performer is included in any Gig.
     */
    private boolean isPerformerInOtherGigs(Performer performer) {
        for (Gig gig : _gigs)
            for (Performer gigPerformer : gig.get_act())
                if (gigPerformer.get_primaryId() == performer.get_primaryId())
                    return true;

        return false;
    }

    /**
     * Will remove any Requests of this Performer for this Event.
     * @param performer The Performer that should have the Request excluded.
     * @return The Requests of this Event.
     */
    private Liszt<Request> removeRequests(Performer performer) {
        for (int i = 1; i <= _requests.size(); i++) {
            if (_requests.Get(i).get_user().get_primaryId() == performer.get_primaryId()) {
                _requests.Remove(i);
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
        return add(new Liszt<>(requests));
    }

    /**
     * Adds some given Requests to the Liszt of requests from current Event.
     * @param requests Determines some specific requests, that is wished to be added.
     * @return All the requests of the current Event.
     */
    public Liszt<Request> add(Liszt<Request> requests) {
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
                    requests[i] = new Request(user, this);
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
    public Liszt<Request> accept(Request request) {
        if (!_requests.contains(request))
            return null;

        for (Request local : _requests) {
            if (local.get_primaryId().equals(request.get_primaryId())) {
                local.set_approved(new Plato(true));
                return _requests;
            }
        }

        return null;
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
        _requests.add(new Request(venue, this));

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
    public Seszt<Participation> addParticipation(Participation participation) {
        return addParticipation(new Participation[]{participation});
    }

    /**
     * Adds some given Participations to the Liszt of participations from current Event.
     * @param participations Determines some specific participants, that is wished to be added.
     * @return All the Participations of the current Event.
     */
    public Seszt<Participation> addParticipation(Participation[] participations) {
        return _participations.Add(participations);
    }

    /**
     * Removes the given Participation from participations of current Event.
     * @param participation Determines a specific participant, that is wished to be removed.
     * @return All the Participations of the current Event.
     */
    public Seszt<Participation> removeParticipation(Participation participation) {
        _participations.remove(participation);
        return _participations;
    }

    /**
     * Adds the given Bulletin to bulletins of this Event.
     * @param bulletin A specific Bulletin, that is wished to be added.
     * @return All the Bulletins of this Event.
     */
    public Liszt<Bulletin> add(Bulletin bulletin) {
        return _bulletins.Add(bulletin);
    }

    /**
     * Adds the given Album to albums of this Event.
     * @param album A specific Album, that is wished to be added.
     * @return All the Albums of this Event.
     */
    public Liszt<Album> add(Album album) {
        return _albums.Add(album);
    }

    /**
     * Removes the given Bulletin from bulletins of current Event.
     * @param bulletin Determines a specific bulletin, that is wished to be removed.
     * @return All the bulletins of the current Event.
     */
    public Liszt<Bulletin> remove(Bulletin bulletin) {
        _bulletins.remove(bulletin);
        return _bulletins;
    }

    /**
     * Sets a Participation's type, by comparing the participants' ids.
     * @param participation The Participation that is wished to be set.
     * @return If the Participation is set successfully, it will return the Participation, else it will return null.
     */
    public Participation set(Participation participation) {
        for (int i = 1; i <= _participations.size(); i++) {
            if (_participations.Get(i).get_participant().get_primaryId() == participation.get_participant().get_primaryId()) {
                _participations.Get(i).set_type(participation.get_type());
                return _participations.Get(i);
            }
        }

        return null;
    }

    /**
     * Sets a Bulletin, by comparing the two ids.
     * @param bulletin The Bulletin that is wished to be set.
     * @return If the Bulletin is set successfully, it will return the Bulletin, else it will return null.
     */
    public Bulletin set(Bulletin bulletin) {
        for (int i = 1; i <= _bulletins.size(); i++) {
            Bulletin localBulletin = _bulletins.Get(i);

            if (localBulletin.get_primaryId() == bulletin.get_primaryId()) {
                _bulletins.Set(i, bulletin);
                return _bulletins.Get(i);
            }
        }

        return null;
    }

    /**
     * Sets a Request, by comparing the two ids of each Request.
     * @param request The Request that is wished to be set.
     * @return If the Request is set successfully, it will return the Request, else it will return null.
     */
    public Request set(Request request) {
        for (int i = 1; i <= _requests.size(); i++) {
            Request localRequest = _requests.Get(i);

            if (
                localRequest.get_primaryId() == request.get_primaryId()
                && Objects.equals(localRequest.get_secondaryId(), request.get_secondaryId())
            ) {
               return _requests.Set(i, request).Get(i);
            }
        }

        return null;
    }

    /**
     * Sets an Album, by comparing the two ids of each Album.
     * @param album The Album that is wished to be set.
     * @return If the Album is set successfully, it will return the Album, else it will return null.
     */
    public Album set(Album album) {
        for (int i = 1; i <= _albums.size(); i++) {
            Album localAlbum = _albums.Get(i);

            if (localAlbum.get_primaryId() == album.get_primaryId()) {;
                return _albums.Set(i, album).Get(i);
            }
        }

        return null;
    }

    /**
     * Will set the values of a Gig in the Event.
     * @param gig The Gig, that is wished to be set.
     * @return If the Gig is set successfully, it will return the Gig, else it will return null.
     */
    public Gig set(Gig gig) {
        for (int i = 1; i <= _gigs.size(); i++) {
            int sharedActs = 0;

            for (int j = 0; j < _gigs.get(i).get_act().size(); j++)
                for (Performer performer : gig.get_act())
                    if (_gigs.get(i).get_act().get(j).get_primaryId() == performer.get_primaryId())
                        sharedActs++;

            if (sharedActs == _gigs.get(i).get_act().size()) {
                _gigs.get(i).set_start(gig.get_start());
                _gigs.get(i).set_end(gig.get_end());
            }
        }

        return _gigs.Get(gig.toString());
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
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Model.Fields._primaryId,
                Model.Fields._title,
                Fields._description,
                Fields._price,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(_primaryId),
                _title,
                _description,
                String.valueOf(_price),
                String.valueOf(_timestamp)
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    public static class DTO extends ModelDTO {

        /**
         * Is when the participants can enter the event,
         * not necessarily the same time as when the gigs start.
         * It must be before or same time as the gigs start.
         */
        private LocalDateTime openDoors;

        /**
         * This DateTime is determining when the first gig will start.
         * Is being calculated automatically.
         */
        private LocalDateTime start;

        /**
         * This DateTime is determining when the last gig will end.
         * Is being calculated automatically.
         */
        private LocalDateTime end;

        /**
         * The amount of time it will take the gigs in total.
         * Is being calculated automatically.
         */
        private long length;

        /** The description of the Event, that can be edited by Performers or Venue */
        private String description;

        /** This Event is paid or voluntary. */
        private Plato.Argument isVoluntary;

        /** If this is a public Event, other Users can view and interact with it. */
        private Plato.Argument isPublic;

        /**
         * Will be true, if this Event is cancelled.
         * Can only be cancelled by the Venue.
         */
        private Plato.Argument isCancelled;

        /** This is marked if there is no more tickets to sell. */
        private Plato.Argument isSoldOut;

        /**
         * This is the address or place, whether the Event will be held.
         * Will be used to be searched at in Google Maps.
         */
        private String location;

        /**
         * The cost of a ticket.
         * Can be multiple prices for tickets, but this is just meant as the cheapest.
         */
        private double price;

        /**
         * A link for where the tickets can be sold.
         * This might be altered later on,
         * since it is wished to be sold through Bandwich.
         * If the field isn't touched, it will use the Venue's location.
         */
        private String ticketsURL;

        /** Different information of contacting. */
        private ContactInfo.DTO contactInfo;

        /**
         * The gigs with times and acts of the Event.
         */
        private Gig.DTO[] gigs;

        /**
         * This venue is the ones responsible for the Event,
         * perhaps even the place it is held, but not necessarily.
         */
        private Venue.DTO venue;

        /** These requests are needed to make sure, everyone wants to be a part of the Event. */
        private Request.DTO[] requests;

        /**
         * The people that will participate in the Event,
         * not including venues or acts.
         */
        private Participation.DTO[] participations;

        /** Post from different people, that will mention contents. */
        private Bulletin.DTO[] bulletins;

        /** An Album of images, that can be used to promote this Event. */
        private Album.DTO[] albums;

        /**
         * Converts into this DTO Object.
         * @param event The Object to be converted.
         */
        public DTO(Event event) {
            super(event);
            description = event.get_description();

            if (event.get_gigs() != null) {
                gigs = new Gig.DTO[event.get_gigs().size()];
                for (int i = 0; i < gigs.length; i++)
                    gigs[i] = new Gig.DTO(event.get_gigs().Get(i+1));
            }

            openDoors = event.get_openDoors();
            start = event.get_start();
            end = event.get_end();
            length = event.get_length();

            isVoluntary = event.get_voluntary() != null ? event.get_voluntary().get_argument() : null;
            isPublic = event.get_public() != null ? event.get_public().get_argument() : null;
            isCancelled = event.get_cancelled() != null ? event.get_cancelled().get_argument() : null;
            isSoldOut = event.get_soldOut() != null ? event.get_soldOut().get_argument() : null;
            price = event.get_price();
            ticketsURL = event.get_ticketsURL();
            contactInfo = event.get_contactInfo() != null ? new ContactInfo.DTO(event.get_contactInfo()) : null;
            venue = (Venue.DTO) DTOService.convert(event.get_venue());

            location = event.get_location();

            if (event.get_requests() != null) {
                requests = new Request.DTO[event.get_requests().size()];
                for (int i = 0; i < requests.length; i++)
                    requests[i] = new Request.DTO(event.get_requests().Get(i+1));
            }
            if (event.get_participations() != null) {
                participations = new Participation.DTO[event.get_participations().size()];
                for (int i = 0; i < participations.length; i++)
                    participations[i] = new Participation.DTO(event.get_participations().Get(i+1));
            }
            if (event.get_bulletins() != null) {
                bulletins = new Bulletin.DTO[event.get_bulletins().size()];
                for (int i = 0; i < bulletins.length; i++)
                    bulletins[i] = new Bulletin.DTO(event.get_bulletins().Get(i+1));
            }
            if (event.get_albums() != null) {
                albums = new Album.DTO[event.get_albums().size()];
                for (int i = 0; i < albums.length; i++)
                    albums[i] = new Album.DTO(event.get_albums().Get(i+1));
            }
        }
    }
}

