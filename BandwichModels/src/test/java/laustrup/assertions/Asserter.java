package laustrup.assertions;

import laustrup.models.*;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Post;
import laustrup.models.chats.messages.Mail;
import laustrup.models.users.*;
import laustrup.quality_assurance.Actor;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.collections.sets.Seszt;
import laustrup.utilities.console.Printer;
import laustrup.utilities.parameters.Plato;

import javax.lang.model.element.UnknownElementException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Will do assertions, mostly specified for Bandwich objects
 * @param <T> The return type.
 */
public abstract class Asserter<T> extends Actor<T> {

    /**
     * Generates a message for cases, where an element such or should not be null.
     * @param object The Object that should either be null or not.
     * @return The generated message.
     */
    public static String isNullMessage(Object object) {
        if (object == null)
            return "An object is null, where it is not supposed to be...";
        return object.toString() + " isn't null...";
    }

    /**
     * Will generate a message of the assertion fail of an unknown switch case occurring at default case.
     * @param element The element that results in the fail.
     * @return The generated message.
     */
    public static String switchElementUnknown(Object element) {
        return element.toString() + " type unknown of " + element.getClass();
    }

    /**
     * Asserts two Users to check they are the same.
     * @param expected The User that is arranged and defined.
     * @param actual The User that is the result of an action.
     */
    public static void asserting(User expected, User actual) {
        Printer.print("Expected = " + expected + "\n\nActual = " + actual);

        if (expected.getClass().equals(Participant.class)) {
            assertParticipants((Participant) expected, (Participant) actual);
        } else if (expected.getClass().equals(Band.class)) {
            assertBand((Band) expected, (Band) actual);
        } else if (expected.getClass().equals(Artist.class)) {
            assertArtists((Artist) expected, (Artist) actual);
        } else if (expected.getClass().equals(Venue.class)) {
            assertion((Venue) expected, (Venue) actual);
        } else {
            AssertionFailer.failing(switchElementUnknown(expected), new UnknownElementException(null, expected));
        }
    }

    /**
     * Asserts two Participants to check they are the same.
     * @param expected The Participant that is arranged and defined.
     * @param actual The Participant that is the result of an action.
     */
    public static void assertParticipants(Participant expected, Participant actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected,actual);

            assertUsers(expected, actual);
            if (AssertionChecker.allowLiszt(expected.get_idols(),actual.get_idols()))
                AssertionActor.assertFor(1,expected.get_idols().size(), i -> {
                    assertEquals(expected.get_idols().Get(i).toString(),actual.get_idols().Get(i).toString());
                    return null;
            });

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Bands to check they are the same.
     * @param expected The Band that is arranged and defined.
     * @param actual The Band that is the result of an action.
     */
    public static void assertBand(Band expected, Band actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected,actual);

            assertPerformers(expected, actual);
            if (AssertionChecker.allowLiszt(expected.get_members(),actual.get_members()))
                AssertionActor.assertFor(1,expected.get_members().size(), i -> {
                    assertEquals(expected.get_members().Get(i).toString(),actual.get_members().Get(i).toString());
                    return null;
            });
            assertEquals(expected.get_runner(),actual.get_runner());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Artists to check they are the same.
     * @param expected The Artist that is arranged and defined.
     * @param actual The Artist that is the result of an action.
     */
    public static void assertArtists(Artist expected, Artist actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected,actual);

            assertPerformers(expected,actual);
            if (AssertionChecker.allowLiszt(expected.get_bands(),actual.get_bands())) {
                AssertionActor.assertFor(1,expected.get_bands().size(), i -> {
                    assertEquals(expected.get_bands().Get(i).toString(),actual.get_bands().Get(i).toString());
                    return null;
                });
            }

            assertEquals(expected.get_runner(),actual.get_runner());
            assertRequests(expected.get_requests(),actual.get_requests());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Performers to check they are the same.
     * @param expected The Performer that is arranged and defined.
     * @param actual The Performer that is the result of an action.
     */
    private static void assertPerformers(Performer expected, Performer actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected,actual);

            assertUsers(expected, actual);
            if (AssertionChecker.allowLiszt(expected.get_fans(),actual.get_fans())) {
                AssertionActor.assertFor(1,expected.get_fans().size(), i -> {
                    assertEquals(expected.get_fans().Get(i).toString(),actual.get_fans().Get(i).toString());
                    return null;
                });
            }
            if (AssertionChecker.allowLiszt(expected.get_idols(),actual.get_idols())) {
                AssertionActor.assertFor(1,expected.get_idols().size(), i -> {
                    assertEquals(expected.get_idols().Get(i).toString(),actual.get_idols().Get(i).toString());
                    return null;
                });
            }

