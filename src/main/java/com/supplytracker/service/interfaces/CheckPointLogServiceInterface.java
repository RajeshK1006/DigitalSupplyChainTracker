package com.supplytracker.service.interfaces;

import com.supplytracker.dto.CheckpointDTO;
import com.supplytracker.entity.CheckpointLog;
import java.util.*;


public interface CheckPointLogServiceInterface {
    public CheckpointDTO addCheckPoint(CheckpointDTO dto);
    public List<CheckpointDTO> getCheckpointByShipment(CheckpointDTO dto);
}
