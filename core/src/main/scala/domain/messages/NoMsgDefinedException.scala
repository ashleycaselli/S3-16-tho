package domain.messages

/**
  *
  * @param s exception's description
  */
case class NoMsgDefinedException(val s: String) extends Exception(s)
