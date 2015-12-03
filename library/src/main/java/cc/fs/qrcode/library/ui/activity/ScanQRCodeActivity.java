package cc.fs.qrcode.library.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

import cc.fs.qrcode.library.R;
import cc.fs.qrcode.library.zxing.CameraManager;
import cc.fs.qrcode.library.zxing.CaptureActivityHandler;
import cc.fs.qrcode.library.zxing.InactivityTimer;
import cc.fs.qrcode.library.ui.view.ScanBoxView;

/**
 * 扫描二维码的acitivity 需要申请权限具体看demo
 */
public class ScanQRCodeActivity extends Activity implements Callback {

    public static final int SCAN_RESULT = R.id.scanbox;
    private CaptureActivityHandler handler;
    private ScanBoxView scanbox;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;

    public static void start(Activity context){
        Intent intent = new Intent(context,ScanQRCodeActivity.class);
        context.startActivityForResult(intent,SCAN_RESULT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            throw new NullPointerException();
        }
        setContentView(R.layout.activity_capture);
        CameraManager.init(getApplication());
        scanbox = (ScanBoxView) findViewById(R.id.scanbox);

        ImageView mButtonBack = (ImageView) findViewById(R.id.backBtn);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(ScanQRCodeActivity.this, "扫描失败", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();

            try {
                bundle.putString("result", resultString);
                Log.e("扫描结果 ", "  " + resultString);
                resultIntent.putExtras(bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(ScanQRCodeActivity.this, "扫描成功", Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_OK, resultIntent);
        }
        ScanQRCodeActivity.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ScanBoxView getScanbox() {
        return scanbox;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        scanbox.drawViewfinder();

    }
}