package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class Robot extends TimedRobot {
  //Controllers
  XboxController XboxDrive = new XboxController(0);
  XboxController XboxShooter = new XboxController(1);

  //Controller vars
  double DriveSpeed;
  double DriveSpeedMulti = 1.0;
  double TurnSpeedMulti = 1.0;

  //Drivebase motors
  WPI_VictorSPX DriveL1 = new WPI_VictorSPX(1);
  WPI_VictorSPX DriveL2 = new WPI_VictorSPX(2);
  WPI_VictorSPX DriveR1 = new WPI_VictorSPX(3);
  WPI_VictorSPX DriveR2 = new WPI_VictorSPX(4);
  DifferentialDrive DriveBase = new DifferentialDrive(DriveL1, DriveR1);

  //Intake motors
  WPI_VictorSPX Roller = new WPI_VictorSPX(5);
  WPI_VictorSPX IntakeLift = new WPI_VictorSPX(6);

  //Shooter motors
  WPI_VictorSPX Shooter = new WPI_VictorSPX(7);
  WPI_TalonSRX Indexer = new WPI_TalonSRX(1);

  //Climb motors
  WPI_VictorSPX Hook = new WPI_VictorSPX(8);
  WPI_TalonSRX Winch1 = new WPI_TalonSRX(2);
  WPI_TalonSRX Winch2 = new WPI_TalonSRX(3);


  @Override
  public void robotInit() {
    DriveL2.follow(DriveL1);
    DriveR2.follow(DriveR1);
    Winch2.follow(Winch1);
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
    //Driving
    DriveSpeed = XboxDrive.getTriggerAxis(Hand.kRight) - XboxDrive.getTriggerAxis(Hand.kLeft);
    DriveBase.arcadeDrive(DriveSpeed * DriveSpeedMulti, XboxDrive.getRawAxis(2) * TurnSpeedMulti);
    //Intake
    boolean DriveBumperRight = XboxDrive.getBumper(Hand.kRight);
    boolean DriveBumperLeft = XboxDrive.getBumper(Hand.kLeft);
    
    if(DriveBumperRight = true){
      Roller.set(1.0);
    } else if(DriveBumperLeft = true){
      Roller.set(-1.0);
    }else if(DriveBumperLeft = DriveBumperRight){
      Roller.set(0.0);
    }



  }



  @Override
  public void testPeriodic() {
  }
}
