import com.pi4j.io.gpio.*;

/**
 * Created by Lin on 2015/8/9.
 * https://github.com/marcandreuf/sunfounder-raspi-4j/tree/master/src/main/java/org/mandfer/sunfunpi4j
 */
public class N1 {
    final static GpioController gpio = GpioFactory.getInstance();
    protected static GpioPinDigitalOutput pinSerailDataInput_DS;
    protected static GpioPinDigitalOutput pinStorageClock_STCP;
    protected static GpioPinDigitalOutput pinShiftRegClock_SHCP;
    public static void main(String[] args) throws InterruptedException {
        pinSerailDataInput_DS= gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23);
        pinStorageClock_STCP= gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24);
        pinShiftRegClock_SHCP= gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25);
        pinSerailDataInput_DS.setState(PinState.LOW);
        pinStorageClock_STCP.setState(PinState.LOW);
        pinShiftRegClock_SHCP.setState(PinState.LOW);
     /*   for(short bitPosition=0; bitPosition<8; bitPosition++)
        {
            System.out.println((short) (0x80 >> bitPosition));
            pinStorageClock_STCP.low();
//            shiftOut(pinSerailDataInput_DS, pinShiftRegClock_SHCP, 1, bitPosition);

//            com.pi4j.wiringpi.Shift.shiftOut((byte)23,(byte) 25,(byte) 1,(byte)bitPosition);
            Thread.sleep(500);

        }*/
/*for(Short i=1;i<256;i++ ) {
    System.out.println("I:"+i);
    transferInputByteToShiftReg((short) i);
    latchShiftRegToStorageReg();
    Thread.sleep(500);
}*/
        transferInputByteToShiftReg((short) 64);
        latchShiftRegToStorageReg();
        Thread.sleep(500);

       /* transferInputByteToShiftReg((short) 0x02);
        latchShiftRegToStorageReg();
        Thread.sleep(500);*/

        }
    static void transferInputByteToShiftReg(short byteValue) {
        short readingBitPosition;
        for (short bitPosition=0; bitPosition<8; bitPosition++) {
            readingBitPosition = (short) (0x80 >> bitPosition);
            pinSerailDataInput_DS.setState((byteValue & readingBitPosition) > 0);
            pulse(pinShiftRegClock_SHCP);
        }
    }
    static void latchShiftRegToStorageReg() {
        pulse(pinStorageClock_STCP);
    }
    static void pulse(GpioPinDigitalOutput pin) {
        pin.setState(PinState.LOW);
        pin.setState(PinState.HIGH);
    }
    /*void updateLED(int value){
//把 595N 的 STcp 關閉，暫時不輸出訊號
        digitalWrite(latch, LOW);
//把 8 bit 訊號由 MSBFIRST 的順序，輸出給 595 chip
        shiftOut(data, clock, MSBFIRST, value);
//打開 STcp，把剛輸入的 8 bit 訊號由 Q0~Q7 輸出讓 LED 亮起來
        digitalWrite(latch, HIGH);
    }*/

}
