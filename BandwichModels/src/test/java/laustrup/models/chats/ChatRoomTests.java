package laustrup.models.chats;

import laustrup.ModelTester;
import laustrup.dtos.chats.ChatRoomDTO;

import laustrup.models.chats.messages.Mail;
import laustrup.models.users.User;
import laustrup.models.users.sub_users.Performer;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;

import laustrup.services.RandomCreatorService;
import laustrup.utilities.collections.sets.Seszt;
import laustrup.utilities.parameters.Plato;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ChatRoomTests extends ModelTester<ChatRoom, ChatRoomDTO> {

    ChatRoomTests() { super(true); }

    @Override @Test // TODO Stack Overflow between member and band, temporally fixed, but when fixed, it should be removed.
    protected void dataTransportObjectTranslate() {
        test(() -> {
            ChatRoom expected = arrange(() -> {
                ChatRoom chatRoom = _items.get_chatRooms()[0];
                for (User chatter : chatRoom.get_chatters())
                    if (chatter.getClass() == Artist.class)
                        for (Band band : ((Artist) chatter).get_bands())
                            for (Artist member : band.get_members())
                                band.remove(member);
                    else if (chatter.getClass() == Band.class)
                        for (Artist member : ((Band) chatter).get_members())
                            for (Band band : member.get_bands())
                                member.remove(band);


                _dto = new ChatRoomDTO(chatRoom);
                return chatRoom;
            });

            ChatRoom actual = act(() -> new ChatRoom(_dto));


            asserting(expected.toString(),actual.toString());

            addToPrint("The two ChatRooms are:\n \n" + expected + "\n" + actual);
        });
    }

    @Override @Test
    protected void toStringTest() {
        ChatRoom arrangement = _items.get_chatRooms()[0];

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

    @Override @Test
    protected void canAdd() {
        ChatRoom arrangement = _items.get_chatRooms()[0];

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
                1, chatRoom, author,
                RandomCreatorService.get_instance().generateString(false,_random.nextInt(9)+1),
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
                    ? _items.generateUser() : _items.get_bands()[_random.nextInt(_items.get_bandAmount())];
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