trait Observer[T] {

    def receiveUpdate(update: T)

}
