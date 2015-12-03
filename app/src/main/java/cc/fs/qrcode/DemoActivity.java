package cc.fs.qrcode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import cc.fs.qrcode.library.ui.activity.ScanQRCodeActivity;
import cc.fs.qrcode.library.utils.QRCodeUtil;


/**
 * Created by fostion on 2015/12/2.
 */
public class DemoActivity extends Activity{

    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_demo);

        imageView = (ImageView)findViewById(R.id.image);

        ((Button)findViewById(R.id.scanBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanQRCodeActivity.start(DemoActivity.this);
            }
        });

        ((Button)findViewById(R.id.createBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap avatar = BitmapFactory.decodeResource(getResources(),R.drawable.demo);
                Bitmap bitmap = QRCodeUtil.createQRCodeBitmapWithPortrait("测试二维码生成是否有效",avatar);
                imageView.setImageBitmap(bitmap);
            }
        });
    }
}
