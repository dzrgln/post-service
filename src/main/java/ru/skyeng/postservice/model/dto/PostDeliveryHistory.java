package ru.skyeng.postservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skyeng.postservice.model.StageDelivery;

import java.util.List;

@Setter
@Getter
public class PostDeliveryHistory {
    private List<StageDelivery> stageDeliveryList;
}
