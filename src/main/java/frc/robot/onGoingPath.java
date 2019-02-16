package frc.robot;

public enum onGoingPath {
    nearCargo(260.65, 90), middleCargo(282.4, 90), farCargo(304.15, 90), rocketShip(229.13, 270);

    double gyroAngle;
    double lidarDistance;
    onGoingPath(double distance, double angle) {
        gyroAngle = angle;
        lidarDistance = distance;
    }
}