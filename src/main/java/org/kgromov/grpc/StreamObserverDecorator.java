package org.kgromov.grpc;

import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamObserverDecorator<T> implements StreamObserver<T> {
    private final StreamObserver<T> observer;
    private final Runnable onNext;

    public static <T> StreamObserverDecorator<T> create(StreamObserver<T> observer, Runnable onNext) {
        return new StreamObserverDecorator<>(observer, onNext);
    }

    @Override
    public void onNext(T t) {
        log.debug("received item: {}", t);
        this.onNext.run();
        this.observer.onNext(t);
    }

    @Override
    public void onError(Throwable throwable) {
        log.debug("received error: {}", throwable.getMessage());
        this.observer.onError(throwable);
    }

    @Override
    public void onCompleted() {
        log.debug("completed");
        this.observer.onCompleted();
    }
}
