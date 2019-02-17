package frc.robot;

public enum activeCargoMechanism {
    lowCargoPickup(false), highCargoPickup(false), lowCargoShoot(false), highCargoShoot(false);

    public boolean isActive;

    activeCargoMechanism(boolean IsActive) {
        isActive = IsActive;
    } 

    
}