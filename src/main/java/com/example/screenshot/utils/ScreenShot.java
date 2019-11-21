package com.example.screenshot.utils;


import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.IplImage;

/**
 * @author Wangjunwei
 * @Date 2019/11/20 17:45
 * @Description 图片截取
 */
public class ScreenShot {

  /**
   * 获取视频时长(单位：秒)
   */
  public static long getVideoLength(String sourcePath) throws Exception {
    long length = 0;
    FrameGrabber grabber = new FFmpegFrameGrabber(sourcePath);
    grabber.start();
    length = grabber.getLengthInTime() / (1000 * 1000);
    grabber.stop();
    return length;
  }

  /**
   * 截取视频的一帧，并保存为图片bmp jpg jpeg png tiff等格式。 截取的图片大小为原视频的大小
   */
  public static void screenShot(String sourcePath, String targetPath) throws Exception {
    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(sourcePath);
    // C语言图像类型
    OpenCVFrameConverter.ToIplImage converter = new ToIplImage();
    grabber.start();
    int length = grabber.getLengthInFrames();
    System.out.println("视频帧数：" + length);
    if (1 > length) {
      return;
    }
    Frame frame = null;
    int i = 0;
    // 获取一帧
    do {
      frame = grabber.grabImage();
      i++;
    } while (i < length && i < 50 && i < length / 1000); //尽量帧数小，避免占用资源，也要避开视频开始的黑屏
    IplImage iplImage = converter.convertToIplImage(frame);
    opencv_imgcodecs.cvSaveImage(targetPath, iplImage);
    grabber.stop();
  }

}
