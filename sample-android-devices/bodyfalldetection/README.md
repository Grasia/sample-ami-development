An example of development using jALI (java Android Layer for Interaction). 

In this example, a known a tutorial from [techrepublic](http://www.techrepublic.com/blog/software-engineer/a-quick-tutorial-on-coding-androids-accelerometer/) is adapted to run in the emulator and to show an standing or laying down person. This example is used as a simplified fall detector.

The change focuses on the line acquiring the SensorManager. Instead of using the activity, it uses static methods of the class SimManager to gain access to a SensorManager instance. This instance will connect with the 3D emulation to get realistic acceleration signals and with the real sensors when running in  real mobiles. 


### REQUIREMENTS:

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

### USAGE:

Make sure the Android SDK is available in the classpath. In particular, the following paths are needed 
*ANDROID_HOME/tools* and *ANDROID_HOME/platform-tools* are in the PATH variable.

To build your apk, simply:

	mvn clean install

To deploy your apk to the connected device or a running simulator:

	mvn android:deploy

If Android SDK tools are in the PATH, the same could be done with adb tool

	adb install target/NAME_OF_THE_APK

As it is, the application can run from the mobile. To run it from the emulator, the PHAT needs to be downloaded too
