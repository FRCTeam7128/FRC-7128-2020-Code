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
  //Intakey
  double IntakeySpeed = 0.8;
  double OutakeySpeed = -0.8;
  boolean ShooterAButton;
  boolean ShooterYButton;
  double IntakeyLiftySpeed = 0.8;
  double IntakeyDownySpeed = -0.4;
  boolean ShooterBButton;
  boolean ShooterXButton;
  //Indexy
  double IndexForwardSpeed = 0.4;
  double IndexBackSpeed = -1.0;
  boolean ShooterBumperLeft;
  boolean ShooterBumperRight;
  //Shooty
  double ShooterSpeed;
  //Stop
  double Stop = 0.0;


  //Drivebase motors
  WPI_VictorSPX DriveL1 = new WPI_VictorSPX(1);
  WPI_VictorSPX DriveL2 = new WPI_VictorSPX(2);
  WPI_VictorSPX DriveR1 = new WPI_VictorSPX(3);
  WPI_VictorSPX DriveR2 = new WPI_VictorSPX(4);
  DifferentialDrive DriveBase = new DifferentialDrive(DriveL1, DriveR1);

  //Intake motors
  WPI_TalonSRX Roller = new WPI_TalonSRX(1);
  WPI_VictorSPX IntakeLift = new WPI_VictorSPX(5);

  //Shooter motors
  WPI_TalonSRX Shooter = new WPI_TalonSRX(2);
  WPI_TalonSRX Indexer = new WPI_TalonSRX(3);

  //Climb motors
  WPI_VictorSPX Hook = new WPI_VictorSPX(6);
  WPI_VictorSPX Winch1 = new WPI_VictorSPX(7);
  WPI_VictorSPX Winch2 = new WPI_VictorSPX(8);


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
    
    //Intakey
    ShooterAButton = XboxShooter.getAButton();
    ShooterYButton = XboxShooter.getYButton();
    //Intakey Lifty
    ShooterBButton = XboxShooter.getBButton();
    ShooterXButton = XboxShooter.getXButton();
    //Indexy
    ShooterBumperRight = XboxShooter.getBumper(Hand.kRight);
    ShooterBumperLeft = XboxShooter.getBumper(Hand.kLeft);
    //Shooter
    ShooterSpeed = XboxShooter.getTriggerAxis(Hand.kRight) - XboxShooter.getTriggerAxis(Hand.kLeft);
    Shooter.set(ShooterSpeed);


    //Intakey
    if(ShooterAButton  && !ShooterYButton){
      //In
      Roller.set(IntakeySpeed);
    } else if(!ShooterAButton  && ShooterYButton){
      //Out
      Roller.set(OutakeySpeed);
    } else if(ShooterAButton  && ShooterYButton){
      //What
      Roller.set(Stop);
      System.out.println("No, the Intake can't go in and out at the same time.");
    } else {
      //No
      Roller.set(Stop);
    }

    //Intakey Lifty
    if(ShooterBButton  && !ShooterXButton){
      //In
      IntakeLift.set(IntakeyLiftySpeed);
    } else if(!ShooterBButton  && ShooterXButton){
      //Out
      IntakeLift.set(IntakeyDownySpeed);
    } else if(ShooterBButton  && ShooterXButton){
      //What
      IntakeLift.set(Stop);
      System.out.println("Why?");
    } else {
      //No
      IntakeLift.set(Stop);
    }

    //Indexy
    if(ShooterBumperRight  && !ShooterBumperLeft){
      //Index forward 
      Indexer.set(IndexForwardSpeed);
    } else if(!ShooterBumperRight  && ShooterBumperLeft){
      //Why
      Indexer.set(IndexBackSpeed);
      System.out.println("Why would you do that?");
    } else if(ShooterBumperRight  && ShooterBumperLeft){
      //Stop It
      Indexer.set(Stop);
      System.out.println("Seriously, Stop it");
    } else {
      //No
      Indexer.set(Stop);
    }
    



  }



  @Override
  public void testPeriodic() {
  }
}
