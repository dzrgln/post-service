package ru.postservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.postservice.model.StageDelivery;

import java.util.List;

@Setter
@Getter
public class PostDeliveryHistory {
    private List<StageDelivery> stageDeliveryList;
}
