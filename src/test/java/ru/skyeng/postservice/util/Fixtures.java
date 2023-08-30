package ru.skyeng.postservice.util;

import ru.skyeng.postservice.model.*;
import ru.skyeng.postservice.model.dto.DeliveryId;
import ru.skyeng.postservice.model.dto.NewPostDelivery;
import ru.skyeng.postservice.model.dto.SenderAddress;

import java.time.LocalDateTime;

public class Fixtures {
    public static DeliveryId getDeliveryId() {
        DeliveryId deliveryId = new DeliveryId();
        deliveryId.setId(1);
        return deliveryId;
    }

    public static NewPostDelivery getNewPostItem() {
        NewPostDelivery postItem = new NewPostDelivery();
        postItem.setIndex(123456);
        SenderAddress address = getSenderAddress();
        postItem.setAddress(address);
        postItem.setUser("Alex");
        return postItem;
    }

    public static PostItem getPostItem() {
        PostItem postItem = new PostItem();
        postItem.setId(1);
        postItem.setIndex(12345);
        postItem.setTypePostItem(getTypePostItemLetter());
        postItem.setAddress(getAddress());
        postItem.setSender("Alex");
        return postItem;
    }

    public static SenderAddress getSenderAddress() {
        SenderAddress address = new SenderAddress();
        address.setIndex(987654);
        address.setCity("C3");
        address.setStreet("Желтая");
        address.setHouseNumber(8);
        address.setFlatNumber(7);
        return address;
    }

    public static Address getAddress() {
        Address address = new Address();
        address.setId(1);
        address.setIndex(987654);
        address.setCity("C3");
        address.setStreet("Желтая");
        address.setHouseNumber(8);
        address.setFlatNumber(7);
        return address;
    }

    public static TypePostItem getTypePostItemLetter() {
        TypePostItem typePostItem = new TypePostItem();
        typePostItem.setId(1);
        typePostItem.setType("Письмо");
        typePostItem.setAlias("letter");
        return typePostItem;
    }

    public static PostOffice getPostOfficeOne() {
        PostOffice postOffice = new PostOffice();
        postOffice.setIndex(123456);
        postOffice.setName("Отделение №1");
        return postOffice;
    }

    public static PostOffice getPostOfficeThree() {
        PostOffice postOffice = new PostOffice();
        postOffice.setIndex(987654);
        postOffice.setName("Отделение №3");
        return postOffice;
    }

    public static StatusDelivery getStatusDeliveryOne() {
        StatusDelivery statusDelivery = new StatusDelivery();
        statusDelivery.setId(1);
        statusDelivery.setStatus("Зарегистрировано в отделении");
        return statusDelivery;
    }

    public static StageDelivery getStageDelivery() {
        StageDelivery stageDelivery = new StageDelivery();
        stageDelivery.setId(1L);
        stageDelivery.setItem(getPostItem());
        stageDelivery.setSenderOffice(getPostOfficeOne());
        stageDelivery.setRecipientOffice(getPostOfficeOne());
        stageDelivery.setStatusDelivery(getStatusDeliveryOne());
        stageDelivery.setOperationTime(LocalDateTime.now());
        return stageDelivery;
    }
}
