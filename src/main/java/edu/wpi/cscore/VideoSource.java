/*----------------------------------------------------------------------------*/
/* Copyright (c) 2016-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.cscore;

/**
 * A source for video that provides a sequence of frames.  Each frame may
 * consist of multiple images (e.g. from a stereo or depth camera); these
 * are called channels.
 */
public class VideoSource implements AutoCloseable {
  public enum Kind {
    kUnknown(0), kUsb(1), kHttp(2), kCv(4);

    @SuppressWarnings("MemberName")
    private final int value;

    Kind(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  /**
   * Connection strategy.
   */
  public enum ConnectionStrategy {
    /**
     * Automatically connect or disconnect based on whether any sinks are
     * connected to this source.  This is the default behavior.
     */
    kAutoManage(0),

    /**
     * Try to keep the connection open regardless of whether any sinks are
     * connected.
     */
    kKeepOpen(1),

    /**
     * Never open the connection.  If this is set when the connection is open,
     * close the connection.
     */
    kForceClose(2);

    @SuppressWarnings("MemberName")
    private final int value;

    ConnectionStrategy(int value) {
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
  public static Kind getKindFromInt(int kind) {
    switch (kind) {
      case 1: return Kind.kUsb;
      case 2: return Kind.kHttp;
      case 4: return Kind.kCv;
      default: return Kind.kUnknown;
    }
  }

  protected VideoSource(int handle) {
    m_handle = handle;
  }

  @Deprecated
  public void free() {
    close();
  }

  @Override
  public synchronized void close() {
    if (m_handle != 0) {
      //CameraServerJNI.releaseSource(m_handle);
    }
    m_handle = 0;
  }

  public boolean isValid() {
    return m_handle != 0;
  }

  public int getHandle() {
    return m_handle;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null) {
      return false;
    }
    if (getClass() != other.getClass()) {
      return false;
    }
    VideoSource source = (VideoSource) other;
    return m_handle == source.m_handle;
  }

  @Override
  public int hashCode() {
    return m_handle;
  }

  /**
   * Get the kind of the source.
   */
  public Kind getKind() {
    return Kind.kUnknown; //getKindFromInt(CameraServerJNI.getSourceKind(m_handle));
  }

  /**
   * Get the name of the source.  The name is an arbitrary identifier
   * provided when the source is created, and should be unique.
   */
  public String getName() {
    return "name"; //CameraServerJNI.getSourceName(m_handle);
  }

  /**
   * Get the source description.  This is source-kind specific.
   */
  public String getDescription() {
    return "desc"; //CameraServerJNI.getSourceDescription(m_handle);
  }

  /**
   * Get the last time a frame was captured.
   * @return Time in 1 us increments.
   */
  public long getLastFrameTime() {
    return 0; //CameraServerJNI.getSourceLastFrameTime(m_handle);
  }

  /**
   * Sets the connection strategy.  By default, the source will automatically
   * connect or disconnect based on whether any sinks are connected.
   *
   * <p>This function is non-blocking; look for either a connection open or
   * close event or call {@link #isConnected()} to determine the connection
   * state.
   *
   * @param strategy connection strategy (auto, keep open, or force close)
   */
  public void setConnectionStrategy(ConnectionStrategy strategy) {
    //CameraServerJNI.setSourceConnectionStrategy(m_handle, strategy.getValue());
  }

  /**
   * Returns if the source currently connected to whatever is providing the images.
   */
  public boolean isConnected() {
    return false; //CameraServerJNI.isSourceConnected(m_handle);
  }

  /**
   * Gets source enable status.  This is determined with a combination of
   * connection strategy and the number of sinks connected.
   *
   * @return True if enabled, false otherwise.
   */
  public boolean isEnabled() {
    return false; //CameraServerJNI.isSourceEnabled(m_handle);
  }

  /**
   * Get a property.
   *
   * @param name Property name
   * @return Property contents (of kind Property::kNone if no property with
   *         the given name exists)
   */
  public VideoProperty getProperty(String name) {
    return new VideoProperty(0 /*CameraServerJNI.getSourceProperty(m_handle, name)*/);
  }

  /**
   * Enumerate all properties of this source.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public VideoProperty[] enumerateProperties() {
    int[] handles = new int[0]; //CameraServerJNI.enumerateSourceProperties(m_handle);
    VideoProperty[] rv = new VideoProperty[handles.length];
    for (int i = 0; i < handles.length; i++) {
      rv[i] = new VideoProperty(handles[i]);
    }
    return rv;
  }

  /**
   * Get the current video mode.
   */
  public VideoMode getVideoMode() {
    return null; //CameraServerJNI.getSourceVideoMode(m_handle);
  }

  /**
   * Set the video mode.
   * @param mode Video mode
   */
  public boolean setVideoMode(VideoMode mode) {
    return false; /*CameraServerJNI.setSourceVideoMode(m_handle,
        mode.pixelFormat.getValue(),
        mode.width,
        mode.height,
        mode.fps); */
  }

  /**
   * Set the video mode.
   *
   * @param pixelFormat desired pixel format
   * @param width desired width
   * @param height desired height
   * @param fps desired FPS
   * @return True if set successfully
   */
  public boolean setVideoMode(VideoMode.PixelFormat pixelFormat, int width, int height, int fps) {
    return true; //CameraServerJNI.setSourceVideoMode(m_handle, pixelFormat.getValue(), width, height, fps);
  }

  /**
   * Set the pixel format.
   *
   * @param pixelFormat desired pixel format
   * @return True if set successfully
   */
  public boolean setPixelFormat(VideoMode.PixelFormat pixelFormat) {
    return true; //CameraServerJNI.setSourcePixelFormat(m_handle, pixelFormat.getValue());
  }

  /**
   * Set the resolution.
   *
   * @param width desired width
   * @param height desired height
   * @return True if set successfully
   */
  public boolean setResolution(int width, int height) {
    return true; //CameraServerJNI.setSourceResolution(m_handle, width, height);
  }

  /**
   * Set the frames per second (FPS).
   *
   * @param fps desired FPS
   * @return True if set successfully
   */
  public boolean setFPS(int fps) {
    return true; //CameraServerJNI.setSourceFPS(m_handle, fps);
  }

  /**
   * Set video mode and properties from a JSON configuration string.
   *
   * <p>The format of the JSON input is:
   *
   * <pre>
   * {
   *     "pixel format": "MJPEG", "YUYV", etc
   *     "width": video mode width
   *     "height": video mode height
   *     "fps": video mode fps
   *     "brightness": percentage brightness
   *     "white balance": "auto", "hold", or value
   *     "exposure": "auto", "hold", or value
   *     "properties": [
   *         {
   *             "name": property name
   *             "value": property value
   *         }
   *     ]
   * }
   * </pre>
   *
   * @param config configuration
   * @return True if set successfully
   */
  public boolean setConfigJson(String config) {
    return true; //CameraServerJNI.setSourceConfigJson(m_handle, config);
  }

  /**
   * Get a JSON configuration string.
   *
   * @return JSON configuration string
   */
  public String getConfigJson() {
    return ""; //CameraServerJNI.getSourceConfigJson(m_handle);
  }

  /**
   * Get the actual FPS.
   *
   * <p>CameraServerJNI#setTelemetryPeriod() must be called for this to be valid
   * (throws VisionException if telemetry is not enabled).
   *
   * @return Actual FPS averaged over the telemetry period.
   */
  public double getActualFPS() {
    return 60; /* CameraServerJNI.getTelemetryAverageValue(m_handle,
        CameraServerJNI.TelemetryKind.kSourceFramesReceived); */
  }

  /**
   * Get the data rate (in bytes per second).
   *
   * <p>CameraServerJNI#setTelemetryPeriod() must be called for this to be valid
   * (throws VisionException if telemetry is not enabled).
   *
   * @return Data rate averaged over the telemetry period.
   */
  public double getActualDataRate() {
    return 1e6; /*CameraServerJNI.getTelemetryAverageValue(m_handle,
        CameraServerJNI.TelemetryKind.kSourceBytesReceived);*/
  }

  /**
   * Enumerate all known video modes for this source.
   */
  public VideoMode[] enumerateVideoModes() {
    return new VideoMode[0]; //CameraServerJNI.enumerateSourceVideoModes(m_handle);
  }

  /**
   * Enumerate all sinks connected to this source.
   *
   * @return Vector of sinks.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public VideoSink[] enumerateSinks() {
    int[] handles = new int[0]; //CameraServerJNI.enumerateSourceSinks(m_handle);
    VideoSink[] rv = new VideoSink[handles.length];
    for (int i = 0; i < handles.length; i++) {
      rv[i] = new VideoSink(handles[i]);
    }
    return rv;
  }

  /**
   * Enumerate all existing sources.
   *
   * @return Vector of sources.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public static VideoSource[] enumerateSources() {
    int[] handles = new int[0]; //CameraServerJNI.enumerateSources();
    VideoSource[] rv = new VideoSource[handles.length];
    for (int i = 0; i < handles.length; i++) {
      rv[i] = new VideoSource(handles[i]);
    }
    return rv;
  }

  protected int m_handle;
}
