package ru.postservice.service;


import ru.postservice.model.dto.NewPostDelivery;
import ru.postservice.model.dto.PostDeliveryHistory;
import ru.postservice.model.PostItem;

public interface PostService {
    PostItem createPostItem(int idPostOffice, String typePostDelivery, NewPostDelivery postDelivery);

    PostItem registerArrivedPostDelivery(int postOfficeInd, long id);

    PostItem registerDeparturePostDelivery(int postOfficeInd, int recipientOfficeId, long idd);

    PostItem registerReceivingPostDelivery(int postOfficeInd, long id);

    PostDeliveryHistory getHistory(long postId);

    PostItem registerReceivePostDelivery(int postOfficeInd, long id);
}
