package com.example.spotifyar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.animation.TimeInterpolator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

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
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;


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
    int repeatCount;
    String selectedTrackName;
    private int models_placed = 0;
    private int pause_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_r);

        Intent intent = getIntent();
        String selectedTrack = intent.getExtras().getString("selectedTrack");
        selectedTrackName = intent.getExtras().getString("selectedTrackName");



        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);








        PlayerService playerService = new PlayerService(ARActivity.this);
        playerService.addSongToPlaybackQueue(selectedTrack);

        //get the tempo and duration time from audio class
        AudioService audioService = new AudioService(ARActivity.this);
        audioService.getAudioFeatures(selectedTrack, () -> {
            Audio audio = audioService.getCurrentAudio();
            tempo = audio.getTempo();
            durationInMs = audio.getDuration();
        });

        //start a dancing robot
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane,  MotionEvent motionEvent) -> {
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
                            Log.e(TAG, "Failed to load my boi",throwable);
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
                    if(tempo < 100){
                        newDuration = (double) (2*AVERAGE_BPM/tempo);
                    }
                    else{
                        newDuration = (double) (AVERAGE_BPM/tempo);
                    }

                    //Song loops and robot keeps dancing.
                    andyAnimator.setDuration((long) (andyAnimator.getTotalDuration() * newDuration));
                    andyAnimator.setRepeatCount((int) ((durationInMs/(andyAnimator.getTotalDuration() * newDuration))));                    //Make an equation that takes in BPM and converts it into a extension of time.

                    //Repeat count should be the total milliseconds of song/ total duration to dance through whole song
                    // Get the animation data called "andy_dance" from the `andyRenderable`.

                    //tap to  pause or resume the robot
                    andy.setOnTapListener(new Node.OnTapListener() {
                        @Override
                        public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
                            if (andyAnimator.isPaused()) {
                                andyAnimator.resume();
                                pause_count--;
                                playerService.resumePlayback();
                            }
                            else {
                                andyAnimator.pause();
                                pause_count++;
                                if (models_placed == pause_count) {
                                    playerService.pausePlayback();
                                }

                            }
                        }
                    });
                    playerService.playQueuedSong();
                    andyAnimator.start();
                });







        // code below makes a big sphere
//        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
//            Anchor anchor = hitResult.createAnchor();
//
//            MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.GREEN))
//                    .thenAccept(material -> {
//                        ModelRenderable renderable = ShapeFactory.makeSphere(1.0f, new Vector3(0f, 1f, 1f), material);
//
//                        AnchorNode anchorNode = new AnchorNode(anchor);
//                        anchorNode.setRenderable(renderable);
//                        arFragment.getArSceneView().getScene().addChild(anchorNode);
//                    });
//        });
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