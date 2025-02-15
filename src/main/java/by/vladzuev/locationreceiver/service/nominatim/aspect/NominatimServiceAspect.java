package by.vladzuev.locationreceiver.service.nominatim.aspect;

import by.vladzuev.locationreceiver.service.nominatim.model.NominatimReverseResponse;
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
import java.util.Optional;
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
    private boolean requestAllowed;

    public NominatimServiceAspect(@Value("${nominatim.millis-between-requests}") final long millisBetweenRequests) {
        this.millisBetweenRequests = millisBetweenRequests;
        executorService = newSingleThreadExecutor();
        lock = new ReentrantLock();
        condition = lock.newCondition();
        requestAllowed = true;
    }

    @PostConstruct
    public void runThreadProtectingFrequentRequests() {
        final Runnable taskProtectingFrequentRequests = createTaskProtectingFrequentRequests();
        executorService.submit(taskProtectingFrequentRequests);
    }

    @Around("reverseMethod()")
    public Optional<NominatimReverseResponse> reverseProtectingFrequentRequest(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        lock.lock();
        try {
            while (!requestAllowed) {
                condition.await();
            }
            final Optional<NominatimReverseResponse> optionalResponse = proceed(joinPoint);
            requestAllowed = false;
            condition.signalAll();
            return optionalResponse;
        } finally {
            lock.unlock();
        }
    }

    @Override
    @PreDestroy
    public void close() {
        executorService.shutdownNow();
    }

    private Runnable createTaskProtectingFrequentRequests() {
        return () -> {
            lock.lock();
            try {
                while (!currentThread().isInterrupted()) {
                    while (requestAllowed) {
                        condition.await();
                    }
                    MILLISECONDS.sleep(millisBetweenRequests);
                    requestAllowed = true;
                    condition.signalAll();
                }
            } catch (final InterruptedException cause) {
                currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static Optional<NominatimReverseResponse> proceed(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        return (Optional<NominatimReverseResponse>) joinPoint.proceed();
    }

    @Pointcut(
            "execution(public java.util.Optional<by.vladzuev.locationreceiver.service.nominatim.model.NominatimReverseResponse> "
                    + "by.vladzuev.locationreceiver.service.nominatim.NominatimService.reverse("
                    + "by.vladzuev.locationreceiver.model.GpsCoordinate))"
    )
    private void reverseMethod() {

    }
}
