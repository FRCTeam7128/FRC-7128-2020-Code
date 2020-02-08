/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot {
  //Controllers
  XboxController XboxDrive = new XboxController(0);
  XboxController XboxShooter = new XboxController(1);
  //Controller vars
  double DriveSpeed;

  //Drivebase motors
  WPI_VictorSPX DriveL1 = new WPI_VictorSPX(1);
  WPI_VictorSPX DriveL2 = new WPI_VictorSPX(2);
  WPI_VictorSPX DriveR1 = new WPI_VictorSPX(3);
  WPI_VictorSPX DriveR2 = new WPI_VictorSPX(4);
  DifferentialDrive DriveBase = new DifferentialDrive(DriveL1, DriveR1);
  //Intake motors

  //Shooter motors

  //Climb motors


  @Override
  public void robotInit() {
    DriveL2.follow(DriveL1);
    DriveR2.follow(DriveR1);
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {
  }



  @Override
  public void teleopPeriodic() {
    DriveSpeed = XboxDrive.getTriggerAxis(Hand.kRight) - XboxDrive.getTriggerAxis(Hand.kLeft);
    DriveBase.arcadeDrive(DriveSpeed, XboxDrive.getRawAxis(2));

  }



  @Override
  public void testPeriodic() {
  }
}
