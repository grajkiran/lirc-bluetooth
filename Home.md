The project aims to allow users to control various programs on their PC using their cellphone via Bluetooth using customizable J2ME application. The goal is to support as many cell phones as possible without bonding with specific model or manufacturer.

The project consists of 2 components:
  * **btlirc** daemon emulates [LIRC](http://www.lirc.org/) protocol allowing any LIRC-enabled applications (MPlayer, XMMS, etc.) to be remote controlled. If the application don't have LIRC-capabilities, you can use [irexec](http://www.lirc.org/html/irexec.html) or [irxevent](http://www.lirc.org/html/irxevent.html) to send DCOP messages to KDE applications (Amarok, digiKam, etc.) or even simulating real keyboard key press or release
  * **LIRC Remote** J2ME application connects via Bluetooth to **btlirc** daemon and sends keypress. It is standard MIDP 2.0 application so it shoudl work on any mobile phone with Java and [JSR 082 support](http://en.wikipedia.org/wiki/Java_APIs_for_Bluetooth). It is tested on Nokia 6021 but should work with any Series 40 or Series 60 phone. See [Comapatability](Comapatability.md) page for more details

**First time here?** Check out [Getting Started](GettingStarted.md) page.

**Have problems?** Drop me a line at [svilen.ivanov@gmail.com](mailto:svilen.ivanov@gmail.com)

![http://lh4.ggpht.com/_B6BFfz7FZKE/SaEF2fhpIzI/AAAAAAAADrw/hLNc26SQDmE/s800/Nokia_6021.jpg](http://lh4.ggpht.com/_B6BFfz7FZKE/SaEF2fhpIzI/AAAAAAAADrw/hLNc26SQDmE/s800/Nokia_6021.jpg)