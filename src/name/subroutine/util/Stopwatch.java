/*
 * Created on May 5, 2009
 */
package name.subroutine.util;

/**
 * @author s96612s
 * 
 * to track time spent on processes.  to use, call
 * 
 * start()
 * 
 * before you begin some task, such as File.read()
 * 
 * then call
 * 
 * stop()
 * 
 * after you finish some task, such as File.read()
 * 
 * then call
 * 
 * getTimeElapsed to find out how many milliseconds it took to perform that
 * task
 */
public class Stopwatch
{
    protected long timeStart;
    protected long timeStop;
    
    public void start()
    {
        setTimeStart( System.currentTimeMillis() );
    }
    
    public void stop()
    {
        setTimeStop( System.currentTimeMillis() );
    }
    
    public long getTimeElapsed()
    {
        return getTimeStop() - getTimeStart();
    }
    
    public long getTimeStart()
    {
        return timeStart;
    }
    
    public void setTimeStart( long timeStart )
    {
        this.timeStart = timeStart;
    }
    
    public long getTimeStop()
    {
        return timeStop;
    }
    
    public void setTimeStop( long timeStop )
    {
        this.timeStop = timeStop;
    }
}
