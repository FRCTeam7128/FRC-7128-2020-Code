/**
 * FRC Team 7128 - 2020 FRC Code
*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.wpilibj.Timer;
import com.kauailabs.navx.frc.AHRS;

public class Robot extends TimedRobot {

  //Controllers
  XboxController xboxDrive = new XboxController(0);
  XboxController xboxShooter = new XboxController(1);
  //Controller Variables
  double driveSpeed;
  double turnSpeed;
  double slowDrive = 1;
  double driveSpeedMulti = 1;
  double turnSpeedMulti = 0.7;
  boolean reverseDrive = false;
  //Intake
  double intakeSpeed = 0.6;
  double outakeSpeed = -0.4;
  boolean shooterAButton;
  boolean shooterBButton;
  boolean shooterXButton;
  boolean shooterYButton;
  boolean intakeLimit;
  double intakeUpSpeed = 0.4;
  double intakeDownSpeed = -0.1;
  //Index
  double indexForwardSpeed = -1.0;
  double indexBackSpeed = 0.4;
  boolean shooterBumperLeft;
  boolean shooterBumperRight;
  //Shoot
  double shooterSpeed;
  //Climb
  double shooterLeftStick;
  //Agitator
  double shooterX;
  double shooterY;
  //stop
  double stop = 0.0;

  //Shooter Motors
  WPI_TalonSRX shooter = new WPI_TalonSRX(1);
  WPI_TalonSRX indexer = new WPI_TalonSRX(2);
  //Climb Motors
  WPI_TalonSRX hook = new WPI_TalonSRX(3);
  WPI_VictorSPX winch1 = new WPI_VictorSPX(1);
  WPI_VictorSPX winch2 = new WPI_VictorSPX(2);
  //Drivebase Motors
  WPI_VictorSPX driveL1 = new WPI_VictorSPX(3);
  WPI_VictorSPX driveL2 = new WPI_VictorSPX(4);
  WPI_VictorSPX driveR1 = new WPI_VictorSPX(5);
  WPI_VictorSPX driveR2 = new WPI_VictorSPX(6);
  DifferentialDrive driveBase = new DifferentialDrive(driveL1, driveR1);
  //Intake Motors
  WPI_VictorSPX roller = new WPI_VictorSPX(7);
  WPI_VictorSPX intakeLift = new WPI_VictorSPX(8);
  //Agitator Motor
  Spark agitatorMotor = new Spark(0);
  //Encoders
  Encoder winchEnc = new Encoder(0,1);
  Encoder LEnc = new Encoder(3, 2, false, EncodingType.k4X); //Switched to create correct direction
  Encoder REnc = new Encoder(4, 5, false, EncodingType.k4X);
  Encoder intakeEnc = new Encoder(7,8);
  private static final double CPR = 360;   //Counts per rotation
  private static final double wheelD = 6;  //Wheel size 
  private static final double pulleyD = 1.2; //Pulley Size
  double distanceTrav;
  //Cameras
  UsbCamera Cam0;
  UsbCamera Cam1;
  MjpegServer switchCam;
  //PDP
  PowerDistributionPanel M_PDP = new PowerDistributionPanel();
  //Limit Switch
  DigitalInput limitSwitch = new DigitalInput(10);
  //Timer
  Timer myTimer = new Timer();
  double autoTime;
  double autoStage;
  //NavX
  AHRS NavX = new AHRS();
  //smartDashBoard values
  double WDis;
  double intakeRot;
  double LDis;
  double RDis;
  double port0Current;
  //Initiation
  @Override
  public void robotInit() {
    driveL2.follow(driveL1);
    driveR2.follow(driveR1);
    winch2.follow(winch1);

    winchEnc.reset();
    LEnc.reset();
    REnc.reset();
    intakeEnc.reset();

    winchEnc.setDistancePerPulse(Math.PI*pulleyD/CPR);
    LEnc.setDistancePerPulse(Math.PI*wheelD/CPR);
    REnc.setDistancePerPulse(Math.PI*wheelD/CPR);
    
    Cam0 = CameraServer.getInstance().startAutomaticCapture(0);
    Cam1 = CameraServer.getInstance().startAutomaticCapture(1);
    switchCam = CameraServer.getInstance().addSwitchedCamera("Camera");
    Cam0.setConnectionStrategy(ConnectionStrategy.kAutoManage);
    Cam1.setConnectionStrategy(ConnectionStrategy.kAutoManage);

    myTimer.start();
    autoStage = 0;

    NavX.reset();
  }

  
  //Periodic
  @Override
  public void robotPeriodic() {
    WDis = winchEnc.getDistance();
    intakeRot = intakeEnc.getRaw();
    LDis = LEnc.getDistance();
    RDis = REnc.getDistance();
    port0Current = M_PDP.getCurrent(0);
    distanceTrav = (LDis + RDis) / 2;
    SmartDashboard.putNumber("Winch Distance", WDis);
    SmartDashboard.putNumber("Intake Rotations", intakeRot);
    SmartDashboard.putNumber("Left Distance", LDis);
    SmartDashboard.putNumber("Right Distance", RDis);
    SmartDashboard.putBoolean("Reverse Drive", reverseDrive);
    SmartDashboard.putNumber("Intake Lift Currents", port0Current);
    SmartDashboard.putNumber("POV", xboxShooter.getPOV());
    SmartDashboard.putNumber("distance travelled", distanceTrav);
    SmartDashboard.putNumber("left encoder raw", LEnc.getRaw());
    SmartDashboard.putNumber("right encoder raw", REnc.getRaw());

    autoTime = myTimer.get();
    SmartDashboard.putNumber("auto Time", autoTime);
    SmartDashboard.putNumber("auto Stage", autoStage);

    //NavX
    SmartDashboard.putNumber("NavX Pitch", NavX.getPitch());
    SmartDashboard.putNumber("NavX Roll", NavX.getRoll());
    SmartDashboard.putNumber("NavX Yaw", NavX.getYaw());
    SmartDashboard.putNumber("NavX North?", NavX.getCompassHeading());
  }

  //Auto Initiation
  @Override
  public void autonomousInit() {
    winchEnc.reset();
    LEnc.reset();
    REnc.reset();
    intakeEnc.reset();
    autoStage = 0;
    myTimer.reset();
  }

  //Auto Periodic
  @Override
  public void autonomousPeriodic() {
    autoStage = 10;
    if(autoTime > 0 && autoTime < 2.0 && RDis > -30){
      driveBase.arcadeDrive(-0.5, 0);
      shooter.set(stop);
      indexer.set(stop);
      autoStage = 11;
    }
    else if(autoTime > 2.0 && autoTime < 6.0 && RDis > -60){
      driveBase.arcadeDrive(-0.35, 0);
      autoStage = 12;
    }
    else if(autoTime > 6.0 && autoTime < 7.0){
      driveBase.arcadeDrive(0.0, 0);
      shooter.set(1.0);
      autoStage = 13;
    }
    
    else if(autoTime > 7.0 && autoTime < 9.0){
      shooter.set(1.0);
      indexer.set(indexForwardSpeed);
      autoStage = 14;
    }
    else{
      shooter.set(stop);
      indexer.set(stop);
      autoStage = 15;
    }
  }

  //Teleop Periodic
  @Override
  public void teleopPeriodic() {

    if(xboxDrive.getTriggerAxis(Hand.kLeft) > 0.1){
      if(switchCam.getSource() == Cam0) {
        switchCam.setSource(Cam1);
    } else {
        switchCam.setSource(Cam0);
    }
    }

    //Driving 
    //Slow drive
    if(xboxDrive.getBumper(Hand.kRight)) {
      slowDrive = 2;
    }
    else {
      slowDrive = 1;
    }

    //Reverse drive
    if(xboxDrive.getBumperPressed(Hand.kLeft)){
      if(reverseDrive){
        reverseDrive = false;
      }
      else {
      reverseDrive = true;
      }
    }

    //Speeds
    driveSpeed = xboxDrive.getRawAxis(1) - xboxDrive.getTriggerAxis(Hand.kRight) + xboxDrive.getTriggerAxis(Hand.kLeft);
    turnSpeed = xboxDrive.getRawAxis(4) / slowDrive;

    //Driving
    if(reverseDrive) {
      driveBase.arcadeDrive(driveSpeed * driveSpeedMulti / slowDrive, turnSpeed * turnSpeedMulti);
    }
    else {
      driveBase.arcadeDrive(driveSpeed * -driveSpeedMulti / slowDrive, turnSpeed * turnSpeedMulti);
    }
    


    //Intake Lift
    shooterAButton = xboxShooter.getAButton();
    shooterYButton = xboxShooter.getYButton();

    //Intake
    shooterBButton = xboxShooter.getBButton();
    shooterXButton = xboxShooter.getXButton();



    //Indexer
    shooterBumperRight = xboxShooter.getBumper(Hand.kRight);
    shooterBumperLeft = xboxShooter.getBumper(Hand.kLeft);

    //Shooter
    shooterSpeed = xboxShooter.getTriggerAxis(Hand.kRight) - xboxShooter.getTriggerAxis(Hand.kLeft);
    shooter.set(shooterSpeed);



    //Climb

    if(xboxDrive.getBButton() && !xboxDrive.getXButton() && !xboxDrive.getAButton()){
      //Both Up
     winch1.set(-0.27);
      hook.set(1.0);
    }
    if(xboxDrive.getBButton()){
      winch1.set(0.5);
    }
    if(xboxDrive.getYButton()){
      winch1.set(-0.5);
    }
    if(xboxDrive.getXButton()){
      hook.set(-1.0);
    }
    if(!xboxDrive.getBButton() && !xboxDrive.getXButton() && !xboxDrive.getAButton()){
      winch1.set(0.0);
      hook.set(0.0);
    }

    //Intake
    if(shooterBButton  && !shooterXButton) {
      //In
      roller.set(intakeSpeed);
    } 
    else if(!shooterBButton  && shooterXButton) {
      //Out
      roller.set(outakeSpeed);
    } 
    else if(shooterBButton  && shooterXButton) {
      //What
      roller.set(stop);
      System.out.println("No, the Intake can't go in and out at the same time.");
    } 
    else {
      //No
      roller.set(stop);
    }



    //Intake Lift

    intakeLimit = !limitSwitch.get();

    if(shooterYButton  && !shooterAButton) {
      //Going up
      if(intakeLimit) {
        intakeLift.set(stop);
      }
      else {
        if(intakeEnc.getRaw() < 500){
          intakeLift.set(intakeUpSpeed);
        }
        else{
          intakeLift.set(stop);
        }
      }
    } 
    else if(!shooterYButton  && shooterAButton) {
      //Going Down
      intakeLift.set(intakeDownSpeed);
    } 
    else if(shooterYButton  && shooterAButton) {
      //What
      intakeLift.set(stop);
      System.out.println("Why?");
    } 
    else {
      //Manual Up/Down
      //intakeLift.set(xboxShooter.getRawAxis(5));
      intakeLift.set(stop);
    }



    //Indexer
    if(!shooterBumperRight  && shooterBumperLeft) {
      //Index forward 
      indexer.set(indexBackSpeed);
      System.out.println("Why would you do that?");
    } 
    else if(shooterBumperRight  && !shooterBumperLeft) {
      //Why
      indexer.set(indexForwardSpeed);
      agitatorMotor.set(-1);
    } 
    else if(shooterBumperRight  && shooterBumperLeft) {
      //stop It
      indexer.set(stop);
      System.out.println("Seriously, stop it");
    } 
    else {
      //NooOOOooOOoOOoOoooOOoOOOOOOo
      indexer.set(stop);
    }


    //Agitator
    shooterX = xboxShooter.getRawAxis(0);
    shooterY = -xboxShooter.getRawAxis(1);
    agitatorMotor.set(shooterX);



  }

  //Test Periodic
  @Override
  public void testPeriodic() {

  }
}