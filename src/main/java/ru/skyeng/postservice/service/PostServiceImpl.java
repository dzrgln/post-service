package ru.skyeng.postservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.skyeng.postservice.model.*;
import ru.skyeng.postservice.model.dto.NewPostDelivery;
import ru.skyeng.postservice.model.dto.PostDeliveryHistory;
import ru.skyeng.postservice.model.dto.SenderAddress;
import ru.skyeng.postservice.repository.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostItemsRepository postRepository;
    private final AddressRepository addressRepository;
    private final TypePostItemRepository typePostItemRepository;
    private final StageDeliveryRepository stageDeliveryRepository;
    private final PostOfficeRepository postOfficeRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public PostItem createPostItem(int postOfficeInd, String aliasTypePostItem, NewPostDelivery postDelivery) {
        Address address = getAddress(postDelivery);
        TypePostItem typePostItem = getTypePostItem(aliasTypePostItem);
        PostOffice postOffice = checkPostOfficeById(postOfficeInd);
        PostItem postItem = postRepository.save(PostItem.builder()
                .typePostItem(typePostItem)
                .address(address)
                .index(postOfficeInd)
                .sender(postDelivery.getUser())
                .build()
        );
        saveStageDelivery(postItem, postOffice, postOffice, new StatusDelivery(1, "Зарегистрировано в отделении"));
        log.info("Посылка успешно зарегестрированна в отделении id отправления = {}", postItem.getId());
        return postItem;
    }

    @Override
    public PostItem registerDeparturePostDelivery(int postOfficeInd, int recipientOfficeId, long id) {
        PostOffice senderOffice = checkPostOfficeById(postOfficeInd);
        PostOffice recipientOffice = checkPostOfficeById(recipientOfficeId);

        PostItem postItem = checkDeliveryById(id);
        if (checkIsItemInPostOffice(id, postOfficeInd)) {
            saveStageDelivery(postItem, senderOffice, recipientOffice, new StatusDelivery(3, "Отправлено из отделения"));
            log.info("Посылка успешно отправлена из отделения");
            return postItem;
        } else {
            throw new DataIntegrityViolationException("Посылка сейчас находится не в том метсте, откуда пришел запрос на отправление");
        }
    }

    @Override
    public PostItem registerArrivedPostDelivery(int ownIndex, long id) {
        PostOffice postOffice = checkPostOfficeById(ownIndex);
        PostItem postItem = checkDeliveryById(id);

        if (checkIsItemToPostOffice(id, ownIndex)) {
            saveStageDelivery(postItem, postOffice, postOffice, new StatusDelivery(2, "Прибыло в отделение"));
            log.info("Посылка успешно прибыла в пункт назначения");
            return postItem;
        } else {
            throw new DataIntegrityViolationException("Посылка была отправлена не в то метсто, откуда пришел запрос на прибытие");
        }
    }


    @Override
    public PostItem registerReceivingPostDelivery(int ownIndex, long id) {
        PostOffice postOffice = checkPostOfficeById(ownIndex);
        PostItem postItem = checkDeliveryById(id);
        if (postOffice.getIndex() == postItem.getAddress().getIndex()) {
            saveStageDelivery(postItem, postOffice, postOffice, new StatusDelivery(4, "Готово к выдаче"));
            log.info("Послыка успешно подготовленна к выдаче");
        } else {
            throw new DataIntegrityViolationException("Посылка находится не в месте ее назначения");
        }
        return postItem;
    }

    @Override
    public PostItem registerReceivePostDelivery(int ownIndex, long id) {
        PostOffice postOffice = checkPostOfficeById(ownIndex);
        PostItem postItem = checkDeliveryById(id);
        if (postOffice.getIndex() == postItem.getAddress().getIndex()) {
            saveStageDelivery(postItem, postOffice, postOffice, new StatusDelivery(5, "Получено адресатом"));
            log.info("Получено адресатом");
        } else {
            throw new DataIntegrityViolationException("Посылка находится не в месте где ее должны получить");
        }
        return postItem;
    }

    @Override
    public PostDeliveryHistory getHistory(long postId) {

        PostItem postItem = checkDeliveryById(postId);
        List<StageDelivery> stageDeliveries = stageDeliveryRepository.findByItemId(postId)
                .orElse(Collections.emptyList());
        PostDeliveryHistory deliveryHistory = new PostDeliveryHistory();
        deliveryHistory.setStageDeliveryList(stageDeliveries);
        return deliveryHistory;
    }


    private void saveStageDelivery(PostItem postItem, PostOffice senderOffice,
                                  PostOffice recipientOffice, StatusDelivery status) {
        StageDelivery stageDelivery = StageDelivery.builder()
                .item(postItem)
                .senderOffice(senderOffice)
                .recipientOffice(recipientOffice)
                .statusDelivery(status)
                .operationTime(LocalDateTime.now())
                .build();
        stageDeliveryRepository.save(stageDelivery);
    }

    private TypePostItem getTypePostItem(String aliasTypePostItem) {
        return typePostItemRepository.getTypePostItemByAlias(aliasTypePostItem)
                .orElseThrow(() -> new EntityNotFoundException("Ошибка в указании типа отправления"));
    }

    private PostOffice checkPostOfficeById(int index) {
        return postOfficeRepository.getPostOfficeByIndex(index)
                .orElseThrow(() -> new EntityNotFoundException("Почтового отделения с индексом " + index + " не существует"));
    }

    private Address getAddress(NewPostDelivery postDelivery) {
        SenderAddress senderAddress = postDelivery.getAddress();
        return addressRepository.getAddressBySenderAddress(senderAddress.getIndex(),
                        senderAddress.getCity(), senderAddress.getStreet(), senderAddress.getHouseNumber(),
                        senderAddress.getFlatNumber())
                .orElseThrow(() -> new EntityNotFoundException("Указанный адрес отсутствует в базе"));
    }

    private PostItem checkDeliveryById(long itemId) {
        return postRepository.findById(itemId).
                orElseThrow(() -> new EntityNotFoundException("Почтового отправления с индексом " + itemId + " не существует"));
    }


    private boolean checkIsItemInPostOffice(long itemId, int ownIndex) {
        Optional<List<StageDelivery>> deliveries = stageDeliveryRepository.findByItemId(itemId);
        if (deliveries.isPresent()) {
            List<StageDelivery> deliveriesList = deliveries.get()
                    .stream()
                    .sorted(Comparator.comparing(StageDelivery::getOperationTime))
                    .collect(Collectors.toList());
            StageDelivery stageDelivery = deliveriesList.get(deliveriesList.size() - 1);
            return ownIndex == stageDelivery.getSenderOffice().getIndex();
        }
        return false;
    }

    private boolean checkIsItemToPostOffice(long itemId, int ownIndex) {

        Optional<List<StageDelivery>> deliveries = stageDeliveryRepository.findByItemId(itemId);
        if (deliveries.isPresent()) {
            List<StageDelivery> deliveriesList = deliveries.get()
                    .stream()
                    .sorted(Comparator.comparing(StageDelivery::getOperationTime))
                    .collect(Collectors.toList());
            StageDelivery stageDelivery = deliveriesList.get(deliveriesList.size() - 1);

            return ownIndex == stageDelivery.getRecipientOffice().getIndex();
        }
        return false;
    }
}
