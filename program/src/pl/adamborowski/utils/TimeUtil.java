/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

import java.text.DecimalFormat;
import java.util.Stack;

/**
 *
 * @author test
 */
public class TimeUtil
{

    private static Stack<Long> stack = new Stack<>();
    private static Stack<String> nameStack = new Stack<>();

    public static void start()
    {
        start("<skrypt>");
    }

    public static void start(String name)
    {
        stack.push(System.nanoTime());
        nameStack.push(name);
    }
    static DecimalFormat decimalFormat = new DecimalFormat("#.####");

    public static void stop()
    {
        System.out.println("Wykonanie: " + nameStack.pop() + " zajęło " + decimalFormat.format((System.nanoTime() - stack.pop()) / 1000.0 / 1000.0) + " ms.");
    }
    private static long lastFrame=0;
    public static void frame()
    {
        long newFrame=System.currentTimeMillis();
        long fps = (long) (1000.0f/(float)(newFrame-lastFrame));
        lastFrame=newFrame;
        System.out.println("FPS: "+fps);
    }
}
