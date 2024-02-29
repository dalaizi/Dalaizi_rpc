package client;

import model.ClientRequest;
import model.Response;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {
    public final static ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture = new ConcurrentHashMap<Long,DefaultFuture>();

    final Lock reentrantLock = new ReentrantLock();
    public Condition condition = reentrantLock.newCondition();
    private Response response;
    private Long timeOut = 2*60*1000l;
    private Long start = System.currentTimeMillis();

    public DefaultFuture(ClientRequest request) {
        allDefaultFuture.put(request.getId(), this);
    }

    public Response getResponse() {
        return response;
    }

    private void setResponse(Response response) {
        this.response = response;
    }

    public static void receive(Response response) {
        DefaultFuture defaultFuture = allDefaultFuture.get(response.getId());
        if(defaultFuture != null) {
            Lock lock = defaultFuture.reentrantLock;
            lock.lock();
            try {
                defaultFuture.setResponse(response);
                defaultFuture.condition.signalAll();
                allDefaultFuture.remove(defaultFuture);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public Long getStart() {
        return start;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public Response get(Long time) {
        reentrantLock.lock();
        try {
            while(!done()) {
                condition.await();
                if((System.currentTimeMillis()-start)>timeOut){
                    System.out.println("Future中的请求超时");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
        return this.response;
    }
    private boolean done() {
        if(this.response != null) {
            return true;
        }
        return false;
    }
    //清理线程
    static class ClearFutureThread extends Thread{
        @Override
        public void run() {
            Set<Long> ids = allDefaultFuture.keySet();
            for(Long id : ids){
                DefaultFuture f = allDefaultFuture.get(id);
                if(f==null){
                    allDefaultFuture.remove(f);
                }else if(f.getTimeOut()<(System.currentTimeMillis()-f.getStart()))
                {//链路超时
                    Response res = new Response();
                    res.setId(id);
                    res.setCode("33333");
                    res.setMsg("链路超时");
                    receive(res);
                }
            }
        }
    }

    static{
        ClearFutureThread clearThread = new ClearFutureThread();
        clearThread.setDaemon(true);
        clearThread.start();
    }
}
