/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends TimedRobot {
  //Controllers
  XboxController XboxDrive = new XboxController(0);
  XboxController XboxShooter = new XboxController(1);

  //Drivebase motors
  VictorSPX DriveL1 = new VictorSPX(1);
  VictorSPX DriveL2 = new VictorSPX(2);
  VictorSPX DriveR1 = new VictorSPX(3);
  VictorSPX DriveR2 = new VictorSPX(4);
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
  }


  @Override
  public void testPeriodic() {
  }
}
