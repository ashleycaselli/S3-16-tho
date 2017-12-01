package domain.messages

/**
  *
  * @param s exception's description
  */
case class NoMsgDefinedException(s: String) extends Exception(s)
