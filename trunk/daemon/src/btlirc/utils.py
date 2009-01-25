

def dump(obj):
  for attr in dir(obj):
    print "obj.%s = %s" % (attr, getattr(obj, attr))

class NonBlockingThread( ):
    active = True

    def __init__(self):
        self.active = True
    
    def setactive(self,  active):
        #print "Changing active status to: "+str(active)
        self.active = active
    
    def isactive(self):
        return self.active
