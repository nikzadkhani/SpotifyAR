#SpotifyAR
#In order to use the application, the core requirements are:
1. You have a device capable of AR.
2. You have the Spotify App downloaded and logged into a Premium account.
3. Physical Device because ARCore does not currently work on emulator.

# Extra Credit (3 items)
- Get Permissions for A using CameraPermissionsHelper
- Get Permissions for Spotify using LoginActivity and auth token
- Loose coupled Fragment Communication (All fragments use ListActivity methods to interact)
- Gestures (Swiping Left & Right with ViewPager in ListActivity)

# Project Structure
Based off of https://github.com/Hyferion/spotify_api_example and https://github.com/google-ar/sceneform-android-sdk/tree/v1.15.0/samples/animation
### Top Layer
The top layer contains the Activities for the application and a helper to get Camera Permissions courtesy of Google.
### Services
Contains all the services to make calls to the Spotify Web API.
### Models
Contains all the models for deserialization in Spotify Web API Volley callback.
### Interfaces
Contains all the interfaces for interfragment communication.
### Fragments
Contains all the fragments. All are fragments used in ListActivity
### Adapters
Contains all the adapter used for RecyclerViews and the ViewPager2.

#Walk Through of Our Application
## Preliminary Steps
1. Download Spotify
2. Sign into Spotify, making sure that the account you are using is a premium account

## Starting the application
1. Please use a physical device
   2. There are two methods provided to start application.
        - The android APK is attached in the following directory path
            ```SpotifyAR/app/releases/app-release.apk```
          This method will allow you to bypass sending your SHA1 key.
        _ The android APK can also be downloaded at the following link:
            https://drive.google.com/file/d/1b-XFN7NRc9HOrGFHY0IxrL4youIswhj_/view?usp=sharing
        - If this method does not work, the source code is also attached, but in order to test the application,
            your SHA1 key must be sent to nikzad@bu.edu.
2. Once the application has been installed into your phone, it will be called Spotify AR (Icon: Android Robot throwing peace sign).

## Once the application is booted up

## Splash Activity
1. You will be greeted with a Spotify login activity asking for you to log in. The SplashActivity uses OnActivityResult
to get the Authorization token for our API calls made using the `services` subpackage
   - First check's user's wifi connection, then will open login activity only if user connects to wifi
   - Loin Activity informs you of the permissions scope we are requesting. Scope is granted upon Login click.
   - After you log in, our application and Spotify will be linked via Authorization token.
     - Note: Please use the Spotify account to log in as opposed to Google and Facebook.
   - If the log in is unsuccessful, the activity provided by Spotify will handle that case, and our application will wait until there is a successful authentication.
   - Using the backstack should exit the application.
## Main Activity
2. After successful authorization, the user will be greeted by the welcome page.
   - This page will display the user's username as well as their last played song as a suggestion to pick for the AR track they want to load.
   - This page will also notify you if your device is AR compatible.
   - In order to advance to the next page, we click the "Select A Track" button. This leads the user to the main activity.


## List Activity
3. The third activity is the meat of the application besides the AR activity. In this activity, you are able to pick a song from your "liked songs" on the Library View.
   If there are no songs to your liking, you are also able to switch to the search screen by swiping to the left or by clicking the search tab located at the top.

    Note: Keep in mind that the robot dances according to the average BPM of the song.
          A "slow" sad song can still have a high beats per minute!
          Some song suggestions to test the difference:
            For a fast song: Untouched by The Veronicas (BPM: 170)
            For a slow song: Somewhere Only We Know by Glee (BPM: 75)

   ###### Library View
       A. You are able to see the first 20 liked songs in your library.
       B. Just tap a song you want to listen to, it will be highlighted in yellow. 
       C. Press lets dance to navigate to the AR mode. 


   ###### Search View
       A. There are three ways to search: track title, artist, album. 
           - The artist when selected will play popular songs by the artist
           - The album when selected will play songs in that specific album 
       B. You can search using the editText view with the magnifiying glass, and when you are finished
           with your query. Simply click enter on your keyboard  or press the magnifying glass to search.
       C. Select the artist/track/ or album you would like to play and head to the AR by clicking lets dance. 

    Note: When lets dance is clicked with no song selected, the button will not do anything.

## AR Activity
4. In the AR mode, you will first see a hand holding a phone waving it in a circular motion.
   Find a flat surface and do the motion depicted until you noticed a bunch of white circles on the flat surface.
   If you are having trouble getting the anchors (white circles) to pop up, try holding your phone sideways.

   - Once the anchor is found, click on one of the white circles to plop down an Andy Robot!
   - Your music will start playing and your robot will dance.
         Note: You can place down more than one robot, as much as your phone can handle.
               The song will restart whenever you put a robot down, but that was intentional when we wanted to allow you to play different songs per robot (in future iterations of the app).
               The feature is not available currently since the work it would take to implement would be very substantial.

   - You are able to pause your andy. If all andys are paused, then the song will stop playing. If at least one is dancing, then the song will resume/keep playing.

   - In order to go back and choose a song, use the android back button to go back to the list view.
     If you don't have a physical back button, pull down on your screen to make the virtual one pop up and click the back button (backwards arrow) to use the backstack.
   - Leaving the AR activity will delete your andys (going back to choose a new song counts as leaving), but your andys will not be deleted if you simply move the camera away from them.





