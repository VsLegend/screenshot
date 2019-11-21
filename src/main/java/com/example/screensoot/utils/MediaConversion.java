package com.example.screensoot.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import ws.schild.jave.Encoder;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

/**
 * @author Wangjunwei
 * @Date 2019/11/21 9:20
 * @Description 音视频转换
 */
public class MediaConversion {

  /**
   * 视频转语音
   */
  public static void videoToVoice(String sourcePath, String targetPath, String sourceType)
      throws EncoderException, MalformedURLException {

    videoToVoice(sourcePath, sourceType, targetPath, "pcm_s16le", "wav", 16000, 2, 8000);
  }

  /**
   * @param sourcePath 源文件地址
   * @param sourceType 文件类型 0远程地址  1本地地址
   * @param targetPath 输出文件地址
   * @param formatType 输出文件格式  pcm_s16le  libmp3lame
   * @param fileType 输出文件类型 wav、pcm、mp3、silk、speex、amr、m4a
   * @param bitRate 比特率 bits 16000 25600 56000 64000 65535 128000 160000
   * @param channels 声道 1单声道 2双声道
   * @param samplingRate 采样频率 kHZ 8000 11025 22050 44100 48000
   */
  public static void videoToVoice(String sourcePath, String sourceType, String targetPath, String formatType,
      String fileType, Integer bitRate, Integer channels, Integer samplingRate)
      throws EncoderException, MalformedURLException {
    File target = new File(targetPath);
    // 音频属性设置
    AudioAttributes audio = new AudioAttributes();
    audio.setCodec(formatType);
    audio.setBitRate(bitRate);
    audio.setChannels(channels);
    audio.setSamplingRate(samplingRate);
    //编码属性设置
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setFormat(fileType);
    attrs.setAudioAttributes(audio);
    Encoder encoder = new Encoder();
    if ("0".equals(sourceType)) {
      encoder.encode(new MultimediaObject(new URL(sourcePath)), target, attrs);
    } else if ("1".equals(sourceType)) {
      encoder.encode(new MultimediaObject(new File(sourcePath)), target, attrs);
    }
  }

}
