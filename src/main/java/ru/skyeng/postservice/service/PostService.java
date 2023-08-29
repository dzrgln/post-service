package ru.skyeng.postservice.service;


import ru.skyeng.postservice.model.PostItem;
import ru.skyeng.postservice.model.dto.NewPostDelivery;
import ru.skyeng.postservice.model.dto.PostDeliveryHistory;

public interface PostService {
    PostItem createPostItem(int idPostOffice, String typePostDelivery, NewPostDelivery postDelivery);

    PostItem registerArrivedPostDelivery(int postOfficeInd, long id);

    PostItem registerDeparturePostDelivery(int postOfficeInd, int recipientOfficeId, long idd);

    PostItem registerReceivingPostDelivery(int postOfficeInd, long id);

    PostDeliveryHistory getHistory(long postId);

    PostItem registerReceivePostDelivery(int postOfficeInd, long id);
}
