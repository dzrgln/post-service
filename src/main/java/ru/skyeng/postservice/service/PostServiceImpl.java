package ru.skyeng.postservice.service;

import org.springframework.stereotype.Service;
import ru.skyeng.postservice.model.PostItem;
import ru.skyeng.postservice.model.dto.NewPostDelivery;
import ru.skyeng.postservice.model.dto.PostDeliveryHistory;

@Service
public class PostServiceImpl implements PostService{
    @Override
    public PostItem createPostDelivery(int idPostOffice, String typePostDelivery, NewPostDelivery postDelivery) {
        return null;
    }

    @Override
    public PostItem registerArrivedPostDelivery(int ownIndex, long id) {
        return null;
    }

    @Override
    public PostItem registerDeparturePostDelivery(int ownIndex, long deliveryId) {
        return null;
    }

    @Override
    public PostItem registerReceivingPostDelivery(long id) {
        return null;
    }

    @Override
    public PostDeliveryHistory getHistory(long ownerId, long postId) {
        return null;
    }
}
