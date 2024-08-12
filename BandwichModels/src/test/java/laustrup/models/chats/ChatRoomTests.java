package laustrup.models.chats;

import laustrup.ModelTester;
import laustrup.models.chats.messages.Mail;
import laustrup.models.User;
import laustrup.models.users.Artist;
import laustrup.models.users.Band;
import laustrup.services.RandomCreatorService;
import laustrup.services.TimeService;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.collections.sets.Seszt;
import laustrup.utilities.parameters.Plato;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

class ChatRoomTests extends ModelTester<ChatRoom, ChatRoom.DTO> {
    @Override @Test
    protected void dataTransportObjectTranslate() {
        test(() -> {
            ChatRoom expected = arrange(() -> {
                ChatRoom chatRoom = _items.get_chatRooms().getFirst();

                _dto = new ChatRoom.DTO(chatRoom);
                return chatRoom;
            });

            ChatRoom actual = act(() -> new ChatRoom(_dto));

            asserting(expected.toString(),actual.toString());

            addToPrint("The two ChatRooms are:\n \n" + expected + "\n" + actual);
        });
    }

    @Override @Test
    protected void toStringTest() {
        ChatRoom arrangement = _items.get_chatRooms().getFirst();

        testToString(arrangement,new String[]{
                "id",
                "title",
                "timestamp"
        },new String[]{
                String.valueOf(arrangement.get_primaryId()),
                arrangement.get_title(),
                String.valueOf(arrangement.get_timestamp())
        });
    }

    @Test
    void canDetermineChatRoomOfEmptyTitle() {
        emptyTitleTest(true);
        emptyTitleTest(false);
    }

    /**
     * Will test the title determining algorithm for a ChatRoom without a title.
     * @param isNull Decides whether the title should be tested to be null or empty.
     */
    private void emptyTitleTest(boolean isNull) {
        test(() -> {
            ChatRoom chatRoom = arrange(() -> new ChatRoom(
                    UUID.randomUUID(),
                    isNull ? null : "",
                    new Liszt<>(),
                    _items.get_chatRooms().get(_random.nextInt(_items.get_chatRooms().size())).get_chatters(),
                    TimeService.generateRandom()
                )
            );

            String expected = simulateEmptyTitle(chatRoom);

            asserting(expected, chatRoom.get_title());

            User newChatter = _items.generateUser();
            while (chatRoom.chatterExists(newChatter))
                newChatter = _items.generateUser();

            chatRoom.add(newChatter);

            expected = simulateEmptyTitle(chatRoom);

            asserting(expected, chatRoom.get_title());
        });
    }

    /**
     * Will try to represent how a title of ChatRoom without a title should be like.
     * @param chatRoom The ChatRoom with a empty title.
     * @return The represented String.
     */
    private String simulateEmptyTitle(ChatRoom chatRoom) {
        StringBuilder title = new StringBuilder();

        for (int i = 1; i <= chatRoom.get_chatters().size(); i++) {
            title.append(chatRoom.get_chatters().Get(i).get_username());
            if (i < chatRoom.get_chatters().size())
                title.append(", ");
        }

        return title.toString();
    }

    @Override @Test
    protected void canAdd() {
        ChatRoom arrangement = _items.get_chatRooms().getFirst();

        canAddMails(arrangement);
        canNotAddMails(arrangement);
        canAddChatters(arrangement);
    }

    /**
     * Will test if a ChatRoom can add Mails.
     * The author must be a chatter of the ChatRoom.
     * Also checks if the ChatRoom is answered.
     * @param chatRoom The arranged ChatRoom to test add.
     */
    private void canAddMails(ChatRoom chatRoom) {
        Mail[] mails = generateMails(chatRoom,chatRoom.get_chatters().get(_random.nextInt(chatRoom.get_chatters().size())));

        test(() -> {
            arrange(() -> chatRoom);

            int previousSize = chatRoom.get_mails().size();

            act(() -> chatRoom.add(mails));

            asserting(chatRoom.get_mails().size() == previousSize + mails.length);
        });
    }

