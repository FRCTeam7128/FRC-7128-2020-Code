/**
 * FRC Team 7128 - 2020 FRC Code
*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;


public class Robot extends TimedRobot {

  //Controllers
  XboxController xboxDrive = new XboxController(0);
  XboxController xboxShooter = new XboxController(1);


  //Controller Variables
  double driveSpeed;
  double turnSpeed;
  double slowDrive = 1;
  double driveSpeedMulti = 1;
  double turnSpeedMulti = 0.5;
  boolean reverseDrive = false;

  //Intake
  double intakeSpeed = 0.6;
  double outakeSpeed = -0.4;
  boolean shooterAButton;
  boolean shooterBButton;
  boolean shooterXButton;
  boolean shooterYButton;
  double intakeUpSpeed = 0.5;
  double intakeDownSpeed = -0.1;

  //Index
  double indexForwardSpeed = 0.4;
  double indexBackSpeed = -1.0;
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
  Encoder intakeEnc = new Encoder(2,3);
  Encoder LEnc = new Encoder(4,5);
  Encoder REnc = new Encoder(6,7);
  Encoder hookEnc = new Encoder(8,9);
  private static final double CPR = 360;   //Counts per rotation
  private static final double wheelD = 6;  //Wheel size 
  private static final double pulleyD = 1.2; //Pulley Size
  private static final double hookPulleyD = 0; //hook Pulley Size

  //PDP
  PowerDistributionPanel M_PDP = new PowerDistributionPanel();

  @Override
  public void robotInit() {
    driveL2.follow(driveL1);
    driveR2.follow(driveR1);
    winch2.follow(winch1);

    winchEnc.reset();
    LEnc.reset();
    REnc.reset();
    intakeEnc.reset();
    hookEnc.reset();

    winchEnc.setDistancePerPulse(Math.PI*pulleyD/CPR);
    LEnc.setDistancePerPulse(Math.PI*wheelD/CPR);
    REnc.setDistancePerPulse(Math.PI*wheelD/CPR);
    hookEnc.setDistancePerPulse(Math.PI*hookPulleyD/CPR);
  }


  @Override
  public void robotPeriodic() {
    double WDis = winchEnc.getDistance();
    double intakeRot = intakeEnc.getRaw();
    double LDis = LEnc.getDistance();
    double RDis = REnc.getDistance();
    double hookDis = hookEnc.getDistance();
    double port0Current = M_PDP.getCurrent(0);

    SmartDashboard.putNumber("Winch Distance", WDis);
    SmartDashboard.putNumber("Intake Rotations", intakeRot);
    SmartDashboard.putNumber("Left Distance", LDis);
    SmartDashboard.putNumber("Right Distance", RDis);
    SmartDashboard.putNumber("hook Distance", hookDis);
    SmartDashboard.putBoolean("Reverse Drive", reverseDrive);
    SmartDashboard.putNumber("Intake Lift Currents", port0Current);
    SmartDashboard.putNumber("POV",xboxShooter.getPOV());
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
    }else if(!xboxDrive.getBButton() && xboxDrive.getXButton() && !xboxDrive.getAButton()){
      //Climb
     winch1.set(0.5);
    }else if(!xboxDrive.getBButton() && !xboxDrive.getXButton() && xboxDrive.getAButton()){
      //hook down
      hook.set(-1.0);
    }else if(!xboxDrive.getBButton() && !xboxDrive.getXButton() && !xboxDrive.getAButton()){
      //stop
     winch1.set(0.0);
      hook.set(0.0);
    }else if(!xboxDrive.getBButton() && !xboxDrive.getXButton() && !xboxDrive.getAButton()){
      //
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
    if(shooterYButton  && !shooterAButton) {
      //going up
      intakeLift.set(intakeUpSpeed);
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
      indexer.set(indexForwardSpeed);
    } 
    else if(shooterBumperRight  && !shooterBumperLeft) {
      //Why
      indexer.set(indexBackSpeed);
      System.out.println("Why would you do that?");
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

    if(shooterX + shooterY > 0) {
      agitatorMotor.set(1);
    }
    else if(shooterX + shooterY < 0) {
      agitatorMotor.set(-1);
    }
    else {
      agitatorMotor.stopMotor();
    }


  }


  @Override
  public void testPeriodic() {

  }
}