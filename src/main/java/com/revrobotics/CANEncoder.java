/*
 * Copyright (c) 2018-2019 REV Robotics
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of REV Robotics nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.revrobotics;

import java.util.concurrent.atomic.AtomicBoolean;

import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.jni.CANSparkMaxJNI;

public class CANEncoder extends CANSensor {
    private int m_cpr;
	
	  private AtomicBoolean encInitialized = new AtomicBoolean(false);

    /**
     * Constructs a CANPIDController.
     *
     * @param device The Spark Max to which the encoder is attached.
     * @param sensorType The encoder type for the motor: kHallEffect or kQuadrature
     * @param cpr The counts per revolution of the encoder
     */
    public CANEncoder(CANSparkMax device, EncoderType sensorType, int cpr) {
      super(device);
      if (!encInitialized.get() || m_cpr != cpr) { 
        encInitialized.set(true);
        m_cpr = cpr;
        CANError.fromInt(CANSparkMaxJNI.c_SparkMax_SetSensorType(m_device.m_sparkMax, sensorType.value));
        if (!(sensorType == EncoderType.kHallSensor || m_cpr == 0)) {
          CANError.fromInt(CANSparkMaxJNI.c_SparkMax_SetCPR(m_device.m_sparkMax, cpr));
        }
      }
    }

    /**
     * Constructs a CANPIDController.
     *
     * @param device The Spark Max to which the encoder is attached.
     */
    public CANEncoder(CANSparkMax device) {
      super(device);
      CANError.fromInt(CANSparkMaxJNI.c_SparkMax_SetSensorType(m_device.m_sparkMax, EncoderType.kHallSensor.value));
    }

    /**
     * Get the position of the motor. This returns the native units
     * of 'rotations' by default, and can be changed by a scale factor
     * using setPositionConversionFactor().
     *
     * @return Number of rotations of the motor
     *
     */
    public double getPosition() {
      return (double)CANSparkMaxJNI.c_SparkMax_GetEncoderPosition(m_device.m_sparkMax);
    }

    /**
     * Get the velocity of the motor. This returns the native units
     * of 'RPM' by default, and can be changed by a scale factor
     * using setVelocityConversionFactor().
     *
     * @return Number the RPM of the motor
     *
     */
    public double getVelocity() {
      return (double)CANSparkMaxJNI.c_SparkMax_GetEncoderVelocity(m_device.m_sparkMax);
    }

    /**
     * Set the position of the encoder.  By default the units
     * are 'rotations' and can be changed by a scale factor
     * using setPositionConversionFactor().
     *
     * @param position Number of rotations of the motor
     *
     * @return CANError Set to CANError.kOK if successful
     */
    public CANError setPosition(double position) {
      return m_device.setEncPosition(position);
    }

    /**
     * Set the conversion factor for position of the encoder.
     * Multiplied by the native output units to give you position.
     *
     * @param factor The conversion factor to multiply the native units by
     *
     * @return CANError Set to CANError.kOK if successful
     */
    public CANError setPositionConversionFactor(double factor) {
      return CANError.fromInt(CANSparkMaxJNI.c_SparkMax_SetPositionConversionFactor(m_device.m_sparkMax, (float)factor));
    }

    /**
     * Set the conversion factor for velocity of the encoder.
     * Multiplied by the native output units to give you velocity
     *
     * @param factor The conversion factor to multiply the native units by
     *
     * @return CANError Set to CANError.kOK if successful
     */
    public CANError setVelocityConversionFactor(double factor) {
      return CANError.fromInt(CANSparkMaxJNI.c_SparkMax_SetVelocityConversionFactor(m_device.m_sparkMax, (float)factor));
    }

    /**
     * Get the conversion factor for position of the encoder.
     * Multiplied by the native output units to give you position
     *
     * @return The conversion factor for position
     */
    public double getPositionConversionFactor() {
      return (double)CANSparkMaxJNI.c_SparkMax_GetPositionConversionFactor(m_device.m_sparkMax);
    }

    /**
     * Get the conversion factor for velocity of the encoder.
     * Multiplied by the native output units to give you velocity
     *
     * @return The conversion factor for velocity
     */
    public double getVelocityConversionFactor() {
      return (double)CANSparkMaxJNI.c_SparkMax_GetVelocityConversionFactor(m_device.m_sparkMax);
    }
    
    /**
     * Set the average sampling depth for a quadrature encoder. This value
     * sets the number of samples in the average for velocity readings. This
     * can be any value from 1 to 64.
     * 
     * When the SparkMax controller is in Brushless mode, this 
     * will not change any behavior.
     * 
     * @param depth The average sampling depth between 1 and 64 (default)
     * 
     * @return CANError.kOK if successful
     */
    public CANError setAverageDepth(int depth) {
      return CANError.fromInt(CANSparkMaxJNI.c_SparkMax_SetAverageDepth(m_device.m_sparkMax, depth));
    }

    /**
     * Get the averafe sampling depth for a quadrature encoder. 
     * 
     * @return The average sampling depth
     */
    public int getAverageDepth() {
      return (int)CANSparkMaxJNI.c_SparkMax_GetAverageDepth(m_device.m_sparkMax);
    }

    /**
     * Set the measurement period for velocity measurements of a quadrature encoder.
     * When the SparkMax controller is in Brushless mode, this will not
     * change any behavior.
     * 
     * The basic formula to calculate velocity is change in positon / change in time.
     * This parameter sets the change in time for measurement.
     * 
     * @param period_us Measurement period in milliseconds. This number may be
     * between 1 and 100 (default).
     * 
     * @return CANError.kOK if successful
     */
    public CANError setMeasurementPeriod(int period_us) {
      return CANError.fromInt(CANSparkMaxJNI.c_SparkMax_SetMeasurementPeriod(m_device.m_sparkMax, period_us));
    }

    /**
     * Get the number of samples for reading from a quadrature encoder.
     * 
     * @return Number of samples
     */ 
    public int getMeasurementPeriod() {
      return (int)CANSparkMaxJNI.c_SparkMax_GetMeasurementPeriod(m_device.m_sparkMax);
    }

    /**
     * Get the counts per revolution of the quadrature encoder.
     * 
     * @return Counts per revolution
     */
    public int getCPR() {
      return (int)CANSparkMaxJNI.c_SparkMax_GetCPR(m_device.m_sparkMax);
    }

    @Override
	  protected int getID() {
		  return (m_device.getInitialMotorType() == MotorType.kBrushless) ? EncoderType.kHallSensor.value : EncoderType.kQuadrature.value;
    }
    
    @Override
    public CANError setInverted(boolean inverted) {
      if (m_device.getInitialMotorType() == MotorType.kBrushless) {
        throw new IllegalArgumentException("Not available in Brushless Mode");
      }
      return CANError.fromInt(CANSparkMaxJNI.c_SparkMax_SetEncoderInverted(m_device.m_sparkMax, inverted));
    }

    @Override
    public boolean getInverted() {
      return (boolean)CANSparkMaxJNI.c_SparkMax_GetEncoderInverted(m_device.m_sparkMax);
    }
}