    /**
     * The author can not be from outside the ChatRoom.
     * @param chatRoom The arranged ChatRoom to test add.
     */
    private void canNotAddMails(ChatRoom chatRoom) {
        User foreignAuthor = _items.generateUser();
        while (chatRoom.get_chatters().contains(foreignAuthor))
            foreignAuthor = _items.generateUser();
        Mail[] mails = generateMails(chatRoom,foreignAuthor);

        test(() -> {
            arrange(() -> chatRoom);

            int previousSize = chatRoom.get_mails().size();

            act(() -> chatRoom.add(mails));

            asserting(chatRoom.get_mails().size() == previousSize);
        });
    }

    /**
     * Will test if a ChatRoom can add chatters.
     * If the chatters are a Band, it will add every member of the Band.
     * @param chatRoom The arranged ChatRoom to test add.
     */
    private void canAddChatters(ChatRoom chatRoom) {
        canAddArtist(chatRoom);
        canAddBand(chatRoom);
    }

    /**
     * Will test if a ChatRoom can add chatters.
     * @param chatRoom The arranged ChatRoom to test add.
     */
    private void canAddArtist(ChatRoom chatRoom) {
        User[] chatters = generateChatters(User.Authority.ARTIST);

        test(() -> {
            arrange(() -> chatRoom);

            int previousSize = chatRoom.get_chatters().size();

            act(() -> chatRoom.add(chatters));

            asserting(chatRoom.get_chatters().size() == previousSize + chatters.length);
        });
    }

    /**
     * If the chatters are a Band, it will add every member of the Band.
     * @param chatRoom The arranged ChatRoom to test add.
     */
    private void canAddBand(ChatRoom chatRoom) {
        User[] users = generateChatters(User.Authority.BAND);
        Band[] bands = new Band[users.length];
        for (int i = 0; i < users.length; i++)
            bands[i] = (Band) users[i];

        test(() -> {
            arrange(() -> chatRoom);

            int previousSize = chatRoom.get_chatters().size();
            Seszt<Artist> newMembers = new Seszt<>();
            for (Band band : bands)
                for (Artist artist : band.get_members())
                    if (!chatRoom.chatterExists(artist))
                        newMembers.add(artist);

            act(() -> chatRoom.add(bands));

            asserting(chatRoom.get_chatters().size() == previousSize + newMembers.size());
        });
    }

    /**
     * Will generate some Mails.
     * @param chatRoom The ChatRoom the Mails are from.
     * @param author The creator of the Mails.
     * @return The generated Mails.
     */
    private Mail[] generateMails(ChatRoom chatRoom, User author) {
        Mail[] mails = new Mail[_random.nextInt(9)+1];
        for (int i = 0; i < mails.length; i++)
            mails[i] = generateMail(chatRoom, author);

        return mails;
    }

    /**
     * Will generate a Mail.
     * @param chatRoom The ChatRoom the Mail are from.
     * @param author The creator of the Mail.
     * @return The generated Mail.
     */
    private Mail generateMail(ChatRoom chatRoom, User author) {
        return new Mail(
                UUID.randomUUID(), chatRoom, author,
                RandomCreatorService.generateString(false,_random.nextInt(9)+1),
                false, new Plato(false), _random.nextBoolean(), LocalDateTime.now()
        );
    }

    /**
     * Will generate some Users as chatters of a ChatRoom.
     * @param authority The authority the Chatters should have.
     * @return The generated User.
     */
    private User[] generateChatters(User.Authority authority) {
        User[] chatters = new User[_random.nextInt(9)+1];
        for (int i = 0; i < chatters.length; i++) {
            User chatter = authority != User.Authority.BAND
                    ? _items.generateUser()
                    : _items.get_bands().get(_random.nextInt(_items.get_bands().size()));
            while (!chatter.get_authority().toString().equals(authority.toString()))
                chatter = _items.generateUser();

            chatters[i] = chatter;
        }

        return chatters;
    }

    @Override @Test
    protected void canSet() {

    }

    @Override @Test
    protected void canRemove() {

    }
}