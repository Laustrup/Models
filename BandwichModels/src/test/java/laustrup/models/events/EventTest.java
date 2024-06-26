package laustrup.models.events;

import laustrup.ModelTester;
import laustrup.dtos.events.EventDTO;

import laustrup.models.albums.Album;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.users.sub_users.Performer;
import laustrup.services.RandomCreatorService;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.collections.sets.Seszt;
import laustrup.utilities.parameters.Plato;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.function.Supplier;

class EventTest extends ModelTester<Event, EventDTO> {

    @ParameterizedTest
    @CsvSource(value = {
            "2016-03-04T11:30" + _delimiter + "2016-03-04T16:30" + _delimiter + "false" + _delimiter + "null",
            "2016-03-04T11:30" + _delimiter + "2016-03-04T16:30" + _delimiter + "true" + _delimiter + "location",
            "2016-03-04T11:30" + _delimiter + "2016-03-04T10:30" + _delimiter + "false" + _delimiter + "null",
            "2016-03-04T11:30" + _delimiter + "2016-03-04T10:30" + _delimiter + "true" + _delimiter + "location"
    }, delimiter = _delimiter)
    void correctlyInitiates(LocalDateTime openDoors, LocalDateTime lastGig, boolean noGigs, String location) {
        test(() -> {
            Event arrangement = arrange(() -> {
                long eventId = _random.nextLong();
                Liszt<Gig> gigs = !noGigs
                        ? _items.generateGigs(new Event(eventId),lastGig, _random.nextInt(10), _random.nextInt(30))
                        : new Liszt<>();
                Seszt<Performer> performers = new Seszt<>();
                if (!noGigs)
                    for (Gig gig : gigs)
                        performers.addAll(gig.get_act());
                Liszt<Request> requests = new Liszt<>();
                if (!noGigs)
                    for (Performer performer : performers)
                        requests.add(new Request(performer, new Event(eventId), new Plato(), "", LocalDateTime.now()));

                try {
                    return new Event(
                        eventId,RandomCreatorService.get_instance().generateString(),
                        RandomCreatorService.get_instance().generateString(), openDoors,
                        _items.generatePlato(),_items.generatePlato(),_items.generatePlato(),_items.generatePlato(),
                        location.equals("null") ? null : location,_random.nextDouble(1000),
                        RandomCreatorService.get_instance().generateString(),
                        _items.get_contactInfo()[_random.nextInt(_items.get_contactInfoAmount())], gigs,
                        _items.get_venues()[_random.nextInt(_items.get_venueAmount())], requests,
                        _items.generateParticipations(new Event(eventId)),
                        new Liszt<>(_items.generateBulletins(new Event(eventId))), new Liszt<>(),LocalDateTime.now()
                    );
                } catch (InputMismatchException e) {
                    asserting(lastGig.isBefore(openDoors) && !gigs.isEmpty());
                    return null;
                }
            });

            if (arrangement != null) {
                assureTimes(arrangement);
                asserting(location.equals("null")
                        ? arrangement.get_location().equals(arrangement.get_venue().get_location())
                        : arrangement.get_location().equals(location)
                );
            }
        });
    }

