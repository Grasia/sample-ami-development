An example of development using jALI (java Android Layer for Interaction). jALI is a framework that allows developers to deploy Android applications in virtual worlds.
The jALI applications are connected to the virtual world as if it was the real one. The same applications can be deployed too in real world android devices and expect similar behaviors. This is the Virtual Living Lab concept as introduced in 

	Pablo Campillo-Sanchez, Jorge J. Gómez-Sanz: Agent Based Simulation for Creating Ambient Assisted Living Solutions. PAAMS 2014: 319-322

The Virtual World is provided by the PHAT framework. The developer can assume a number of Android Virtual Machines being connected to the simulated world. The Ambient Intelligence solution will be provided by those Android devices and the software developed in the simulation will be the same that will be used in the future Ambient Intelligence deployment. 

This project contains examples of devices developed using jALI. In particular, an accelerometer based app that identifies the body position, an app for detecting the sound intensity, and an app capable of capturing the camera video stream. All of them can be deployed into real phones or emulated ones. In the second case, they can be integrated with PHAT framework and receive real time feed from the 3D simulation. 

The development of applications that work inside PHAT can be made in different ways:

- Using ADT + Eclipse. This works with previous SDK Build versions, but connecting with the 3D world makes the dexing step of the android development to fail due to failures in the ADT classpath resolution. Hence, developers are advised that the application can compile, but may not be deployed in a emulator.
- Using Gradle + Android Studio. There is a basic gradle configuration to enable in-device run of the code. 
- Using Maven. This is the preferred version, since it is the most reliable. The developer loses the debugging capabilities, though. In any case, the integration of the generated application into the simulation requires using maven.

# REQUIREMENTS:

Basic development requires the following:

- Java 1.7 (set variable JAVA_HOME)
- Maven 3.1.1+ installed, see http://maven.apache.org/download.html (set variable M2_HOME)
- Ant (set variable ANT_HOME)
- Android SDK (r21.1 or later, latest is best supported) installed, preferably with all platforms, see http://developer.android.com/sdk/index.html
- Make sure you have images for API 19 installed in your android SDK. It is required to have the IntelAtomx86 image to permit hardware acceleration. Instructions for Android are available in the [Android site](http://developer.android.com/tools/devices/emulator.html#acceleration)
- Set environment variable ANDROID_HOME to the path of your installed Android SDK and add $ANDROID_HOME/tools as well as $ANDROID_HOME/platform-tools to your $PATH. (or on Windows %ANDROID_HOME%\tools and %ANDROID_HOME%\platform-tools).
- Add binaries to environment variable PHAT (PATH=$PATH:$HOME/bin:$JAVA_HOME/bin:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$M2_HOME/bin)

The use of an IDE requires extra elements:

- Eclipse + ADT. An eclipse install, preferibly one with maven enabled like the JAVA developer or Rich Client Platform eclipse variants. It also requires Android plugins. They can be installed from the Eclipse Marketplace. In particular, the "Android  for Maven Eclipse 1.4.0" and the "Android development tools for Eclipse".
- Android Studio + Gradle. Basic installs are already configured with all necessary software.

To run the software connected to the virtual world, a virtual lab instance is needed. One copy can be downloaded from 

# USAGE:

Make sure the Android SDK is available in the classpath. In particular, the following paths are needed 
*ANDROID_HOME/tools* and *ANDROID_HOME/platform-tools* are in the PATH variable.

To build all availables apks, simply, from root:

	mvn clean install

To deploy your apk to the connected device or a running simulator you need to move to the particular app folder

	mvn android:deploy

If Android SDK tools are in the PATH, the same could be done with adb tool

	adb install target/NAME_OF_THE_APK


As it is, the application can run from the mobile. To run it from the emulator, the PHAT needs to be downloaded too

# Launching from the 3D simulation

To do this, you need to have installed also a 3D simulation. A basic simulation that can work with these devices is 
