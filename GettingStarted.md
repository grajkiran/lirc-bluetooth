# Installing the daemon #

  1. First you need to install **btlirc** daemon. It is really simple - just download the latest Debian/Ubuntu package and install it:
```
$ wget http://lirc-bluetooth.googlecode.com/files/btlirc_0.1-1_i386.deb
$ sudo dpkg -i btlirc_0.1-1_i386.deb
```
  1. Copy sample `.lircrc` configuration in your home directory:
```
$ cp /usr/share/doc/btlirc/dot_lircrc ~/.lircrc
```
  1. That's it! The daemon is successfully installed.

# Installing the mobile application #

There are several ways to install the mobile application - choose either one of them, whichever seems you the easiest.

## Installing over the air ##

It is the simplest way to install the mobile application on your phone. In order to use this method, you need to have data plan (GPRS/CSD) from your carrier. Or in other words, you must check if you can open web pages using cell phone's mobile browser. **Note:** You don't need data plan to _use_ the application once it is installed. Your carrier _will_ charge you small fee for the few kilobytes traffic you generated downloading the application.

To install the mobile application, open one of the follwing URLs using your cellphone's browser:
  * http://lirc-bluetooth.googlecode.com/files/LircBT.jar
  * http://tinyurl.com/btlirc
(I know, typing long URLs on cellphone keypad is tedious job)

Follow the instructions on the phone's screen to complete the installation. This method is tested with Nokia 6021 and I suppose it should work with any other Nokia phone.

If the described method above doesn't work, try the following: some mobile phones need second file - **JAD file** - for installing an application. The JAD files are located at:
  * http://lirc-bluetooth.googlecode.com/files/LircBT.jad
  * http://tinyurl.com/btlirc-jad

Same as above, try to open the URLs using your cellphone's browser. **Note:** This method is not working on Nokia phone but might work on other cell phones (Sony Ericsson or Samsung)

## Installing over Bluetooth ##

_TODO_