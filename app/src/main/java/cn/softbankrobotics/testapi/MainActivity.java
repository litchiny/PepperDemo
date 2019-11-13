package cn.softbankrobotics.testapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.Qi;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.TakePictureBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.camera.TakePicture;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private static final String TAG = "MainActivity";
    private ImageView iv_show;
    private TextView tv_camera;
    private boolean startTakePicture = false;
    private QiContext qiContext;
    private Future<TakePicture> takePictureFuture;
    private Disposable disposable;
    private String showTime = "";
    private boolean isStart = false;
    private int cameraTime = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_show = findViewById(R.id.iv_show);
        tv_camera = findViewById(R.id.tv_camera);
        QiSDK.register(this, this);
        initTime();
    }

    private void initTime() {
        String timeStr = SPUtil.getCameraTime(this);
        if (!TextUtils.isEmpty(timeStr)) {
            TextView tv_time = findViewById(R.id.tv_time);
            tv_time.setText(timeStr);
        }
        updateCameraTime();
    }

    private void updateCameraTime() {
        tv_camera.setText("拍照频率：" + cameraTime);
    }

    public void startTakePicture(View view) {
        if (qiContext == null) {
            showToast("qiContext == null");
            return;
        }
        startTakePicture = !startTakePicture;
        if (startTakePicture && disposable == null) {
            showToast("开始连续拍照视频");
            initCircleObservable();
        } else {
            showToast("连拍已准备，请等待");
        }
    }

    @Override
    protected void onDestroy() {
        saveCameraTime();
        QiSDK.unregister(this);
        destroyObservable();
        super.onDestroy();
    }

    private void destroyObservable() {
        if (disposable != null) {
            disposable.dispose();
        }
    }


    public void upCameraTime(View view) {
        if (cameraTime < 300)
            cameraTime += 5;
        updateCameraTime();
    }

    public void downCameraTime(View view) {
        if (cameraTime > 10)
            cameraTime -= 5;
        updateCameraTime();
    }


    private void saveCameraTime() {
        if (!TextUtils.isEmpty(showTime)) {
            long startTimeL = Long.valueOf(showTime);
            long duration = (System.currentTimeMillis() - startTimeL) / 1000;
            String spTime = SPUtil.getCameraTime(this);
            StringBuilder stringBuilder = new StringBuilder();
            if (!TextUtils.isEmpty(spTime))
                stringBuilder.append(spTime).append("\n");
            stringBuilder.append("startTime: ").append(startTimeL).append(",duration(seconds)：").append(duration);
            SPUtil.setCameraTime(this, stringBuilder.toString());
            Log.d(TAG, "saveCameraTime: startTime: " + stringBuilder.toString());
        }
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Log.d(TAG, "onRobotFocusGained: ");
        this.qiContext = qiContext;
        takePictureFuture = TakePictureBuilder.with(qiContext).buildAsync();
    }

    @Override
    public void onRobotFocusLost() {
        Log.d(TAG, "onRobotFocusLost: ");
        saveCameraTime();
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        Log.d(TAG, "onRobotFocusRefused: ");
    }

    private void initCircleObservable() {
        updateCameraTime();
        destroyObservable();
        disposable = Observable.interval(cameraTime, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> { //false
                    Log.d(TAG, "initPreview: doOnNext--isStartOnLine:");
                    startTakePicture();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> { //true
                    Log.d(TAG, "initPreview: subscribe");
                });
    }


    private void startTakePicture() {
        //        if (camera != null && mQiContext != null) {
//            camera.makeTakePicture(mQiContext.getRobotContext()).async().run().andThenConsume(timestampedImageHandle -> {
//                ByteBuffer buffer = timestampedImageHandle.getImage().getValue().getData();
//                buffer.rewind();
//                int pictureBufferSize = buffer.remaining();
//                byte[] pictureArray = new byte[pictureBufferSize];
//                buffer.get(pictureArray);
//                checkHumanFace(BitmapFactory.decodeByteArray(pictureArray, 0, pictureBufferSize));
//            });
//        }
        if (takePictureFuture != null)
            takePictureFuture.andThenCompose(Qi.onUiThread(takePicture -> {
                return takePicture.async().run();
            })).andThenConsume(timestampedImageHandle -> {
                ByteBuffer buffer = timestampedImageHandle.getImage().getValue().getData();
                buffer.rewind();
                final int pictureBufferSize = buffer.remaining();
                final byte[] pictureArray = new byte[pictureBufferSize];
                buffer.get(pictureArray);
                Bitmap onlineBitmap = BitmapFactory.decodeByteArray(pictureArray, 0, pictureBufferSize);
                runOnUiThread(() -> {
                    iv_show.setImageBitmap(onlineBitmap);
                });
                if (!isStart) {
                    showTime = String.valueOf(System.currentTimeMillis());
                    isStart = true;
                    Log.d(TAG, "startTakePicture: showTime: " + showTime);
                }
            });
    }


    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}
