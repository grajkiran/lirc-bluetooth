class NonBlockingThread( ):
    active = True

    def __init__(self):
        self.active = True
    
    def setactive(self,  active):
        self.active = active
    
    def isactive(self):
        return self.active
