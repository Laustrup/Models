package laustrup.models.chats;

import laustrup.ModelTester;
import laustrup.dtos.chats.ChatRoomDTO;

import laustrup.models.users.User;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;

import org.junit.jupiter.api.Test;

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

    }

    @Override @Test
    protected void canAdd() {

    }

    @Override @Test
    protected void canSet() {

    }

    @Override @Test
    protected void canRemove() {

    }

    void add() {
    }

    void testAdd() {
    }

    void remove() {
    }

    void testRemove() {
    }
}