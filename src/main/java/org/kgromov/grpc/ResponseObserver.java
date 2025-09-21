package org.kgromov.grpc;

import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.synchronizedList;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseObserver<T> implements StreamObserver<T> {
    private final Consumer<T> onNext;
    @Getter
    private final List<T> list;
    @Getter
    private Throwable throwable;

    public static <T> ResponseObserver<T> create(Consumer<T> onNext) {
        return new ResponseObserver<>(onNext, synchronizedList(new ArrayList<>()));
    }

    @Override
    public void onNext(T t) {
        log.debug("received item: {}", t);
        this.onNext.accept(t);
        this.list.add(t);
    }

    @Override
    public void onError(Throwable throwable) {
        log.debug("received error: {}", throwable.getMessage());
        this.throwable = throwable;
    }

    @Override
    public void onCompleted() {
        log.debug("completed");
    }
}
