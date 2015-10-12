A development with **AIDE** has two parts:

- One were the activities of daily living, the actors involved, the physical realm, and the elements in the AmI are defined.
- Another were the control software for AmI elements are created reusing one of the **AIDE** parts, jALI

In this example, the scenario is about two people in the same house. One of them has Parkinson and needs some monitoring solution to make the caregiver less concerned about the status of the caretaker. The monitoring solution ought to warn whenever there is a fall on behalf the patient. The solution devised in this example is not prepared for a real life situation, yet, but it shows the potential of the **AIDE** and the Virtual Living Lab concept. 


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

# USAGE:

To install the system, the following needs to be done:

	mvn clean install

With this, all files will be compiled and installed. After this, the following is possible:

- Running the 3D simulation. Sample scenarios can be run within the basic-modeling-sample folder with

	ant runSimAppBodyPosHandNoDevices

- Installing the APKs in your Android phone (required kit kat version, at least). For this, follow instructions from the folder sample-android-devices
- Running the whole simulation with devices and software. This requires following some instructions from basic-modeling-sample folder to create involved virtual devices
 



