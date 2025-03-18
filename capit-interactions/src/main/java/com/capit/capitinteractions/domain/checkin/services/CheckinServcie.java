package com.capit.capitinteractions.domain.checkin.services;

import com.capit.capitinteractions.api.checkin.requests.CreateCheckinRequest;

public interface CheckinServcie {
    String createCheckin(CreateCheckinRequest createCheckinRequest);
}
