package com.capit.capitinteractions.domain.checkin.service;

import com.capit.capitinteractions.domain.checkin.dto.CheckinDto;
import com.capit.capitinteractions.domain.checkin.requests.CheckinRequest;
import com.capit.capitinteractions.domain.checkin.entity.Checkin;
import com.capit.capitinteractions.domain.checkin.repository.CheckinRepository;
import com.capit.capitinteractions.domain.user.User;
import com.capit.capitinteractions.domain.user.UserRepository;
import com.capit.exceptions.BaseRuntimeException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckinServiceImpl implements CheckinService {

    private final CheckinRepository checkinRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CheckinDto createCheckin(CheckinRequest request) {
        if (checkinRepository.existsByUserIdAndSessionId(request.userId(), request.sessionId())) {
            throw new BaseRuntimeException("Checkin already exists for userId: " + request.userId() + " and sessionId: " + request.sessionId(), HttpStatus.CONFLICT);
        }

        Checkin checkin = Checkin.builder()
                .id(UUID.randomUUID())
                .userId(request.userId())
                .sessionId(request.sessionId())
                .conferenceId(request.conferenceId())
                .checkinTime(LocalDateTime.now())
                .build();

        checkinRepository.save(checkin);

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BaseRuntimeException("User not found with id: " + request.userId(), HttpStatus.NOT_FOUND));

        return CheckinDto.builder()
                .id(checkin.getId())
                .userId(checkin.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .conferenceId(checkin.getConferenceId())
                .sessionId(checkin.getSessionId())
                .checkinTime(checkin.getCheckinTime())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CheckinDto> getCheckinById(UUID id) {
        return checkinRepository.findById(id)
                .map(c -> modelMapper.map(c, CheckinDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckinDto> getCheckinsByUserId(UUID userId) {
        return checkinRepository.findByUserId(userId).stream()
                .map(c -> modelMapper.map(c, CheckinDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckinDto> getCheckinsBySessionId(UUID sessionId) {
        return checkinRepository.findBySessionId(sessionId).stream()
                .map(c -> modelMapper.map(c, CheckinDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckinDto> getCheckinsByConferenceId(UUID conferenceId) {
        return checkinRepository.findByConferenceId(conferenceId).stream()
                .map(c -> modelMapper.map(c, CheckinDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkinExists(UUID userId, UUID sessionId) {
        return checkinRepository.existsByUserIdAndSessionId(userId, sessionId);
    }

    // delete checkin
    @Override
    @Transactional
    public void deleteCheckin(UUID id) {
        Checkin checkin = checkinRepository.findById(id)
                .orElseThrow(() -> new BaseRuntimeException("Checkin not found with id: " + id, HttpStatus.NOT_FOUND));
        checkinRepository.delete(checkin);
    }
}
