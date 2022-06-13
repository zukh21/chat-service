import java.lang.Exception

data class ChatService(
    var id: Int = 0,
    val user: User,
    val friend: User,
    var message: Array<Message>,
    var isMessageUnread: Boolean = false
)
class UserEqualToNewChatUser(mess: String) : Exception(mess)
class IsEmpty(mess: String) : Exception(mess)
class NotFoundException(mess: String) : Exception(mess)
class AllErrorException(mess: String) : Exception(mess)
data class Message(
    var id: Int = 0,
    val mess: String,
    var isUnread: Boolean = true
)

data class User(
    var id: Int = 0,
    val name: String = "Tom"
)

object WallService{
    private val directMessages = mutableListOf<ChatService>()
    private val messages = mutableListOf<Message>()
    private val users = mutableListOf<User>()
    private val defaultMessage = Message(mess = "Нет сообщений", isUnread = false)
    private var idForChat = 0
    private var idForUser = 0
    private var idForMess = 0
    fun createUser(user: User): User {
        users.add(user)
        user.id = ++idForUser
        return user

    }
    fun createChat(chat: ChatService): ChatService {
            if (chat.friend.id != chat.user.id){
                chat.id = ++idForChat
                directMessages.add(chat)
                return chat
            }
        throw UserEqualToNewChatUser("Вы не можете создать чат себе выберите другие пользователи")
    }

    fun getChatsList(){
        if (directMessages.isNotEmpty()){
            for (chat in directMessages){
                if (chat.message.isEmpty()){
                    chat.message = arrayOf(defaultMessage)
                }
                println(chat)
            }
        }else throw IsEmpty("У вас нету чаты")
    }

    fun removeChat(chatId: Int): Boolean {
        for (chat in directMessages){
            if (chat.id == chatId){
                directMessages -= chat
                return true
            }
        }
        throw NotFoundException("Чат с id $chatId не найден")
    }

    fun getUnreadChatsCount(): String {
        var count = 0
            for (chat in directMessages){
                if (chat.isMessageUnread){
                    count++
                }
            }
        return "Не прочитанные чаты: $count"
    }

    fun createMessage(newMessage: Message, sender: ChatService, recipient: ChatService): Message {
        if (newMessage.isUnread && sender.id != recipient.id){
            sender.isMessageUnread = false
            newMessage.isUnread = false
            sender.message += arrayOf(newMessage)
            recipient.isMessageUnread = true
            newMessage.isUnread = true
            recipient.message += arrayOf(newMessage)
            newMessage.id = ++ idForMess
            messages.add(newMessage)
            return newMessage
        }
        throw AllErrorException("Создать сообщения не получился")
    }

    fun editMessage(messId: Int, newMess: Message): Message {
        for (mess in messages){
            if (messId == mess.id){
                messages[messId] = newMess.copy(id = mess.id)
                messages -= mess
                return newMess
            }
        }
        throw NotFoundException("Смс с id $messId не найден")
    }

    fun removeMessage(messId: Int, user: ChatService): Boolean {
        for (mess in messages){
            if (mess.id == messId && mess in user.message){
                messages.removeAt(messId-1)
                return true
            }
        }
        throw NotFoundException("Смс с id $messId не найден или вы хотите чужой смс удалить")
    }

    fun getAllMessages(){
        for (mess in messages){
            println(mess)
        }
    }
    fun getAllMessagesFromChat(idChat: Int) {
        var i = 0
        for (chat in directMessages){
            if (idChat == chat.id){
                println("Чат id: ${chat.id}")
                while (i != chat.message.size){
                    messages[i].isUnread = false
                    println("${chat.message[i]}")
                    i++
                }
            }

        }
    }

}

fun main(){
    val service = WallService
    val user = service.createUser(User(name = "Zukh"))
    val user1 = service.createUser(User(name = "Ilyo"))
    val user2 = service.createUser(User(name = "Muza"))
    println("$user,\n$user1")
    val mess1 = Message( mess = "message 1")
    val mess2 = Message( mess = "message 2")
    val mess3 = Message( mess = "message 3")
    val chat1 = service.createChat(ChatService(user = user, friend = user1, message = arrayOf()))
    val chat2 = service.createChat(ChatService(user = user, friend = user2, message = arrayOf()))
    service.createMessage(mess2, chat2, chat1)
    service.createMessage(mess1, chat2, chat1)
    service.createMessage(mess3, chat2, chat1)
    service.getChatsList()
//    val eMess = service.editMessage(2, mess3)
//    println(service.removeMessage(2, chat1))
    service.getAllMessages()
    service.getChatsList()
//    service.getAllMessages()
    println(service.getUnreadChatsCount())
    service.getAllMessagesFromChat(1)
}