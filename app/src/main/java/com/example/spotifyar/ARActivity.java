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


//Clean up code
//Add Comments
//Error Check
//Show the group in case they have questions.


//Two android guys before the dance finishes.
//Important notes, all andys we make are the same andy. This is the reason for the crash. When I pause one andy all pause dancing.

//Spamming it might cause a crash --> adding multiple andys dont crash. It only crashes when theyre dancing
// E/MessageQueue-JNI: java.lang.IllegalStateException: Only one ModelAnimator may play on a ModelRenderable at a time
//         at com.google.ar.sceneform.animation.AnimationEngine.addRenderable(Unknown Source:35)
//         at com.google.ar.sceneform.animation.ModelAnimatorImpl.start(Unknown Source:42)
//         at com.google.ar.sceneform.animation.ModelAnimator.start(Unknown Source:2)
//         at com.example.spotifyar.ARActivity.lambda$onCreate$3$ARActivity(ARActivity.java:184)


//https://stackoverflow.com/questions/55681374/how-to-play-multiple-animations-using-animatorset
//Solution is to create their own dynamically

//The same renderable, different nodes




//Renderable smaller
//Differe




//Dragging the Andy/Scaling Andy --> No crash
//Main




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

        //get the tempo and duration time from audio class
        AudioService audioService = new AudioService(ARActivity.this);

        //start a dancing robot
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

                    mSpotifyAppRemote.getPlayerApi().play(uri);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
                        Track currentTrack = playerState.track;
                        Log.v("CurrentPlayingTrack", currentTrack.toString());
                        audioService.getAudioFeatures(currentTrack.uri, () -> {
                            Audio audio = audioService.getCurrentAudio();
                            tempo = audio.getTempo();
                            durationInMs = audio.getDuration();


                            //create the model
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

                            if (andyRenderable == null) {
                                return;
                            }

                            // Create the Anchor.
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            anchorNode.setLocalScale(new Vector3(0.3f, 0.3f, 0.3f));

                            // Create the transformable andy and add it to the anchor.
                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            // andy.getScaleController().setMinScale(0.1f);
                            // andy.getScaleController().setMaxScale(1f);

                            // andy.setLocalScale(new Vector3(0.1f, 0.1f, 1f));
                            // Toast.makeText(getApplicationContext(), andy.getLocalScale().toString(), Toast.LENGTH_SHORT).show();
                            andy.setParent(anchorNode);
                            andy.setRenderable(andyRenderable);
                            models_placed++;
                            andy.select();


                            // Get the animation data called "andy_dance" from the `andyRenderable`.
                            AnimationData danceData = andyRenderable.getAnimationData("andy_dance");
                            ModelAnimator andyAnimator = new ModelAnimator(danceData, andyRenderable);
                            //<100 divide by 2, Remain the same (lets use this) (function for this)
                            if (tempo < 100) {
                                newDuration = (double) (2 * AVERAGE_BPM / tempo);
                            } else {
                                newDuration = (double) (AVERAGE_BPM / tempo);
                            }

                            //Song loops and robot keeps dancing.
                            andyAnimator.setDuration((long) (andyAnimator.getTotalDuration() * newDuration));
                            andyAnimator.setRepeatCount((int) ((durationInMs / (andyAnimator.getTotalDuration() * newDuration))));                    //Make an equation that takes in BPM and converts it into a extension of time.

                            //Repeat count should be the total milliseconds of song/ total duration to dance through whole song
                            // Get the animation data called "andy_dance" from the `andyRenderable`.

                            //tap to  pause or resume the robot
                            andy.setOnTapListener(new Node.OnTapListener() {
                                @Override
                                public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
                                    if (andyAnimator.isPaused()) {
                                        andyAnimator.resume();
                                        pause_count--;
                                        mSpotifyAppRemote.getPlayerApi().resume();
                                    } else {
                                        andyAnimator.pause();
                                        pause_count++;
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
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

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

    @Override
    protected void onStop() {
        super.onStop();
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