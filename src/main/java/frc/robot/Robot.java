package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends TimedRobot {

  //Controllers
  XboxController XboxDrive = new XboxController(0);
  XboxController XboxShooter = new XboxController(1);

  
  //Controller Variables
  double DriveSpeed;
  double TurnSpeed;
  double SlowDrive = 1;
  double DriveSpeedMulti = 1;
  double TurnSpeedMulti = 1;
  boolean ReverseDrive = false;

  //Intakey
  double IntakeySpeed = 0.8;
  double OutakeySpeed = -0.6;
  boolean ShooterAButton;
  boolean ShooterBButton;
  boolean ShooterXButton;
  boolean ShooterYButton;
  double IntakeyLiftySpeed = 0.8;
  double IntakeyDownySpeed = -0.4;

  //Indexy
  double IndexForwardSpeed = 0.4;
  double IndexBackSpeed = -1.0;
  boolean ShooterBumperLeft;
  boolean ShooterBumperRight;

  //Shooty
  double ShooterSpeed;

  //Climby
  double ShooterLeftStick;

  //Stop
  double Stop = 0.0;


  //Shooter Motors
  WPI_TalonSRX Shooter = new WPI_TalonSRX(1);
  WPI_TalonSRX Indexer = new WPI_TalonSRX(2);

  //Climb Motors
  WPI_TalonSRX Hook = new WPI_TalonSRX(3);
  WPI_VictorSPX Winch1 = new WPI_VictorSPX(1);
  WPI_VictorSPX Winch2 = new WPI_VictorSPX(2);

  //Drivebase Motors
  WPI_VictorSPX DriveL1 = new WPI_VictorSPX(3);
  WPI_VictorSPX DriveL2 = new WPI_VictorSPX(4);
  WPI_VictorSPX DriveR1 = new WPI_VictorSPX(5);
  WPI_VictorSPX DriveR2 = new WPI_VictorSPX(6);
  DifferentialDrive DriveBase = new DifferentialDrive(DriveL1, DriveR1);

  //Intake Motors
  WPI_VictorSPX Roller = new WPI_VictorSPX(7);
  WPI_VictorSPX IntakeLift = new WPI_VictorSPX(8);


  //Encodys
  Encoder WinchEnc = new Encoder(0,1);
  Encoder InLtEnc = new Encoder(2,3);
  Encoder LEnc = new Encoder(4,5);
  Encoder REnc = new Encoder(6,7);
  private static final double CPR = 360;   //Counts per rotation
  private static final double WheelD = 6;  //Wheel size 
  private static final double PulleyD = 1.2; //Pulley Size


  @Override
  public void robotInit() {
    DriveL2.follow(DriveL1);
    DriveR2.follow(DriveR1);
    Winch2.follow(Winch1);
    WinchEnc.setDistancePerPulse(Math.PI*PulleyD/CPR);
    LEnc.setDistancePerPulse(Math.PI*WheelD/CPR);
    REnc.setDistancePerPulse(Math.PI*WheelD/CPR);
  }


  @Override
  public void robotPeriodic() {
    double WDis = WinchEnc.getDistance();
    double InLtRot = InLtEnc.getRaw();
    double LDis = LEnc.getDistance();
    double RDis = REnc.getDistance();
    SmartDashboard.putNumber("Winch Distance", WDis);
    SmartDashboard.putNumber("Intake Rotations", InLtRot/188);
    SmartDashboard.putNumber("Left Distance", LDis);
    SmartDashboard.putNumber("Right Distance", RDis);
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopPeriodic() {

    //Drivy
    if(XboxDrive.getBumper(Hand.kRight)) {
      SlowDrive = 2;
    }
    else {
      SlowDrive = 1;
    }

    if(XboxDrive.getBumperPressed(Hand.kLeft)){
      if(ReverseDrive){
        ReverseDrive = false;
      }
      else {
      ReverseDrive = true;
      }
    }

    DriveSpeed = XboxDrive.getRawAxis(1) + XboxDrive.getTriggerAxis(Hand.kRight) - XboxDrive.getTriggerAxis(Hand.kLeft);
    TurnSpeed = XboxDrive.getRawAxis(4) / SlowDrive;

    if(ReverseDrive) {
      DriveBase.arcadeDrive(DriveSpeed * DriveSpeedMulti / SlowDrive, TurnSpeed * -TurnSpeedMulti);
    }
    else {
      DriveBase.arcadeDrive(DriveSpeed * -DriveSpeedMulti / SlowDrive, TurnSpeed * TurnSpeedMulti);
    }
    

    //Intakey Lifty
    ShooterAButton = XboxShooter.getAButton();
    ShooterYButton = XboxShooter.getYButton();

    //Intakey
    ShooterBButton = XboxShooter.getBButton();
    ShooterXButton = XboxShooter.getXButton();

    //Indexy
    ShooterBumperRight = XboxShooter.getBumper(Hand.kRight);
    ShooterBumperLeft = XboxShooter.getBumper(Hand.kLeft);

    //Shooty
    ShooterSpeed = XboxShooter.getTriggerAxis(Hand.kRight) - XboxShooter.getTriggerAxis(Hand.kLeft);
    Shooter.set(ShooterSpeed);

    //Climby
    ShooterLeftStick = XboxShooter.getRawAxis(1);
    Winch1.set(ShooterLeftStick / -2);
    Hook.set(ShooterLeftStick);


    //Intakey
    if(ShooterBButton  && !ShooterXButton) {
      //In
      Roller.set(IntakeySpeed);
    } 
    else if(!ShooterBButton  && ShooterXButton) {
      //Out
      Roller.set(OutakeySpeed);
    } 
    else if(ShooterBButton  && ShooterXButton) {
      //What
      Roller.set(Stop);
      System.out.println("No, the Intake can't go in and out at the same time.");
    } 
    else {
      //No
      Roller.set(Stop);
    }


    //Intakey Lifty
    if(ShooterYButton  && !ShooterAButton) {
      //In
      IntakeLift.set(IntakeyLiftySpeed);
    } 
    else if(!ShooterYButton  && ShooterAButton) {
      //Out
      IntakeLift.set(IntakeyDownySpeed);
    } 
    else if(ShooterYButton  && ShooterAButton) {
      //What
      IntakeLift.set(Stop);
      System.out.println("Why?");
    } 
    else {
      //No
      IntakeLift.set(Stop);
    }


    //Indexy
    if(!ShooterBumperRight  && ShooterBumperLeft) {
      //Index forward 
      Indexer.set(IndexForwardSpeed);
    } 
    else if(ShooterBumperRight  && !ShooterBumperLeft) {
      //Why
      Indexer.set(IndexBackSpeed);
      System.out.println("Why would you do that?");
    } 
    else if(ShooterBumperRight  && ShooterBumperLeft) {
      //Stop It
      Indexer.set(Stop);
      System.out.println("Seriously, Stop it");
    } 
    else {
      //No
      Indexer.set(Stop);
    }

  }


  @Override
  public void testPeriodic() {

  }
}