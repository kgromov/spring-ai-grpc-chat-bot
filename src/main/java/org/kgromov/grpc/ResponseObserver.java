package org.kgromov.grpc;

import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.synchronizedList;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseObserver<T> implements StreamObserver<T> {
    // FIXME: try to pass Runnable/Callable instead
    private final CountDownLatch latch;
    @Getter
    private final List<T> list;
    @Getter
    private Throwable throwable;

    public static <T> ResponseObserver<T> create() {
        return new ResponseObserver<>(new CountDownLatch(1), synchronizedList(new ArrayList<>()));
    }

    @Override
    public void onNext(T t) {
        log.debug("received item: {}", t);
        this.list.add(t);
    }

    @Override
    public void onError(Throwable throwable) {
        log.debug("received error: {}", throwable.getMessage());
        this.throwable = throwable;
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        log.debug("completed");
        this.latch.countDown();
    }

    public void await() {
        try {
            this.latch.await(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
