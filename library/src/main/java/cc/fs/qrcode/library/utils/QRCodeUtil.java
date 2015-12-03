package cc.fs.qrcode.library.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * @类功能说明: 生成二维码图片示例
 */
public class QRCodeUtil
{
    private static int QR_WIDTH = 200;
    private static int QR_HEIGHT = 200;

    //要转换的地址或字符串,可以是中文
    public static  Bitmap createQRImage(String msg)
    {
        try
        {
            //判断URL合法性
            if (msg == null || "".equals(msg) || msg.length() < 1)
            {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            hints.put(EncodeHintType.ERROR_CORRECTION,""+ com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H);
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(msg, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++)
            {
                for (int x = 0; x < QR_WIDTH; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
//            createQRCodeBitmapWithPortrait(bitmap,head);
            return bitmap;
        }
        catch (WriterException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createQRCodeBitmapWithPortrait(String msg, Bitmap portrait) {

        float portraitSize = 40f;
        Bitmap qr = createQRImage(msg);
        if(qr == null)
            return null;
        // 头像图片的大小
        int portrait_W = portrait.getWidth();
        int portrait_H = portrait.getHeight();

        //头像太大需要缩小(Math.round(a/b)/100.0)
        float sacleSize = portrait_W > portrait_H ? portraitSize/portrait_W:portraitSize/portrait_H;
        Matrix matrix = new Matrix();
        matrix.postScale(sacleSize,sacleSize); //长和宽放大缩小的比例
        Bitmap sacleBitmap = Bitmap.createBitmap(portrait,0,0,portrait_W,portrait_H,matrix,true);
        portrait_W = sacleBitmap.getWidth();
        portrait_H = sacleBitmap.getHeight();


        // 设置头像要显示的位置，即居中显示
        int left = (qr.getWidth() - portrait_W) / 2;
        int top = (qr.getHeight() - portrait_H) / 2;
        int right = left + portrait_W;
        int bottom = top + portrait_H;
        Rect rect1 = new Rect(left, top, right, bottom);

        // 取得qr二维码图片上的画笔，即要在二维码图片上绘制我们的头像
        Canvas canvas = new Canvas(qr);

        // 设置我们要绘制的范围大小，也就是头像的大小范围
        Rect rect2 = new Rect(0, 0, portrait_W, portrait_H);
        // 开始绘制
        canvas.drawBitmap(sacleBitmap, rect2, rect1, null);
        return qr;
    }
}
