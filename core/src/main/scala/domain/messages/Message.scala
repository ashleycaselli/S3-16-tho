package domain.messages

/**
  * A Message is an entity that could be spread through a communication channel.
  */
trait Message extends domain.Serializable {

    /**
      * Property to get the message's sender
      *
      * @return a String containing the sender's id
      */
    def sender: String

    /**
      * Property to get the message's payload
      *
      * @return contains a String that represents the payload
      */
    def payload: String

}
