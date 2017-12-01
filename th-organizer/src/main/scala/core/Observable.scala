package core

trait Observable[T] {

    private var observers: List[Observer[T]] = Nil

    def addObserver(observer: Observer[T]): Unit = observers = observer :: observers

    def notifyObservers(msg: T): Unit = observers foreach (_ receiveUpdate msg)
}
