package com.example.spotifyar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifyar.models.Audio;
import com.example.spotifyar.services.AudioService;
import com.example.spotifyar.services.PlayerService;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SkeletonNode;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

public class ARActivity extends AppCompatActivity {
    private final String NO_PERMISSION = "Camera permission is needed to run this application";
    private final String TAG = "ARActivity";

    private ModelRenderable andyRenderable;
    private AnchorNode anchorNode;
    private SkeletonNode andy;
    private ModelAnimator animator;

    private final double AVERAGE_BPM = 100.00;private int bpm;
    double newDuration;
    private float tempo;
    private int durationInMs;
    
    private int models_placed = 0;
    private int pause_count = 0;

    private SpotifyAppRemote mSpotifyAppRemote;

    private static final String CLIENT_ID = "730bb52a8e884ac9bb4e03b49856815f";
    private static final String REDIRECT_URI = "https://com.example.spotifyar/callback";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_r);

        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        String uri = args.getString("uri");

        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        // Create audio service for the playing song.
        AudioService audioService = new AudioService(ARActivity.this);

        // Start a dancing robot when the user single tap on the screen
        // It requires user to find a plane to place the robot
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    // Create the model
                    ModelRenderable.builder()
                            .setSource(this, R.raw.andy_dance)
                            .build()
                            .thenAccept(renderable -> andyRenderable = renderable)
                            .exceptionally(
                                    throwable -> {
                                        Toast toast =
                                                Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        Log.e(TAG, "");
                                        return null;
                                    });

                    // Play the playlist.
                    mSpotifyAppRemote.getPlayerApi().play(uri);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.v("Hit Plane", "WE hit the plane");
                    mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
                        Track currentTrack = playerState.track;
                        Log.v("CurrentPlayingTrack", currentTrack.toString());

                        //get the tempo and duration time of the playing song from audio class
                        audioService.getAudioFeatures(currentTrack.uri, () -> {
                            Audio audio = audioService.getCurrentAudio();
                            tempo = audio.getTempo();
                            durationInMs = audio.getDuration();
                            Log.v("CurrentTempo", String.valueOf(tempo));




                            if (andyRenderable == null) {
                                return;
                            }

                            // Create the Anchor
                            // Anchor is what the andy renderable is set on. It connects the AR to the enviornment. 
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            // Define the size of the model.
                            anchorNode.setLocalScale(new Vector3(0.3f, 0.3f, 0.3f));

                            // Create the transformable andy and add it to the anchor.
                            // The transformable node allows the user to resize and move around their andy model. 
                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setParent(anchorNode);
                            // Sets the Renderable to display for this node.
                            andy.setRenderable(andyRenderable);
                            // Numbers of robots the user places.
                            models_placed++;
                            andy.select();


                            // Get the animation data called "andy_dance" from the `andyRenderable`.
                            AnimationData danceData = andyRenderable.getAnimationData("andy_dance");
                            // Set Animator using dance data for robot.
                            ModelAnimator andyAnimator = new ModelAnimator(danceData, andyRenderable);
                            // Adjust the tempo to show obviously different dancing speeds between different tempos.
                            // Temp <100: divide by 2; else: Remain the same.
                            if (tempo < 100) {
                                newDuration = (double) (2 * AVERAGE_BPM / tempo);
                            } else {
                                newDuration = (double) (AVERAGE_BPM / tempo);
                            }

                            // Shorted duration of the animation makes robots dancing faster. Vice versa.
                            andyAnimator.setDuration((long) (andyAnimator.getTotalDuration() * newDuration));
                            // The dancing animation will repeat integer times until song ends.
                            andyAnimator.setRepeatCount((int) ((durationInMs / (andyAnimator.getTotalDuration() * newDuration)))); 

                            // Tap on one robot to pause or resume its dancing
                            andy.setOnTapListener(new Node.OnTapListener() {
                                @Override
                                public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
                                    // If there's at least one robot dancing, the song will keep playing.
                                    if (andyAnimator.isPaused()) {
                                        andyAnimator.resume();
                                        pause_count--;
                                        mSpotifyAppRemote.getPlayerApi().resume();
                                    } else {
                                        andyAnimator.pause();
                                        pause_count++;
                                        // if all the robots dancing is paused, the song will get paused.
                                        if (models_placed == pause_count) {
                                            mSpotifyAppRemote.getPlayerApi().pause();
                                        }

                                    }
                                }
                            });
                            andyAnimator.start();
                        });

                    });
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Setup authentication to developer portal.
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        // Setup connection to Spotify app
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        mSpotifyAppRemote.getConnectApi().connectSwitchToLocalDevice();
                        Log.d("MainActivity", "Connected! Yay!");
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                    }
                });
    }

    // Disconnect when stop the AR activity.
    @Override
    protected void onStop() {
        super.onStop();
        mSpotifyAppRemote.getPlayerApi().pause();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // ARCore requires camera permission to operate.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this,NO_PERMISSION, Toast.LENGTH_LONG).show();
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this);
            }
            finish();
        }
    }

}