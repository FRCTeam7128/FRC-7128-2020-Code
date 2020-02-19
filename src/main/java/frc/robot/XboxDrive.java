


package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;



public class XboxDrive extends Robot {
    
    boolean ReverseDrive = false;

    public XboxDrive () {

    }

    public void use() {



        
        //Drivy
        if(XboxDriving.getBumper(Hand.kRight)) {
            SlowDrive = 2;
        }
        else {
            SlowDrive = 1;
        }
    
        if(XboxDriving.getBumperPressed(Hand.kLeft)){
            if(ReverseDrive){
            ReverseDrive = false;
            }
            else {
            ReverseDrive = true;
            }
        }
    
        DriveSpeed = XboxDriving.getRawAxis(1) + XboxDriving.getTriggerAxis(Hand.kRight) - XboxDriving.getTriggerAxis(Hand.kLeft);
        TurnSpeed = XboxDriving.getRawAxis(4) / SlowDrive;
    
        if(ReverseDrive) {
            DriveBase.arcadeDrive(DriveSpeed * DriveSpeedMulti / SlowDrive, TurnSpeed * -TurnSpeedMulti);
        }
        else {
            DriveBase.arcadeDrive(DriveSpeed * -DriveSpeedMulti / SlowDrive, TurnSpeed * TurnSpeedMulti);
        }
    }
}