/**
 * Created by Lin on 2015/8/9.
 */
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * Super class with sketch abstraction methods.
 *
 * @author marcandreuf
 */
public abstract class BaseSketch {

    protected final GpioController gpio;
    protected static Thread threadCheckInputStream;
    protected boolean isNotInterrupted = true;
    protected static final CountDownLatch countDownLatchEndSketch = new CountDownLatch(1);

    protected abstract void setup();
    protected abstract void loop(String[] args) throws InterruptedException;

    protected void loop() throws InterruptedException{
        loop(new String[0]);
    }

    public BaseSketch() {
        this(null);
    }

    public BaseSketch(GpioController gpio) {
        this.gpio = gpio;
    }

    protected void run(String[] args) throws InterruptedException {
        setup();
        startThreadToCheckInputStream();
        loop(args);
        tearDown();
    }

    private void startThreadToCheckInputStream() {
        CheckEnd checkend = new CheckEnd();
        threadCheckInputStream = new Thread(checkend);
        threadCheckInputStream.start();
    }

    protected void tearDown() {
        if(gpio != null){
            gpio.shutdown();
        }
    }

    protected void delay(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException ex) {
        }
    }

    protected void setSketchInterruption() {
        if (threadCheckInputStream != null && threadCheckInputStream.isAlive()) {
            threadCheckInputStream.interrupt();
        }
        isNotInterrupted = false;
    }

    private class CheckEnd implements Runnable {

        Scanner scanner = new Scanner(System.in);

        @Override
        @SuppressWarnings("empty-statement")
        public void run() {
            while (!scanner.hasNextLine()) {};
            scanner.close();
            isNotInterrupted = false;
            countDownLatchEndSketch.countDown();
        }
    }

    protected List<GpioPinDigitalOutput> createListOfPinOutputs(int numOfPins) throws Exception {
        Pin pin;
        List<GpioPinDigitalOutput> list = new ArrayList<>();
        if(numOfPins<1) throw new NumberFormatException("The num of leds can not be negative.");
        if(numOfPins>20) throw new NumberFormatException("The maximum number of GPIOs is 20.");
        for (int i = 0; i < numOfPins; i++) {
            pin = RaspiPin.getPinByName("GPIO "+i);
            list.add(gpio.provisionDigitalOutputPin(pin));
        }
        return list;
    }

    protected static void wiringPiSetup(){
        if (Gpio.wiringPiSetup() == -1) {
            String msg = "==>> GPIO SETUP FAILED";
            throw new RuntimeException(msg);
        }
    }

}
