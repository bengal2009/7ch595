
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marcandreuf
 */
public class NCH595 extends Common_74HC595 {

    private final List<Led> leds;

    private class Led {
        short byteValue;
        public Led(short value) {
            this.byteValue = value;
        }
        public short getValue() {
            return byteValue;
        }
    }

    public NCH595(GpioController gpio) {
        super(gpio);

        leds = new ArrayList<>();
        leds.add(new Led((short) 0x01)); //1
        leds.add(new Led((short) 0x02)); //2
        leds.add(new Led((short) 0x04)); //4
        leds.add(new Led((short) 0x08)); //8
        leds.add(new Led((short) 0x10)); //16
        leds.add(new Led((short) 0x20)); //32
        leds.add(new Led((short) 0x40)); //64
        leds.add(new Led((short) 0x80)); //128
    }

    public static void main(String[] args) throws InterruptedException {
        NCH595 ex11_74HC595 = new NCH595(GpioFactory.getInstance());
        ex11_74HC595.run(args);
    }

    @Override
    protected void setup() {
        init();
    }

    @Override
    protected void loop(String[] args) throws InterruptedException {
        do {
            /*for (Led led : leds) {
                transferInputByteToShiftReg(led.getValue());
                System.out.println(led.getValue());
                delay(1000);
                latchShiftRegToStorageReg();
                System.out.println("Latch");
                System.out.println();
                delay(1000);
            }*/
            for (int i=0; i<8; i++) {
                System.out.println(leds.get(8 - i - 1).getValue());
                transferInputByteToShiftReg(leds.get(8 - i - 1).getValue());
                latchShiftRegToStorageReg();
                delay(2000);

            }
            /*for (int i=0; i<3; i++) {
                transferInputByteToShiftReg((short) 0xff);
                latchShiftRegToStorageReg();
                delay(100);
                transferInputByteToShiftReg((short) 0x00);
                latchShiftRegToStorageReg();
                delay(100);
            }
            delay(500);

            for (int i=0; i<8; i++) {
                transferInputByteToShiftReg(leds.get(8-i-1).getValue());
                latchShiftRegToStorageReg();
                delay(150);
            }
            delay(500);

            for (int i=0; i<3; i++) {
                transferInputByteToShiftReg((short) 0xff);
                latchShiftRegToStorageReg();
                delay(100);
                transferInputByteToShiftReg((short) 0x00);
                latchShiftRegToStorageReg();
                delay(100);
            }*/
            delay(500);

        } while (isNotInterrupted);
    }
}
