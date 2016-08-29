import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Created by Lin on 2015/8/9.
 *  *
 *	    74HC595
 *      Q1 |1     16|-> VCC (+5V)
 *      Q2 |2     15| Q0
 *      Q3 |3     14|-> DS (PB2, pin 51)
 *      .  |4     13|-> OE
 *      .  |5     12|-> ST_CP (PB0, pin 53)
 *      .  |6     11|-> SH_CP (PB1, pin 52)
 *      .  |7     10|-> MR
 *     GND |8      9| Q7'
11
 */

public class N2 {
    static final GpioController gpio = GpioFactory.getInstance();
    static final GpioPinDigitalOutput DATAPIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23);
    static final GpioPinDigitalOutput CLOCKPIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24);
    static final GpioPinDigitalOutput LATCHPIN = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25);
   /* int data = 23;
    int clock = 24;
    int latch = 25;*/
   static int ledState = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Short.parseShort("11111111", 2));
        System.out.println(fromBin("10000000"));
        for(int i = 0; i < 8; i++){
            System.out.println("I:"+ i);
            changeLED(i, 1);     //turn on LED #i
            Thread.sleep(1000);
            changeLED(i,0); //turn off the LED we turned on last time
            Thread.sleep(1000);
        }
        for(int i = 7; i >0; i--){
            System.out.println("I:"+i);
            changeLED(i, 1);     //turn on LED #i
            Thread.sleep(1000);
            changeLED(i, 0); //turn off the LED we turned on last time
            Thread.sleep(1000);
        }

    }
    /*
    * updateLEDs() - sends the LED states set in ledStates to the 74HC595
    * sequence
    */
    static void updateLEDs(int value){
        LATCHPIN.setState(false);
//        com.pi4j.wiringpi.Shift.shiftOut((byte) 23, (byte) 24, (byte) 1, (byte) value);
        shiftOut(23,24,1,(byte) value);
//        shiftOut(data, clock, MSBFIRST, value); //Shifts out the 8 bits to the shift register
        LATCHPIN.setState(true);
    }
    static void shiftOut(Integer dataPin, Integer clockPin, Integer bitOrder, byte val)
    {
        int i;
        for (i = 0; i < 8; i++)  {
            if (bitOrder ==  com.pi4j.wiringpi.Shift.LSBFIRST)
                DATAPIN.setState(false);
//                digitalWrite(dataPin, !!(val & (1 << i)));
            else
                DATAPIN.setState(true);
//            digitalWrite(dataPin, !!(val & (1 << (7 - i))));
            CLOCKPIN.setState(true);
            CLOCKPIN.setState(false);
        }
    }

    /*
     * updateLEDsLong() - sends the LED states set in ledStates to the 74HC595
     * sequence. Same as updateLEDs except the shifting out is done in software
     * so you can see what is happening.
     */
    static void updateLEDsLong(int value) throws InterruptedException{

        LATCHPIN.low();
        for(int i = 0; i < 8; i++){  //Will repeat 8 times (once for each bit)
            int bit = value & fromBin("10000000"); //We use a "bitmask" to select only the eighth
            //bit in our number (the one we are addressing this time thro
            //ugh
            value = value << 1;          //we move our number up one bit value so next time bit 7 will
            // be
            //bit 8 and we will do our math on it
            if(bit == 128){DATAPIN.high();} //if bit 8 is set then set our data pin high
            else{DATAPIN.low();}            //if bit 8 is unset then set the data pin low
            CLOCKPIN.high();
            Thread.sleep(1000);
            CLOCKPIN.low();
        }
        LATCHPIN.high();
    }


    public static Short fromBin(String bin) {
//        return (byte) Integer.parseInt(bin, 2);
        return (Short) Short.parseShort(bin,2);
    }

    //These are used in the bitwise math that we use to change individual LEDs
//For more details http://en.wikipedia.org/wiki/Bitwise_operation
    static Short bits[] = {fromBin("00000001"), fromBin("00000010"),
            fromBin("00000100"), fromBin("00001000"),
            fromBin("00010000"), fromBin("00100000"),
                    fromBin("01000000"), fromBin("10000000")};
    static Short masks[] = {fromBin("11111110"), fromBin("11111101"),
            fromBin("11111011"), fromBin("11110111"),
                    fromBin("11101111"), fromBin("11011111"),
                            fromBin("10111111"), fromBin("01111111")};
    /*
     * changeLED(int led, int state) - changes an individual LED
     * LEDs are 0 to 7 and state is either 0 - OFF or 1 - ON
     */
    static void changeLED(int led, int state){
        ledState = ledState & masks[led];  //clears ledState of the bit we are addressing
        if(state == 1){ledState = ledState | bits[led];} //if the bit is on we will add it to le
        //dState
        updateLEDs(ledState);              //send the new LED state to the shift register
    }


}
