enum DoorState {
    OPEN,
    CLOSED,
    LOCKED
}

class IllegalStateTransitionException extends RuntimeException {

    IllegalStateTransitionException(String msg) {
        super(msg);
    }
}

class VaultDoor {

    private DoorState state = DoorState.OPEN;

    void closeDoor() {
        state = DoorState.CLOSED;
        System.out.println("Door Closed");
    }

    void lockDoor() {

        if (state != DoorState.CLOSED) {
            throw new IllegalStateTransitionException("Close Door First");
        }

        state = DoorState.LOCKED;
        System.out.println("Door Locked");
    }

    void unlockDoor() {

        if (state == DoorState.LOCKED) {
            state = DoorState.CLOSED;
        }

        System.out.println("Door Unlocked");
    }
}

public class Ques3 {

    public static void main(String[] args) {

        VaultDoor d = new VaultDoor();

        try {
            d.lockDoor();
        } catch (IllegalStateTransitionException e) {
            System.out.println(e.getMessage());
        }

        d.closeDoor();
        d.lockDoor();
        d.unlockDoor();
    }
}