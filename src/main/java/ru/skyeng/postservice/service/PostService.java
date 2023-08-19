package ru.skyeng.postservice.service;

import ru.skyeng.postservice.model.PostItem;
import ru.skyeng.postservice.model.dto.NewPostDelivery;
import ru.skyeng.postservice.model.dto.PostDeliveryHistory;

public interface PostService {
    PostItem createPostItem(int idPostOffice, String typePostDelivery, NewPostDelivery postDelivery);

    PostItem registerArrivedPostDelivery(int ownIndex, long id);

    PostItem registerDeparturePostDelivery(int ownIndex, long deliveryId);

    PostItem registerReceivingPostDelivery(long id);

    PostDeliveryHistory getHistory(long ownerId, long postId);
}
