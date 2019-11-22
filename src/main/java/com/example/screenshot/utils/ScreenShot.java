package com.example.screenshot.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;
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
   * 视频图像截取 截取视频的一帧，并保存为图片bmp jpg jpeg png tiff等格式。截取的图片大小为原视频的大小
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
    } while (i < length && i < 50 && i < length / 100); //尽量帧数小，避免占用资源，也要避开视频开始的黑屏
    IplImage iplImage = converter.convertToIplImage(frame);
    opencv_imgcodecs.cvSaveImage(targetPath, iplImage);
    grabber.release();
    grabber.stop();
  }

  /**
   * 视频图像截取
   */
  public static void screenShot2(String sourcePath, String targetPath) throws IOException {
    String suffix = targetPath.substring(targetPath.lastIndexOf(".") + 1);
    Java2DFrameConverter converter = new Java2DFrameConverter();
    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(sourcePath);
    grabber.start();
    Frame frame = null;
    int length = grabber.getLengthInFrames();
    int i = 0;
    do {
      frame = grabber.grabImage();
      i++;
    } while (i < length && i < 50 && i < length / 100);
    BufferedImage image = converter.convert(frame);
    // 可操作BufferImage 实现图片的缩放，剪切等功能
    ImageIO.write(image, suffix, new File(targetPath));
  }

  /**
   * 视频转音频
   * TODO: 暂时有问题
   */
  public static void videoToVoice(String sourcePath, String targetPath)
      throws Exception, FrameRecorder.Exception {
    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(sourcePath);
    FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(targetPath, 1);
    grabber.start();
    // 输出格式
    recorder.setAudioCodec(avcodec.AV_CODEC_ID_PCM_S16LE);
    recorder.setAudioChannels(grabber.getAudioChannels());
    recorder.start();
    Frame frame = null;
    while (null != (frame = grabber.grabFrame(true, false, true, true))) {
      recorder.record(frame);
    }
  }

  public static void main(String[] args) throws IOException {
//    String source = "F:\\Video\\NeZha.mp4";
//    String target = "F:\\Video\\nezha.png";
//    screenShot(source, target);
    System.out.println(System.currentTimeMillis());
  }

}
