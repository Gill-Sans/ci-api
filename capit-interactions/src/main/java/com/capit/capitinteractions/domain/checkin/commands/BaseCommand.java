package com.capit.capitinteractions.domain.checkin.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@RequiredArgsConstructor
public class BaseCommand<T> {
    @TargetAggregateIdentifier
    public final T id;
}