            return AssertionMessage.SUCCESS.get_content();
        });

    }

    /**
     * Asserts two Venues to check they are the same.
     * @param expected The Venue that is arranged and defined.
     * @param actual The Venue that is the result of an action.
     */
    public static void assertion(Venue expected, Venue actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected,actual);

            assertUsers(expected, actual);
            assertEquals(expected.get_location(),actual.get_location());
            assertEquals(expected.get_stageSetup(),actual.get_stageSetup());
            assertEquals(expected.get_size(),actual.get_size());
            assertRequests(expected.get_requests(),actual.get_requests());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Users to check they are the same.
     * @param expected The User that is arranged and defined.
     * @param actual The User that is the result of an action.
     */
    private static void assertUsers(User expected, User actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected,actual);

            assertEquals(expected.get_primaryId(),actual.get_primaryId());
            assertEquals(expected.get_username(),actual.get_username());
            assertEquals(expected.get_firstName(),actual.get_firstName());
            assertEquals(expected.get_lastName(),actual.get_lastName());
            assertEquals(expected.get_description(),actual.get_description());
            asserting(expected.get_contactInfo(),actual.get_contactInfo());
            assertAlbums(expected.get_albums(),actual.get_albums());
            assertRatings(expected.get_ratings(),actual.get_ratings());
            assertEvents(expected.get_events(),actual.get_events());
            assertChatRooms(expected.get_chatRooms(),actual.get_chatRooms());
            asserting(expected.get_subscription(),actual.get_subscription());
            assertBulletins(expected.get_posts(),actual.get_posts());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Albums to check they are the same.
     * @param expectations The Albums that are arranged and defined.
     * @param actuals The Albums that are the result of an action.
     */
    public static void assertAlbums(Liszt<Album> expectations, Liszt<Album> actuals) {
        assertAlbums(new Seszt<>(expectations.get_data()), new Seszt<>(actuals.get_data()));
    }

    /**
     * Asserts Albums to check they are the same.
     * @param expectations The Albums that are arranged and defined.
     * @param actuals The Albums that are the result of an action.
     */
    public static void assertAlbums(Seszt<Album> expectations, Seszt<Album> actuals) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowLiszt(expectations, actuals))
                return AssertionChecker.lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                Album expected = expectations.Get(i),
                        actual = actuals.Get(i);

                asserting(expected.get_items().get_data(), actual.get_items().get_data());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Album items to check they are the same.
     * @param expectations The Items that are arranged and defined.
     * @param actuals The Items that are the result of an action.
     */
    public static void asserting(Album.Item[] expectations, Album.Item[] actuals) {
        if (expectations.length > 0 && actuals.length > 0) {
            Seszt<Album.Item>
                    expectedSeszt = new Seszt<>(expectations),
                    actualSeszt = new Seszt<>(actuals);
            for (int j = 1; j <= expectations.length; j++) {
                assertEquals(expectedSeszt.Get(j).get_endpoint(), actualSeszt.Get(j).get_endpoint());
                AssertionActor.assertIf(
                        expectedSeszt.Get(j).get_event() != null,
                        expectedSeszt.Get(j).get_event() != null
                                ? expectedSeszt.Get(j).get_event().get_primaryId()
                                : 0,
                        actualSeszt.Get(j).get_primaryId());
                assertEquals(
                        expectedSeszt.Get(j).get_kind(),
                        actualSeszt.Get(j).get_kind()
                );
            }
        }
    }

    /**
     * Asserts Ratings to check they are the same.
     * @param expectations The Ratings that are arranged and defined.
     * @param actuals The Ratings that are the result of an action.
     */
    public static void assertRatings(Liszt<Rating> expectations, Liszt<Rating> actuals) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowLiszt(expectations,actuals))
                return AssertionChecker.lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                Rating expected = expectations.Get(i),
                        actual = actuals.Get(i);

                assertEquals(expected.get_primaryId(), actual.get_primaryId());
                assertEquals(expected.get_secondaryId(), actual.get_secondaryId());
                assertEquals(expected.get_value(),actual.get_value());
                asserting((expected.get_value() <= 5 && expected.get_value() > 0)
                        && (actual.get_value() <= 5 && actual.get_value() > 0));
                assertEquals(expected.get_comment(),actual.get_comment());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts ChatRooms to check they are the same.
     * @param expectations The ChatRooms that are arranged and defined.
     * @param actuals The ChatRooms that are the result of an action.
     */
    public static void assertChatRooms(Seszt<ChatRoom> expectations, Seszt<ChatRoom> actuals) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowLiszt(expectations,actuals))
                return AssertionChecker.lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                ChatRoom expected = expectations.Get(i),
                        actual = actuals.Get(i);
                assertMails(expected.get_mails(),actual.get_mails());
                AssertionActor.assertFor(1,expected.get_chatters().size(), index -> {
                    assertEquals(expected.get_chatters().Get(index).toString(),actual.get_chatters().Get(index).toString());
                    return null;
                });
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Mails to check they are the same.
     * @param expectations The Mails that are arranged and defined.
     * @param actuals The Mails that are the result of an action.
     */
    public static void assertMails(Liszt<Mail> expectations, Liszt<Mail> actuals) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowLiszt(expectations,actuals))
                return AssertionChecker.lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                Mail expected = expectations.Get(i), actual = actuals.Get(i);

                assertEquals(expected.get_primaryId(),actual.get_primaryId());
                assertEquals(expected.get_chatRoom().toString(),actual.get_chatRoom().toString());
                assertEquals(expected.get_author().toString(),actual.get_author().toString());
                assertEquals(expected.get_content(),actual.get_content());
                assertEquals(expected.get_sent(), actual.get_sent());
                assertEquals(expected.get_edited().toString(),actual.get_edited().toString());
                assertEquals(expected.is_public(),actual.is_public());
                assertEquals(expected.get_timestamp(),actual.get_timestamp());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Subscriptions to check they are the same.
     * @param expected The Subscription that is arranged and defined.
     * @param actual The Subscription that is the result of an action.
     */
    public static void asserting(User.Subscription expected, User.Subscription actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected,actual);

            assertEquals(expected.get_status(),actual.get_status());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Requests to check they are the same.
     * @param expectations The Requests that are arranged and defined.
     * @param actuals The Requests that are the result of an action.
     */
    public static void assertRequests(Liszt<Request> expectations, Liszt<Request> actuals) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowLiszt(expectations,actuals))
                return AssertionChecker.lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                Request expected = expectations.Get(i),
                        actual = actuals.Get(i);
                assertUsers(expected.get_user(), actual.get_user());
                asserting(expected.get_event(), actual.get_event());
                assertEquals(expected.get_approved(), actual.get_approved());
                assertEquals(expected.get_message(), actual.get_message());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two ContactInfos to check they are the same.
     * @param expected The information that is arranged and defined.
     * @param actual The information that is the result of an action.
     */
    public static void asserting(ContactInfo expected, ContactInfo actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected,actual);

            assertEquals(expected.get_email(), actual.get_email());
            assertEquals(expected.get_country().get_title(), actual.get_country().get_title());
            assertingPhones(expected.get_phones(), actual.get_phones());
            assertEquals(expected.getAddressInfo(), actual.getAddressInfo());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Ensures that the sizes of each sets are the same and then loops through them with the asserting method.
     * @param expectations The Phones that are expected.
     * @param actuals The Phones that are the result of the test.
     */
    private static void assertingPhones(Seszt<ContactInfo.Phone> expectations, Seszt<ContactInfo.Phone> actuals) {
        assertTrue(expectations.size() == actuals.size());
        for (int i = 1; i <= expectations.size(); i++)
            asserting(expectations.Get(i), actuals.Get(i));
    }

    /**
     * Will assert the attributes in each input and use the country method.
     * @param expected The phone that is expected.
     * @param actual The phone that is a result of the test.
     */
    private static void asserting(ContactInfo.Phone expected, ContactInfo.Phone actual) {
        assertEquals(expected.get_numbers(), actual.get_numbers());
        assertEquals(expected.is_mobile(), actual.is_mobile());
        asserting(expected.get_country(), actual.get_country());
    }

    /**
     * Simply compares the attributes of the countries.
     * @param expected The country that is expected.
     * @param actual The country that is a result of the test.
     */
    private static void asserting(ContactInfo.Country expected, ContactInfo.Country actual) {
        assertEquals(expected.get_title(), actual.get_title());
        assertEquals(expected.get_firstPhoneNumberDigits(), actual.get_firstPhoneNumberDigits());
    }

    /**
     * Asserts Bulletins to check they are the same.
     * @param expectations The Bulletins that are arranged and defined.
     * @param actuals The Bulletins that are the result of an action.
     */
    public static void assertBulletins(Liszt<Post> expectations, Liszt<Post> actuals) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowLiszt(expectations,actuals))
                return AssertionChecker.lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                Post expected = expectations.Get(i), actual = actuals.Get(i);

                assertEquals(expected.get_primaryId(),actual.get_primaryId());
                assertUsers(expected.get_author(), actual.get_author());
                AssertionActor.assertIf(AssertionChecker.allowObjects(expected.get_author(),actual.get_author()),expected.get_receiver(),actual.get_receiver());
                AssertionActor.assertIf(AssertionChecker.allowObjects(expected.get_receiver(),actual.get_receiver()),expected.get_receiver(),actual.get_receiver());
                assertEquals(expected.get_content(),actual.get_content());
                assertEquals(expected.get_sent(), actual.get_sent());
                asserting(expected.get_edited(), actual.get_edited());
                assertEquals(expected.is_public(), actual.is_public());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Events to check they are the same.
     * @param expectations The Events that are arranged and defined.
     * @param actuals The Events that are the result of an action.
     */
    public static void assertEvents(Seszt<Event> expectations, Seszt<Event> actuals) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowLiszt(expectations,actuals))
                return AssertionChecker.lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++)
                asserting(expectations.Get(i),actuals.Get(i));

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Events to check they are the same.
     * @param expected The Event that is arranged and defined.
     * @param actual The Event that is the result of an action.
     */
    public static void asserting(Event expected, Event actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected, actual);

            AssertionActor.assertIf(
                AssertionChecker.allowObjects(expected.get_openDoors(),actual.get_openDoors()),
                expected.get_openDoors(),actual.get_openDoors()
            );
            AssertionActor.assertIf(
                AssertionChecker.allowObjects(expected.get_start(),actual.get_start()),
                    expected.get_start(),actual.get_start()
            );
            AssertionActor.assertIf(
                AssertionChecker.allowObjects(expected.get_end(),actual.get_end()),
                expected.get_end(),actual.get_end()
            );
            AssertionActor.assertIf(
                AssertionChecker.allowObjects(expected.get_contactInfo(),actual.get_contactInfo()),
                expected.get_contactInfo(),actual.get_contactInfo()
            );
            AssertionActor.assertIf(
                expected.get_cancelled()!=null&&actual.get_cancelled()!=null,
                () -> asserting(expected.get_cancelled(), actual.get_cancelled())
            );
            AssertionActor.assertIf(
                expected.get_soldOut()!=null&&actual.get_soldOut()!=null,
                () -> asserting(expected.get_soldOut(), actual.get_soldOut())
            );
            AssertionActor.assertIf(
                expected.get_voluntary()!=null&&actual.get_voluntary()!=null,
                () -> asserting(expected.get_voluntary(), actual.get_voluntary())
            );
            AssertionActor.assertIf(
                expected.get_public()!=null&&actual.get_public()!=null,
                () -> asserting(expected.get_public(), actual.get_public())
            );

            assertEquals(expected.get_duration(),expected.get_duration());
            assertEquals(expected.get_location(), actual.get_location());

            AssertionActor.assertIf(
                expected.get_gigs()!=null && actual.get_gigs()!=null,
                () -> assertGigs(expected.get_gigs(), actual.get_gigs())
            );
            AssertionActor.assertIf(
                expected.get_venue()!=null && actual.get_venue()!=null,
                () -> assertion(expected.get_venue(),actual.get_venue())
            );
            AssertionActor.assertIf(
                expected.get_requests() != null && actual.get_requests() != null,
                () -> assertRequests(expected.get_requests(), actual.get_requests())
            );
            AssertionActor.assertIf(
                expected.get_participations() != null && actual.get_participations() != null,
                () -> assertParticipations(expected.get_participations(), actual.get_participations())
            );
            AssertionActor.assertIf(
                expected.get_albums()!=null&&actual.get_albums()!=null,
                () -> assertAlbums(expected.get_albums(), actual.get_albums())
            );

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Participations to check they are the same.
     * @param expectations The Participations that are arranged and defined.
     * @param actuals The Participations that are the result of an action.
     */
    public static void assertParticipations(Seszt<Event.Participation> expectations, Seszt<Event.Participation> actuals) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowLiszt(expectations,actuals))
                return AssertionChecker.lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++)
                asserting(expectations.Get(i),actuals.Get(i));

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Participations to check they are the same.
     * @param expected The Participation that is arranged and defined.
     * @param actual The Participation that is the result of an action.
     */
    public static void asserting(Event.Participation expected, Event.Participation actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected, actual))
                return AssertionChecker.objectMessage(expected, actual);

            assertEquals(expected.toString(), actual.toString());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Gigs to check they are the same.
     * @param expectations The Gigs that are arranged and defined.
     * @param actuals The Gigs that are the result of an action.
     */
    public static void assertGigs(Liszt<Event.Gig> expectations, Liszt<Event.Gig> actuals) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowLiszt(expectations,actuals))
                return AssertionChecker.lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++)
                asserting(expectations.Get(i),actuals.Get(i));

            return AssertionMessage.SUCCESS.get_content();
        });
    }



    /**
     * Asserts two Gigs to check they are the same.
     * @param expected The Gig that is arranged and defined.
     * @param actual The Gig that is the result of an action.
     */
    public static void asserting(Event.Gig expected, Event.Gig actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected,actual);

            asserting(expected.get_event(), actual.get_event());
            asserting(expected.get_act().get_data(), actual.get_act().get_data());
            assertEquals(expected.get_start(), actual.get_start());
            assertEquals(expected.get_end(), actual.get_end());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts participation type to check they are the same.
     * @param expectations The participation type that are arranged and defined.
     * @param actuals The participation type that are the result of an action.
     */
    public void asserting(Event.Participation.Type expectations, Event.Participation.Type actuals) {
        AssertionActor.doAssert(() -> {
            assertEquals(expectations, actuals);
            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two acts to check they are the same.
     * @param expectations The acts that is arranged and defined.
     * @param actuals The acts that is the result of an action.
     */
    public static void asserting(Performer[] expectations, Performer[] actuals) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowCollection(expectations,actuals))
                return AssertionChecker.collectionMessage(expectations,actuals);

            for (int i = 0; i < expectations.length; i++)
                assertPerformers(expectations[i], actuals[i]);

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Platos to check they are the same.
     * @param expected The act that is arranged and defined.
     * @param actual The act that is the result of an action.
     */
    public static void asserting(Plato expected, Plato actual) {
        asserting(expected.toString(), actual.toString());
    }

    /**
     * Asserts two local dates to see if they are the same.
     * @param expected The LocalDate that is arranged and defined.
     * @param actual The LocalDate that is the result of an action.
     */
    public static void asserting(LocalDate expected, LocalDate actual) {
        AssertionActor.doAssert(() -> {
            assertEquals(expected, actual);
            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two local date times to see if they are the same.
     * @param expected The LocalDateTime that is arranged and defined.
     * @param actual The LocalDateTime that is the result of an action.
     */
    public static void asserting(LocalDateTime expected, LocalDateTime actual) {
        AssertionActor.doAssert(() -> {
            assertEquals(expected, actual);
            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two instants to see if they are the same.
     * @param expected The instant that is arranged and defined.
     * @param actual The instant that is the result of an action.
     */
    public static void asserting(Instant expected, Instant actual) {
        AssertionActor.doAssert(() -> {
            assertEquals(expected, actual);
            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two ZonedDateTimes to see if they are the same.
     * @param expected The ZonedDateTime that is arranged and defined.
     * @param actual The ZonedDateTime that is the result of an action.
     */
    public static void asserting(ZonedDateTime expected, ZonedDateTime actual) {
        AssertionActor.doAssert(() -> {
            assertEquals(expected, actual);
            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Is used to determine whether a statement is true.
     * Uses the assertTrue method.
     * @param isTrue A statement or value that must be true.
     */
    public static void asserting(boolean isTrue) {
        AssertionActor.doAssert(() -> {
            assertTrue(isTrue);
            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * A simple comparing assertEqual of two Strings.
     * @param expected The String that is arranged and defined.
     * @param actual The String that is the result of an action
     */
    public static void asserting(String expected, String actual) {
        AssertionActor.doAssert(() -> {
            if (!AssertionChecker.allowObjects(expected,actual))
                return AssertionChecker.objectMessage(expected,actual);

            assertEquals(expected, actual);
            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Will assert the condition of whether this is null or not.
     * @param object The Object that will be asserted.
     */
    public static void notNull(Object object) {
        AssertionActor.doAssert(() -> {
            assertNotNull(object);
            return AssertionMessage.SUCCESS.get_content();
        });
    }
}
