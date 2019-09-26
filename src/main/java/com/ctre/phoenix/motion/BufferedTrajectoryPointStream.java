package com.ctre.phoenix.motion;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.TrajectoryPoint;

/**
 * Stream of trajectory points for Talon/Victor motion profiling.
 */
public class BufferedTrajectoryPointStream{
    private long m_handle;

    public BufferedTrajectoryPointStream()
    {
        m_handle = 0;
    }
	/**
	 * Clear all trajectory points.
	 * @return nonzero error code if operation fails.
	 */
    public ErrorCode Clear()
    {
        return ErrorCode.valueOf(0);
    }
	/**
	 * Write a single trajectory point into the buffer.
 	 * @param trajPt 	Trajectory point to write.
	 * @return nonzero error code if operation fails.
	 */
    public ErrorCode Write(TrajectoryPoint trajPt)
    {
        int status = 0;
        return ErrorCode.valueOf(status);
    }
	/**
	 * Writes an array of trajectory point into the buffer.
 	 * @param trajPts 	Array of trajectory points to write.
 	 * @param trajPtCount  Number of points to write.  This is capped against array length.
	 * @return nonzero error code if operation fails.
	 */
    public ErrorCode Write(TrajectoryPoint[] trajPts, int trajPtCount)
    {
        ErrorCode retval = ErrorCode.OK;

        if(trajPtCount > trajPts.length){trajPtCount = trajPts.length;}

        for (int i = 0; i < trajPtCount; ++i) {
            /* insert next pt */
            ErrorCode er = Write(trajPts[i]);
            /* save first nonzero error code */
            if (retval == ErrorCode.OK) { retval = er; }
        }

        return retval;
    }
	/**
 	 * Writes an array of trajectory point into the buffer.
 	 * @param trajPts 	Array of trajectory points to write.
 	 * @return nonzero error code if operation fails.
	 */
    public ErrorCode Write(TrajectoryPoint[] trajPts){
        return Write(trajPts, trajPts.length);
    }

    /**
     * @return handle of object
     */
    public long getHandle(){return m_handle;}
}