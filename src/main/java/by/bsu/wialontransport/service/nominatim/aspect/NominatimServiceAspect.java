package by.bsu.wialontransport.service.nominatim.aspect;

import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@Aspect
@Component
@ConditionalOnProperty(prefix = "nominatim", name = "enable-waiting-between-requests", havingValue = "true")
public final class NominatimServiceAspect implements AutoCloseable {
    private final long millisBetweenRequests;
    private final ExecutorService executorService;
    private final Lock lock;
    private final Condition condition;
    private boolean durationBetweenRequestsPassed;

    public NominatimServiceAspect(@Value("${nominatim.millis-between-requests}") final long millisBetweenRequests) {
        this.millisBetweenRequests = millisBetweenRequests;
        this.executorService = newSingleThreadExecutor();
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.durationBetweenRequestsPassed = true;
    }

    @PostConstruct
    public void runTaskWaitingNecessaryDurationBetweenRequests() {
        final Runnable taskWaitingNecessaryDurationBetweenRequests
                = this.createTaskWaitingNecessaryDurationBetweenRequests();
        this.executorService.submit(taskWaitingNecessaryDurationBetweenRequests);
    }

    @Around("reverseMethod()")
    public NominatimReverseResponse reverseWithWaitingBetweenRequests(final ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {
        this.lock.lock();
        try {
            while (!this.durationBetweenRequestsPassed) {
                this.condition.await();
            }
            final NominatimReverseResponse response = (NominatimReverseResponse) proceedingJoinPoint.proceed();
            this.durationBetweenRequestsPassed = false;
            this.condition.signalAll();
            return response;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    @PreDestroy
    public void close() {
        this.executorService.shutdownNow();
    }

    private Runnable createTaskWaitingNecessaryDurationBetweenRequests() {
        return () -> {
            this.lock.lock();
            try {
                while (!currentThread().isInterrupted()) {
                    while (this.durationBetweenRequestsPassed) {
                        this.condition.await();
                    }
                    MILLISECONDS.sleep(this.millisBetweenRequests);
                    this.durationBetweenRequestsPassed = true;
                    this.condition.signalAll();
                }
            } catch (final InterruptedException cause) {
                currentThread().interrupt();
            } finally {
                this.lock.unlock();
            }
        };
    }

    @Pointcut("execution(public by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse "
            + "by.bsu.wialontransport.service.nominatim.NominatimService.reverse(..)"
            + ")")
    private void reverseMethod() {

    }
}
