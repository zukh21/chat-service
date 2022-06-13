import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

internal class WallServiceTest {

    @Test
    fun createChat() {
        val service = WallService
        val chat = ChatService(0, message = arrayOf(), user = User(), friend = User(id = 2))
        val result = service.createChat(chat)
        assertEquals(chat, result)
        assertThrows(UserEqualToNewChatUser::class.java){
            service.createChat(chat)
        }
    }

    @Test
    fun removeChat() {
        val service = WallService
        val chat = ChatService(0, message = arrayOf(), user = User(), friend = User(id = 2))
        service.createChat(chat)
        val result = service.removeChat(0)
        assertTrue(result)

        assertThrows(NotFoundException::class.java){
            service.removeChat(0)
        }
    }

    @Test
    fun createMessage() {
        val service = WallService
        val chat = ChatService(0, message = arrayOf(), user = User(), friend = User(id = 2))
        val chat1 = ChatService(1, message = arrayOf(), user = User(), friend = User(id = 3))
        assertThrows(AllErrorException::class.java){
            service.createMessage(Message(mess = "Test message"), chat, chat1)
        }
    }

    @Test
    fun removeMessage() {
        val chat = ChatService(0, message = arrayOf(), user = User(), friend = User(id = 2))
        Message(mess = "Test message")
        val service = WallService
        val result = service.removeMessage(0, chat)
        assertTrue(result)
    }
}