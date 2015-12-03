package cc.fs.qrcode.library.zxing;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

import cc.fs.qrcode.library.ui.view.ScanBoxView;


public final class ViewfinderResultPointCallback implements ResultPointCallback {

  private final ScanBoxView viewfinderView;

  public ViewfinderResultPointCallback(ScanBoxView viewfinderView) {
    this.viewfinderView = viewfinderView;
  }

  public void foundPossibleResultPoint(ResultPoint point) {
    viewfinderView.addPossibleResultPoint(point);
  }

}
