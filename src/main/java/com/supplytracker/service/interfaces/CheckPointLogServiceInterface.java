package com.supplytracker.service.interfaces;

import com.supplytracker.dto.CheckpointDto;
import com.supplytracker.entity.CheckpointLog;
import java.util.*;


public interface CheckPointLogServiceInterface {
    public CheckpointDto addCheckPoint(CheckpointDto dto);
    public List<CheckpointDto> getCheckpointByShipment(CheckpointDto dto);
}
