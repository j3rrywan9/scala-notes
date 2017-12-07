package course2.week3

class Wire {
  private var sigVal = false
  private var actions: List[Action] = List()

  def getSignal: Boolean = sigVal

  def setSignal(s: Boolean): Unit =
    if (s != sigVal) {
      sigVal = s
      actions foreach (_())
    }

  def addActions(a: Action): Unit = {
    actions = a :: actions
    a()
  }
}
