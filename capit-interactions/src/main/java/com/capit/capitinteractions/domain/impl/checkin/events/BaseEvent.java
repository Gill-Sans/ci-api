package com.capit.capitinteractions.domain.impl.checkin.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseEvent<T> {
    public final T id;
}
