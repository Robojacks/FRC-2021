/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.shooter.ChangePosition;
import frc.robot.shooter.Shooter;
import frc.robot.vision.Limelight;

import static frc.robot.Constants.*;
import static frc.robot.Gains.*;

/**
 * Mainly for logging values that need adjustment
 */
public class Update {
  private Shooter m_shooter;
  private Limelight limelight;
  private ChangePosition goalMover;

  // Starting positions
  private final Pose2d left = new Pose2d(-1, 0, Rotation2d.fromDegrees(0));
  private final Pose2d center = new Pose2d(0, 0, Rotation2d.fromDegrees(0));
  private final Pose2d right = new Pose2d(1, 0, Rotation2d.fromDegrees(0));

  private static final SendableChooser choosePosition = new SendableChooser<Pose2d>();

  public Update(Shooter shooter, Limelight vision, ChangePosition shooterPose) {
    m_shooter = shooter;
    limelight = vision;
    goalMover = shooterPose;

    choosePosition.setDefaultOption("Center", center);
    choosePosition.addOption("Left", left);
    choosePosition.addOption("Right", right);
    SmartDashboard.putData("Starting Position", choosePosition);

    // Display PID values (angle)
    SmartDashboard.putNumber("P value(angle)", angleCorrection.kP);
    SmartDashboard.putNumber("I value(angle)", angleCorrection.kI);
    SmartDashboard.putNumber("D value(angle)", angleCorrection.kD);

    // Display left and right shooter velocities
    SmartDashboard.putNumber("Shooter RPM", m_shooter.getVelocity());
    
  }
  
  public static Pose2d getStartingPose() {
    final Pose2d position = (Pose2d) choosePosition.getSelected();
    return position;
  }

  public void periodic() {
    // Update left and right shooter velocities
    SmartDashboard.putNumber("Shooter RPM", m_shooter.getVelocity());
    SmartDashboard.putNumber("Distance", limelight.getTargetDistanceMeasured(centerOfBallTargetHeight, cameraAngle));
    
    // Robot Positioning
    SmartDashboard.putBoolean("Is in Shooting Pose", !goalMover.isCollectingPose());

    // Change PID values for angle correction
    if (angleCorrection.kP != SmartDashboard.getNumber("P value(angle)", angleCorrection.kP))  {
      angleCorrection.kP = SmartDashboard.getNumber("P value(angle)", angleCorrection.kP);
    }

    if (angleCorrection.kI != SmartDashboard.getNumber("I value(angle)", angleCorrection.kI))  {
      angleCorrection.kI = SmartDashboard.getNumber("I value(angle)", angleCorrection.kI);
    }

    if (angleCorrection.kD != SmartDashboard.getNumber("D value(angle)", angleCorrection.kD))  {
      angleCorrection.kD = SmartDashboard.getNumber("D value(angle)", angleCorrection.kD);
    }
  }
}
