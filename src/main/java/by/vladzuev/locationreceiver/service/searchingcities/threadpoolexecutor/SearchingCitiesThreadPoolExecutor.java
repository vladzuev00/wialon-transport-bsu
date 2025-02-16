package by.vladzuev.locationreceiver.service.searchingcities.threadpoolexecutor;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public final class SearchingCitiesThreadPoolExecutor extends ThreadPoolExecutor {

    /*
     * Size of executor service should be equal or more than 2 - one thread to start main task, another threads to start subtasks.
     * If size is 1, then main task will be run and then blocked until subtasks will be finished but subtasks won't be run =>
     * so deadlock will happen
     */
    private static final int MINIMAL_THREAD_COUNT = 2;

    private static final int MAXIMAL_THREAD_COUNT = 10;

    public SearchingCitiesThreadPoolExecutor(@Value("${searching-cities.thread-count}") final int threadCount) {
        super(
                validateThreadCountWithReturning(threadCount),
                threadCount,
                0,
                MILLISECONDS,
                new LinkedBlockingQueue<>()
        );
    }

    @Override
    @PreDestroy
    public void shutdown() {
        super.shutdown();
    }

    private static int validateThreadCountWithReturning(final int threadCount) {
        if (!isValidThreadCount(threadCount)) {
            throw new IllegalArgumentException(
                    "Thread count '%d' isn't valid".formatted(threadCount)
            );
        }
        return threadCount;
    }

    private static boolean isValidThreadCount(final int threadCount) {
        return MINIMAL_THREAD_COUNT <= threadCount && threadCount <= MAXIMAL_THREAD_COUNT;
    }
}
