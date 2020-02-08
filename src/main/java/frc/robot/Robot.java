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
    
    boolean DriveBumperRight = XboxDrive.getBumper(Hand.kRight);
    boolean DriveBumperLeft = XboxDrive.getBumper(Hand.kLeft);
    boolean ShooterBumperRight = XboxShooter.getBumper(Hand.kRight);
    boolean ShooterBumperLeft = XboxShooter.getBumper(Hand.kLeft);

    //Intake
    if(DriveBumperRight  && !DriveBumperLeft){
      //In
      Roller.set(1.0);
    } else if(!DriveBumperRight  && DriveBumperLeft){
      //Out
      Roller.set(-1.0);
    } else if(DriveBumperRight  && DriveBumperLeft){
      //What
      Roller.set(0.0);
      System.out.println("No, the Intake can't go in and out at the same time.");
    } else {
      //No
      Roller.set(0.0);
    }

    //Shooter
    if(ShooterBumperRight  && !ShooterBumperLeft){
      //Shoot
      Roller.set(1.0);
    } else if(!ShooterBumperRight  && ShooterBumperLeft){
      //Why
      Roller.set(-1.0);
      System.out.println("Why would you do that?");
    } else if(ShooterBumperRight  && ShooterBumperLeft){
      //Stop It
      Roller.set(0.0);
      System.out.println("");
    } else {
      //No
      Roller.set(0.0);
    }


  }



  @Override
  public void testPeriodic() {
  }
}
