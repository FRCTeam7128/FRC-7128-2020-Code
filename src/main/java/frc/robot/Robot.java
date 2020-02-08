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
  double IntakeyLiftySpeed = 0.8;
  double IntakeyDownySpeed = -0.4;
  //Shooty
  double ShootyShootSpeed = 1.0;
  double ShootyMidSpeed = 0.2;
  double ShootyBackSpeed = -0.6;
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
    
    boolean ShooterAButton = XboxShooter.getAButton();
    boolean ShooterYButton = XboxShooter.getYButton();
    boolean ShooterBButton = XboxShooter.getBButton();
    boolean ShooterXButton = XboxShooter.getXButton();
    boolean ShooterBumperRight = XboxShooter.getBumper(Hand.kRight);
    boolean ShooterBumperLeft = XboxShooter.getBumper(Hand.kLeft);

    //Intake
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

    //Intake Lift
    if(ShooterBButton  && !ShooterXButton){
      //In
      Roller.set(IntakeyLiftySpeed);
    } else if(!ShooterBButton  && ShooterXButton){
      //Out
      Roller.set(IntakeyDownySpeed);
    } else if(ShooterBButton  && ShooterXButton){
      //What
      Roller.set(Stop);
      System.out.println("Why?");
    } else {
      //No
      Roller.set(Stop);
    }

    //Shooter
    if(ShooterBumperRight  && !ShooterBumperLeft){
      //Shoot
      Shooter.set(1.0);
    } else if(!ShooterBumperRight  && ShooterBumperLeft){
      //Why
      Shooter.set(-0.6);
      System.out.println("Why would you do that?");
    } else if(ShooterBumperRight  && ShooterBumperLeft){
      //Stop It
      Shooter.set(ShootyMidSpeed);
      System.out.println("Seriously, Don't do it.");
    } else {
      //No
      Shooter.set(Stop);
    }


  }



  @Override
  public void testPeriodic() {
  }
}
