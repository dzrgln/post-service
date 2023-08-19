package ru.skyeng.postservice.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skyeng.postservice.exceptions.UnknownDataException;
import ru.skyeng.postservice.model.*;
import ru.skyeng.postservice.model.dto.NewPostDelivery;
import ru.skyeng.postservice.model.dto.PostDeliveryHistory;
import ru.skyeng.postservice.model.dto.SenderAddress;
import ru.skyeng.postservice.repository.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AddressRepository addressRepository;
    private final TypePostItemRepository typePostItemRepository;
    private final StageDeliveryRepository stageDeliveryRepository;
    private final PostOfficeRepository postOfficeRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           AddressRepository addressRepository,
                           TypePostItemRepository typePostItemRepository,
                           StageDeliveryRepository stageDeliveryRepository,
                           PostOfficeRepository postOfficeRepository) {
        this.postRepository = postRepository;
        this.addressRepository = addressRepository;
        this.typePostItemRepository = typePostItemRepository;
        this.stageDeliveryRepository = stageDeliveryRepository;
        this.postOfficeRepository = postOfficeRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public PostItem createPostItem(int index, String aliasTypePostItem, NewPostDelivery postDelivery) {
        Address address = getAddress(postDelivery);
        TypePostItem typePostItem = getTypePostItem(aliasTypePostItem);
        PostItem postItem = postRepository.save(PostItem.builder()
                .typePostItem(typePostItem)
                .address(address)
                .index(index)
                .sender(postDelivery.getUser())
                .build()
        );
        fixStageDelivery(postItem, getPostOffice(index), new StatusDelivery(1, "Зарегистрировано в отделении"));
        log.info("Saving was good id={}", postItem.getId());
        System.out.println(postRepository.getReferenceById(postItem.getId()));
        return postItem;
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

    private StageDelivery fixStageDelivery(PostItem postItem, PostOffice postOffice, StatusDelivery status){
        StageDelivery stageDelivery = StageDelivery.builder()
                .item(postItem)
                .office(postOffice)
                .statusDelivery(status)
                .operationTime(LocalDateTime.now())
                .build();
        return stageDeliveryRepository.save(stageDelivery);
    }
    private TypePostItem getTypePostItem(String aliasTypePostItem) {
        Optional<TypePostItem> optionalTypePostItem = typePostItemRepository.getTypePostItemByAlias(aliasTypePostItem);
        TypePostItem typePostItem;
        if(optionalTypePostItem.isEmpty()){
            throw new UnknownDataException("Указанный адрес отсутствует в базе");
        } else {
            typePostItem = optionalTypePostItem.get();
        }
        return typePostItem;
    }
    private PostOffice getPostOffice(int index) {
        Optional<PostOffice> optionalPostOffice = postOfficeRepository.getPostOfficeByIndex(index);
        PostOffice typePostItem;
        if(optionalPostOffice.isEmpty()){
            throw new UnknownDataException("Ошибка в индексе");
        } else {
            typePostItem = optionalPostOffice.get();
        }
        return typePostItem;
    }

    private Address getAddress(NewPostDelivery postDelivery) {
        SenderAddress senderAddress = postDelivery.getAddress();
        Optional<Address> optionalAddress = addressRepository.getAddressBySenderAddress(senderAddress.getIndex(),
                senderAddress.getCity(), senderAddress.getStreet(), senderAddress.getHouseNumber(),
                senderAddress.getFlatNumber());
        Address address;
        if(optionalAddress.isEmpty()){
            throw new UnknownDataException("Указаный адрес отсутсвует в базе");
        } else {
            address = optionalAddress.get();
        }
        return address;
    }
}
