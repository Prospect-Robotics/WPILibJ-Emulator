/*----------------------------------------------------------------------------*/
/* Copyright (c) 2016-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.cscore;

/**
 * A source that represents a MJPEG-over-HTTP (IP) camera.
 */
public class HttpCamera extends VideoCamera {
  public enum HttpCameraKind {
    kUnknown(0), kMJPGStreamer(1), kCSCore(2), kAxis(3);

    @SuppressWarnings("MemberName")
    private final int value;

    HttpCameraKind(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  /**
   * Convert from the numerical representation of kind to an enum type.
   *
   * @param kind The numerical representation of kind
   * @return The kind
   */
  public static HttpCameraKind getHttpCameraKindFromInt(int kind) {
    switch (kind) {
      case 1: return HttpCameraKind.kMJPGStreamer;
      case 2: return HttpCameraKind.kCSCore;
      case 3: return HttpCameraKind.kAxis;
      default: return HttpCameraKind.kUnknown;
    }
  }

  /**
   * Create a source for a MJPEG-over-HTTP (IP) camera.
   *
   * @param name Source name (arbitrary unique identifier)
   * @param url Camera URL (e.g. "http://10.x.y.11/video/stream.mjpg")
   */
  public HttpCamera(String name, String url) {
    super(0/*CameraServerJNI.createHttpCamera(name, url, HttpCameraKind.kUnknown.getValue())*/);
  }

  /**
   * Create a source for a MJPEG-over-HTTP (IP) camera.
   *
   * @param name Source name (arbitrary unique identifier)
   * @param url Camera URL (e.g. "http://10.x.y.11/video/stream.mjpg")
   * @param kind Camera kind (e.g. kAxis)
   */
  public HttpCamera(String name, String url, HttpCameraKind kind) {
    super(0/*CameraServerJNI.createHttpCamera(name, url, kind.getValue())*/);
  }

  /**
   * Create a source for a MJPEG-over-HTTP (IP) camera.
   *
   * @param name Source name (arbitrary unique identifier)
   * @param urls Array of Camera URLs
   */
  public HttpCamera(String name, String[] urls) {
    super(0/*CameraServerJNI.createHttpCameraMulti(name, urls, HttpCameraKind.kUnknown.getValue())*/);
  }

  /**
   * Create a source for a MJPEG-over-HTTP (IP) camera.
   *
   * @param name Source name (arbitrary unique identifier)
   * @param urls Array of Camera URLs
   * @param kind Camera kind (e.g. kAxis)
   */
  public HttpCamera(String name, String[] urls, HttpCameraKind kind) {
    super(0/*CameraServerJNI.createHttpCameraMulti(name, urls, kind.getValue())*/);
  }

  /**
   * Get the kind of HTTP camera.
   *
   * <p>Autodetection can result in returning a different value than the camera
   * was created with.
   */
  public HttpCameraKind getHttpCameraKind() {
    return getHttpCameraKindFromInt(0/*CameraServerJNI.getHttpCameraKind(m_handle)*/);
  }

  /**
   * Change the URLs used to connect to the camera.
   */
  public void setUrls(String[] urls) {
    //CameraServerJNI.setHttpCameraUrls(m_handle, urls);
  }

  /**
   * Get the URLs used to connect to the camera.
   */
  public String[] getUrls() {
	  return new String[0]; // CameraServerJNI.getHttpCameraUrls(m_handle);
  }
}
