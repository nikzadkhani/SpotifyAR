#SpotifyAR
## Credential Setup
The Spotify API doesn't allow




#In order to use the application, the core requirements are:
1. You have a device capable of AR.
2. You have the Spotify App downloaded and logged into a Premium account.
3. Physical Device because ARCore does not currently work on emulator.

#Walk Through of Our Application
## Preliminary Steps
1. Download Spotify
2. Sign into Spotify, making sure that the account you are using is a premium account

## Starting the application
1. Please use a physical device
   2. There are two methods provided to start application.
        - The android APK is attached. This method will allow you to bypass sending your SH1 key.
        - If this method does not work, the source code is also attached, but in order to test the application,
            your SH1 key must be sent to nikzad@bu.edu.
2. Once the application has been installed into your phone, it will be called Spotify AR (Icon: Android Robot throwing peace sign).

## Once the application is booted up

1. You will be greeted with a Spotify splash activity asking for you to log in.
   - This also informs you of the permissions we are requesting as well as confirming you are giving permission when you log in.
   - After you log in, our application and Spotify will be linked.
     - Note: Please use the Spotify account to log in as opposed to Google and Facebook.
   - If the log in is unsuccessful, the activity provided by Spotify will handle that case, and our application will wait until there is a successful authentication.

2. After successful authorization, the user will be greeted by the welcome page.
   - This page will display the user's username as well as their last played song as a suggestion to pick for the AR track they want to load.
   - This page will also notify you if your device is AR compatible.
   - In order to advance to the next page, we click the "Select A Track" button. This leads the user to the main activity.

3. The third activity is the meat of the application besides the AR activity. In this activity, you are able to pick a song from your "liked songs" on the Library View.
   If there are no songs to your liking, you are also able to switch to the search screen by swiping to the left or by clicking the search tab located at the top.