    @Test
    void canCalculateEventLength() {
        test(() -> assureTimes(arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())])));
    }

    /**
     * Will check if the Gigs times fits the calculated times of the Event.
     * @param event The Event that should have its times checked.
     */
    private void assureTimes(Event event) {
        LocalDateTime firstGig = null, latestGig = null;
        if (!event.get_gigs().isEmpty())
            for (Gig gig : event.get_gigs()) {
                if (firstGig == null || gig.get_start().isBefore(firstGig))
                    firstGig = gig.get_start();
                if (latestGig == null || gig.get_end().isAfter(latestGig))
                    latestGig = gig.get_end();
            }
        else
            firstGig = event.get_openDoors();

        asserting(
            (event.get_openDoors().isBefore(event.get_start()) || event.get_openDoors().equals(event.get_start())) &&
                    (latestGig == null || event.get_openDoors().isBefore(event.get_end()) || event.get_gigs().isEmpty()) &&
                    firstGig.isEqual(event.get_start()) && (latestGig == null || latestGig.isEqual(event.get_end()))
        );
    }

    @Override @Test //TODO Event stackoverflow from items, temporarily fixed.
    protected void dataTransportObjectTranslate() {
        test(() -> {
            Event expected = arrange(() -> {
                Event event = fixStackOverflow(_items.get_events()[_random.nextInt(_items.get_eventAmount())]);
                _dto = new EventDTO(event);
                return event;
            });

            Event actual = act(() -> new Event(_dto));

            asserting(expected.toString(),actual.toString());

            addToPrint("The two Events are:\n \n" + expected + "\n" + actual);
        });
    }

    private Event fixStackOverflow(Event event) {
        return fixBulletinStackOverflow(fixParticipationStackOverflow(fixRequestStackOverflow(event)));
    }

    private Event fixRequestStackOverflow(Event event) {
        Liszt<Request> requests = new Liszt<>();
        for (Request request : event.get_requests()) {
            requests.add(new Request(
                    request.get_user(),
                    new Event(request.get_event().get_primaryId()),
                    request.get_approved(),
                    request.get_message(),
                    request.get_timestamp())
            );
        }

        return new Event(event.get_primaryId(),event.get_title(),event.get_description(),event.get_openDoors(),event.get_voluntary(),event.get_public(),event.get_cancelled(),event.get_soldOut(),event.get_location(),event.get_price(),event.get_ticketsURL(),event.get_contactInfo(),event.get_gigs(),event.get_venue(),requests,event.get_participations(),event.get_bulletins(),event.get_albums(),event.get_timestamp());
    }

    private Event fixParticipationStackOverflow(Event event) {
        Liszt<Participation> participations = new Liszt<>();
        for (Participation participation : event.get_participations())
            participations.add(new Participation(
                    participation.get_participant(),
                    new Event(participation.get_event().get_primaryId()),
                    participation.get_type(),
                    participation.get_timestamp()
            ));

        return new Event(event.get_primaryId(),event.get_title(),event.get_description(),event.get_openDoors(),event.get_voluntary(),event.get_public(),event.get_cancelled(),event.get_soldOut(),event.get_location(),event.get_price(),event.get_ticketsURL(),event.get_contactInfo(),event.get_gigs(),event.get_venue(),event.get_requests(),participations,event.get_bulletins(),event.get_albums(),event.get_timestamp());
    }

    private Event fixBulletinStackOverflow(Event event) {
        Liszt<Bulletin> bulletins = new Liszt<>();
        for (Bulletin bulletin : event.get_bulletins())
            bulletins.add(new Bulletin(
                    bulletin.get_primaryId(),
                    bulletin.get_author(),
                    new Event(bulletin.get_receiver().get_primaryId()),
                    bulletin.get_content(),
                    bulletin.is_sent(),
                    bulletin.get_edited(),
                    bulletin.is_public(),
                    bulletin.get_timestamp()
            ));

        return new Event(event.get_primaryId(),event.get_title(),event.get_description(),event.get_openDoors(),event.get_voluntary(),event.get_public(),event.get_cancelled(),event.get_soldOut(),event.get_location(),event.get_price(),event.get_ticketsURL(),event.get_contactInfo(),event.get_gigs(),event.get_venue(),event.get_requests(),event.get_participations(),bulletins,event.get_albums(),event.get_timestamp());
    }

    @Override @Test
    protected void toStringTest() {
        Event event = _items.get_events()[_random.nextInt(_items.get_eventAmount())];

        testToString(event,new String[]{
            "id",
            "title",
            "description",
            "price",
            "timestamp"
        },new String[]{
            String.valueOf(event.get_primaryId()),
            event.get_title(),
            event.get_description(),
            String.valueOf(event.get_price()),
            String.valueOf(event.get_timestamp())
        });
    }

    @Override @Test
    protected void canAdd() {
        canAddGigs();
        canAddRequests();
        canAddParticipation();
        canAddBulletin();
        canAddAlbums();
    }

    private void canAddGigs() {
        test(() -> {
            Event event = arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            int previousSize = event.get_gigs().size();
            Gig[] gigs = new Gig[]{
                new Gig(
                    event,
                    new Performer[]{
                            _items.get_bands()[_random.nextInt(_items.get_bandAmount())],
                            _items.get_artists()[_random.nextInt(_items.get_artistAmount())]
                    },
                    LocalDateTime.now(), LocalDateTime.now().plusHours(1)
                ),
                new Gig(
                    event,
                    new Performer[]{
                            _items.get_bands()[_random.nextInt(_items.get_bandAmount())],
                            _items.get_artists()[_random.nextInt(_items.get_artistAmount())]
                    },
                    LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2)
                )
            };

            act(() -> event.add(gigs));

            asserting(
                event.get_gigs().size() == previousSize + gigs.length &&
                        event.get_gigs().contains(gigs)
            );
        });
    }

    private void canAddRequests() {
        test(() -> {
            Event event = arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            int previousSize = event.get_requests().size();
            Request[] requests = _items.generateRequests(
                    new Liszt<>(new Performer[]{
                        _items.get_bands()[_random.nextInt(_items.get_bandAmount())],
                                _items.get_artists()[_random.nextInt(_items.get_artistAmount())]
                    }),
                    event
            );

            act(() -> event.add(requests));

            asserting(
                event.get_requests().size() == previousSize + requests.length &&
                        event.get_requests().contains(requests)
            );
        });
    }

    private void canAddParticipation() {
        test(() -> {
            Event event = arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            int previousSize = event.get_participations().size();
            Participation participation = _items.generateParticipations(event).getFirst();

            act(() -> event.add(participation));

            asserting(
                event.get_participations().size() == previousSize + 1 &&
                        event.get_participations().contains(participation)
            );
        });
    }

    private void canAddBulletin() {
        test(() -> {
            Event event = arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            int previousSize = event.get_bulletins().size();
            Bulletin bulletin = _items.generateBulletins(event)[0];

            act(() -> event.add(bulletin));

            asserting(
                    event.get_bulletins().size() == previousSize + 1 &&
                            event.get_bulletins().contains(bulletin)
            );
        });
    }

    private void canAddAlbums() {
        test(() -> {
            Event event = arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            int previousSize = event.get_albums().size();
            Album album = new Album(
                    RandomCreatorService.get_instance().generateString(),
                    _items.generateAlbumItems(),
                    event.get_venue()
            );

            act(() -> event.add(album));

            asserting(
                event.get_albums().size() == previousSize + 1 &&
                        event.get_albums().contains(album)
            );
        });
    }

    @Override @Test
    protected void canSet() {
        canSetGigs();
        canSetRequest();
        canSetParticipation();
        canSetBulletin();
        canSetAlbum();
    }

    private void canSetGigs() {
        test(() -> {
            Event arrangement = arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            int index = _random.nextInt(arrangement.get_gigs().size());
            Gig updated = arrangement.get_gigs().get(index);
            updated.set_title("This gig has been changed!");

            act(() -> arrangement.set(updated));

            asserting(arrangement.get_gigs().get(index).get_title(), updated.get_title());
        });
    }

    private void canSetRequest() {
        test(() -> {
            Event arrangement = arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            int index = _random.nextInt(arrangement.get_requests().size());
            Request request = arrangement.get_requests().get(index);
            request.set_title("This request has been changed!");

            act(() -> arrangement.set(request));

            asserting(arrangement.get_requests().get(index).get_title(), request.get_title());
        });
    }

    private void canSetParticipation() {
        test(() -> {
            Event arrangement = arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            int index = _random.nextInt(arrangement.get_participations().size());
            Participation participation = arrangement.get_participations().get(index);
            participation.set_type(new Supplier<Participation.ParticipationType>() {
                @Override
                public Participation.ParticipationType get() {
                    Participation.ParticipationType type = Participation.ParticipationType.CANCELED;
                    if (type == participation.get_type())
                        type = Participation.ParticipationType.ACCEPTED;

                    return type;
                }
            }.get());

            act(() -> arrangement.set(participation));

            asserting(arrangement.get_participations().get(index).get_type(), participation.get_type());
        });
    }

    private void canSetBulletin() {
        test(() -> {
            Event arrangement = arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            int bulletinIndex = _random.nextInt(arrangement.get_bulletins().size());
            Bulletin bulletin = arrangement.get_bulletins().get(bulletinIndex);
            bulletin.set_title("This bulletin has been changed!");

            act(() -> arrangement.set(bulletin));

            asserting(arrangement.get_bulletins().get(bulletinIndex).get_title(),bulletin.get_title());
        });
    }

    private void canSetAlbum() {
        test(() -> {
            Event arrangement = arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            int index = _random.nextInt(arrangement.get_albums().size());
            Album album = arrangement.get_albums().get(index);
            album.set_title("This album has been changed!");

            act(() -> arrangement.set(album));

            asserting(arrangement.get_albums().get(index).get_title(),album.get_title());
        });
    }

    @Override @Test
    protected void canRemove() {

    }
}