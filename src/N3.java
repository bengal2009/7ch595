import com.pi4j.io.gpio.*;

/**
 * Created by Lin on 2015/8/9.
 * https://www.youtube.com/watch?v=BxKjW-d50D4
 */

public class N3 {
    static final GpioController gpio = GpioFactory.getInstance();
    static final GpioPinDigitalOutput DATAPIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23);
    static final GpioPinDigitalOutput LATCHPIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25);
    static final GpioPinDigitalOutput CLOCKPIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello");
        DATAPIN.setState(PinState.LOW);
        LATCHPIN.setState(PinState.LOW);
        CLOCKPIN.setState(PinState.LOW);
        System.out.println("Led1");
        transferInputByteToShiftReg((short) 2);
        latchShiftRegToStorageReg();


    }
    static protected void transferInputByteToShiftReg(short byteValue) {
        short readingBitPosition;
        for (short bitPosition=0; bitPosition<8; bitPosition++) {
            readingBitPosition = (short) (0x80 >> bitPosition);
            DATAPIN.setState( (byteValue & readingBitPosition) > 0 );
            pulse(CLOCKPIN);
        }
    }
    static protected void shiftOut(short dataOut){
        // This shifts 8 bits out MSB first, on the rising edge of the clock, clock idles low.

        // Clear everything out just in case to prepare shift register for bit shifting.
        DATAPIN.setState(PinState.LOW);
        CLOCKPIN.setState(PinState.LOW);

        //For each bit in the byte myDataOut,NOTICE THAT WE ARE COUNTING DOWN in our for loop
        //This means that %00000001 or "1" will go through such that it will be pin Q0 that lights.
        for(short bitMask=7; bitMask>=0; bitMask--){
            CLOCKPIN.setState(PinState.LOW);

            //if the value passed to myDataOut and a bitmask result true
            //then... so if we are at i=6 and our value is %11010100 it would the code
            //compares it to %01000000 and proceeds to set pinState to 1.
            DATAPIN.setState( (dataOut & (1 << bitMask)) > 0 );
            CLOCKPIN.setState(PinState.HIGH);
            // Zero the data pin after shift to prevent bleed through
            DATAPIN.setState(PinState.LOW);
        }

        // Stop shifting
        CLOCKPIN.setState(PinState.LOW);
    }

    static protected void latchShiftRegToStorageReg() {
        pulse(CLOCKPIN);
    }
    static protected void pulse(GpioPinDigitalOutput pin) {
        pin.setState(PinState.LOW);
        pin.setState(PinState.HIGH);
    }
}
