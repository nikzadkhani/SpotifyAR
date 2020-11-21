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

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
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

//Accomplished the tempo synch
//Retrieve the BPM and incorporate into our code
//Create the function that checks the BPM and if its less than 100 divide by 2
//Click to stop is optional if we have time




public class ARActivity extends AppCompatActivity {
    private final String NO_PERMISSION = "Camera permission is needed to run this application";
    private final String TAG = "ARActivity";

    private ModelRenderable andyRenderable;
    private AnchorNode anchorNode;
    private SkeletonNode andy;
    private ModelAnimator animator;

    private final double AVERAGE_BPM = 100.00;
    private int bpm;
    double newDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_r);

        Intent intent = getIntent();
        String selectedTrack = intent.getExtras().getString("selectedTrack");
        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        PlayerService playerService = new PlayerService(ARActivity.this);
        playerService.addSongToPlaybackQueue(selectedTrack);



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



//        andyAnimator.setInterpolator(new )
        

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();

                    // Get the animation data called "andy_dance" from the `andyRenderable`.
                    AnimationData danceData = andyRenderable.getAnimationData("andy_dance");
                    ModelAnimator andyAnimator = new ModelAnimator(danceData, andyRenderable);
                   // andyAnimator.setInterpolator(new TempoSyncInterpolator());
                    Log.v("totalDurationOld", String.valueOf(andyAnimator.getTotalDuration()));
                    //Test: 25, 50, 75, 100, 125, 150. Note: Songs will never a bpm of 25, 50 is also rare.
                    // Tungs Favorite: 125 fast, 25 slow, 75~100 normal
                    // Mengqiao Favorite: <100 divide by 2, Remain the same (lets use this) (function for this)
                    if(bpm < 100){
                        newDuration = (double) (2*AVERAGE_BPM/bpm);
                    }
                    else{
                        newDuration = (double) (AVERAGE_BPM/bpm);
                    }
                     
                    Log.v("newDuration", String.valueOf(newDuration));
                    andyAnimator.setDuration((long) (andyAnimator.getTotalDuration() * newDuration));
                    Log.v("totalDurationNew", String.valueOf(andyAnimator.getTotalDuration()));
                    andyAnimator.setRepeatCount(100);
                    //Make an equation that takes in BPM and converts it into a extension of time.

                    //Repeat count should be the total milliseconds of song/ total duration to dance through whole song


                    playerService.playQueuedSong();
                    andyAnimator.start();

                    //Research Notes: Slow dance cuts off before 9 seconds

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