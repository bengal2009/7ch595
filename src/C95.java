/**
 * Created by Lin on 2015/8/9.
 * http://thats-worth.blogspot.hk/2014/04/arduino-74hc595n-shift-register-arduino.html
 */
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marcandreuf
 */
public class C95 extends Common_74HC595 {

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

    public C95(GpioController gpio) {
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
        C95 ex11_74HC595 = new C95(GpioFactory.getInstance());

        ex11_74HC595.run(args);
    }

    @Override
    protected void setup() {
        init();
    }

    @Override
    protected void loop(String[] args) throws InterruptedException {
        do {
           /* for (Led led : leds) {
                System.out.println(led.getValue());
                transferInputByteToShiftReg(led.getValue());
                latchShiftRegToStorageReg();
                delay(2000);
            }
            transferInputByteToShiftReg((short) 0x00);
            delay(500);*/
            System.out.println("Led1");
            transferInputByteToShiftReg((short)2);
            latchShiftRegToStorageReg();
            delay(2000);
            System.out.println("Led2");
            transferInputByteToShiftReg((short) 4);
            latchShiftRegToStorageReg();
            delay(2000);
            System.out.println("Led3");
            transferInputByteToShiftReg((short)8);
            latchShiftRegToStorageReg();
            delay(2000);
            System.out.println("Led4");
            transferInputByteToShiftReg((short)16);
            latchShiftRegToStorageReg();
            delay(2000);
            transferInputByteToShiftReg((short) 0x00);
            latchShiftRegToStorageReg();

            delay(500);
            for (int i=0; i<3; i++) {
                transferInputByteToShiftReg((short) 0xff);
                latchShiftRegToStorageReg();
                delay(5000);
                transferInputByteToShiftReg((short) 0x00);
                latchShiftRegToStorageReg();
            }
                delay(1000);
            /*for (int i=0; i<8; i++) {
                System.out.println(leds.get(i).getValue());
                transferInputByteToShiftReg(leds.get(i).getValue());
                latchShiftRegToStorageReg();
                delay(1000);
            }
            delay(500);*/

          /*  for (int i=0; i<3; i++) {
                transferInputByteToShiftReg((short) 0xff);
                latchShiftRegToStorageReg();
                delay(100);
                transferInputByteToShiftReg((short) 0x00);
                latchShiftRegToStorageReg();
                delay(100);
            }*/


        } while (isNotInterrupted);
    }
}
