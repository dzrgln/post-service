package ru.postservice.service;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.postservice.model.PostOffice;
import ru.postservice.model.dto.NewPostDelivery;
import ru.postservice.model.dto.PostDeliveryHistory;
import ru.postservice.repository.*;
import ru.postservice.model.PostItem;
import ru.postservice.model.StageDelivery;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.postservice.util.Fixtures.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostServiceImpl postService;
    @Mock
    private PostItemsRepository postItemsRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private TypePostItemRepository typePostItemRepository;
    @Mock
    private PostOfficeRepository postOfficeRepository;
    @Mock
    private StageDeliveryRepository stageDeliveryRepository;
    @Captor
    private ArgumentCaptor<PostItem> postItemArgumentCaptor;

    @Captor
    private ArgumentCaptor<StageDelivery> stageDeliveryArgumentCaptor;
    private static int POST_IND_SENDER = 123456;
    private static int POST_IND_RECIPIENT = 987654;
    private static String LETTER = "letter";

    @Test
    void createPostDelivery_whenAllCorrect_thenReturnPostItem() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postItemsRepository.save(any())).thenReturn(postItem);
        when(addressRepository.getAddressBySenderAddress(getAddress().getIndex(),
                getAddress().getCity(), getAddress().getStreet(), getAddress().getHouseNumber(),
                getAddress().getFlatNumber())).thenReturn(Optional.of(getAddress()));
        when(typePostItemRepository.getTypePostItemByAlias(LETTER)).thenReturn(Optional.of(getTypePostItemLetter()));
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));
//        when(stageDeliveryRepository.save(any())).thenReturn(Collections.emptyList());

        PostItem actualPostItem = postService
                .createPostItem(POST_IND_SENDER, LETTER, newPostDelivery);

        verify(postItemsRepository).save(postItemArgumentCaptor.capture());
        PostItem savedPostItem = postItemArgumentCaptor.getValue();
        savedPostItem.setId(1);

        assertEquals(savedPostItem, actualPostItem);
        verify(postItemsRepository).save(postItem);

        assertEquals(savedPostItem.getSender(), "Alex");
        assertEquals(savedPostItem.getTypePostItem().getAlias(), LETTER);
        assertEquals(savedPostItem.getAddress().getCity(), "C3");
        assertEquals(savedPostItem.getIndex(), POST_IND_SENDER);
    }


    @Test
    void createPostDelivery_whenAddressNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        when(addressRepository.getAddressBySenderAddress(getAddress().getIndex(),
                getAddress().getCity(), getAddress().getStreet(), getAddress().getHouseNumber(),
                getAddress().getFlatNumber())).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.createPostItem(POST_IND_SENDER, LETTER, newPostDelivery));
        assertEquals(entityNotFoundException.getMessage(), "Указанный адрес отсутствует в базе");
        verify(postItemsRepository, never()).save(any());
    }

    @Test
    void createPostDelivery_whenTypePostItemNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();

        when(addressRepository.getAddressBySenderAddress(getAddress().getIndex(),
                getAddress().getCity(), getAddress().getStreet(), getAddress().getHouseNumber(),
                getAddress().getFlatNumber())).thenReturn(Optional.of(getAddress()));
        when(typePostItemRepository.getTypePostItemByAlias(LETTER)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.createPostItem(POST_IND_SENDER, LETTER, newPostDelivery));
        assertEquals(entityNotFoundException.getMessage(), "Ошибка в указании типа отправления");
        verify(postItemsRepository, never()).save(any());
    }

    @Test
    void createPostDelivery_whenPostOfficeNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();

        when(addressRepository.getAddressBySenderAddress(getAddress().getIndex(),
                getAddress().getCity(), getAddress().getStreet(), getAddress().getHouseNumber(),
                getAddress().getFlatNumber())).thenReturn(Optional.of(getAddress()));
        when(typePostItemRepository.getTypePostItemByAlias(LETTER)).thenReturn(Optional.of(getTypePostItemLetter()));
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.createPostItem(POST_IND_SENDER, LETTER, newPostDelivery));
        assertEquals(entityNotFoundException.getMessage(), "Почтового отделения с индексом " + POST_IND_SENDER +
                " не существует");
        verify(postItemsRepository, never()).save(any());
    }

    @Test
    void registerDeparturePostDelivery_whenAllCorrect_thenReturnPostItem() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.of(postItem));
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));
        when(stageDeliveryRepository.findByItemId(postItem.getId())).thenReturn(Optional.of(List.of(getStageDelivery())));

        PostItem actualPostItem = postService
                .registerDeparturePostDelivery(POST_IND_SENDER, POST_IND_SENDER, postItem.getId());

        verify(stageDeliveryRepository).save(stageDeliveryArgumentCaptor.capture());
        StageDelivery savedStageDelivery = stageDeliveryArgumentCaptor.getValue();

        assertEquals(savedStageDelivery.getStatusDelivery().getStatus(), "Отправлено из отделения");
        assertEquals(savedStageDelivery.getItem().getSender(), actualPostItem.getSender());
        assertEquals(savedStageDelivery.getSenderOffice().getIndex(), POST_IND_SENDER);
    }

    @Test
    void registerDeparturePostDelivery_whenPostOfficeNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_RECIPIENT)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.registerDeparturePostDelivery(POST_IND_RECIPIENT, POST_IND_SENDER, postItem.getId()));

        assertEquals(entityNotFoundException.getMessage(), "Почтового отделения с индексом " + POST_IND_RECIPIENT +
                " не существует");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void registerDeparturePostDelivery_whenPostItemIdeNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.empty());
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.registerDeparturePostDelivery(POST_IND_SENDER, POST_IND_SENDER, postItem.getId()));

        assertEquals(entityNotFoundException.getMessage(), "Почтового отправления с индексом " + postItem.getId() +
                " не существует");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void registerDeparturePostDelivery_whenPostOfficeNotIsSenderOffice_thenReturnBadRequest() {

        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        StageDelivery badStageDelivery = getStageDelivery();
        PostOffice badOffice = getPostOfficeThree();
        badStageDelivery.setSenderOffice(badOffice);
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.of(postItem));
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));
        when(stageDeliveryRepository.findByItemId(postItem.getId())).thenReturn(Optional.of(List.of(badStageDelivery)));

        DataIntegrityViolationException dataIntegrityViolationException = assertThrows(DataIntegrityViolationException.class,
                () -> postService.registerDeparturePostDelivery(POST_IND_SENDER, POST_IND_SENDER, postItem.getId()));

        assertEquals(dataIntegrityViolationException.getMessage(), "Посылка сейчас находится не в том метсте, откуда" +
                " пришел запрос на отправление");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void registerArrivedPostDelivery_whenAllCorrect_thenReturnPostItem() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.of(postItem));
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));
        when(stageDeliveryRepository.findByItemId(postItem.getId())).thenReturn(Optional.of(List.of(getStageDelivery())));

        PostItem actualPostItem = postService
                .registerArrivedPostDelivery(POST_IND_SENDER, postItem.getId());

        verify(stageDeliveryRepository).save(stageDeliveryArgumentCaptor.capture());
        StageDelivery savedStageDelivery = stageDeliveryArgumentCaptor.getValue();

        assertEquals(savedStageDelivery.getStatusDelivery().getStatus(), "Прибыло в отделение");
        assertEquals(savedStageDelivery.getItem().getSender(), actualPostItem.getSender());
        assertEquals(savedStageDelivery.getSenderOffice().getIndex(), POST_IND_SENDER);
    }

    @Test
    void registerArrivedPostDelivery_whenPostOfficeNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_RECIPIENT)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.registerArrivedPostDelivery(POST_IND_RECIPIENT, postItem.getId()));

        assertEquals(entityNotFoundException.getMessage(), "Почтового отделения с индексом " + POST_IND_RECIPIENT +
                " не существует");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void registerArrivedPostDelivery_whenPostItemIdeNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.empty());
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.registerArrivedPostDelivery(POST_IND_SENDER, postItem.getId()));

        assertEquals(entityNotFoundException.getMessage(), "Почтового отправления с индексом " + postItem.getId() +
                " не существует");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void registerArrivedPostDelivery_whenPostOfficeNotIsAdressesOffice_thenReturnBadRequest() {

        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        StageDelivery badStageDelivery = getStageDelivery();
        PostOffice badOffice = getPostOfficeThree();
        badStageDelivery.setRecipientOffice(badOffice);
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.of(postItem));
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));
        when(stageDeliveryRepository.findByItemId(postItem.getId())).thenReturn(Optional.of(List.of(badStageDelivery)));

        DataIntegrityViolationException dataIntegrityViolationException = assertThrows(DataIntegrityViolationException.class,
                () -> postService.registerArrivedPostDelivery(POST_IND_SENDER, postItem.getId()));

        assertEquals(dataIntegrityViolationException.getMessage(), "Посылка была отправлена не в то метсто," +
                " откуда пришел запрос на прибытие");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void registerReceivingPostDelivery_whenAllCorrect_thenReturnPostItem() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeThree()));
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.of(postItem));

        PostItem actualPostItem = postService
                .registerReceivingPostDelivery(POST_IND_SENDER, postItem.getId());

        verify(stageDeliveryRepository).save(stageDeliveryArgumentCaptor.capture());
        StageDelivery savedStageDelivery = stageDeliveryArgumentCaptor.getValue();

        assertEquals(savedStageDelivery.getStatusDelivery().getStatus(), "Готово к выдаче");
        assertEquals(savedStageDelivery.getItem().getSender(), actualPostItem.getSender());
        assertEquals(savedStageDelivery.getSenderOffice().getIndex(), POST_IND_RECIPIENT);
    }


    @Test
    void registerReceivingPostDelivery_whenPostOfficeNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_RECIPIENT)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.registerReceivingPostDelivery(POST_IND_RECIPIENT, postItem.getId()));

        assertEquals(entityNotFoundException.getMessage(), "Почтового отделения с индексом " + POST_IND_RECIPIENT +
                " не существует");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void registerReceivingPostDelivery_whenPostItemIdeNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.empty());
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.registerReceivingPostDelivery(POST_IND_SENDER, postItem.getId()));

        assertEquals(entityNotFoundException.getMessage(), "Почтового отправления с индексом " + postItem.getId() +
                " не существует");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void registerArrivedPostDelivery_whenPostOfficeNotIsOfficeWhereIsTheDelivery_thenReturnBadRequest() {

        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        StageDelivery badStageDelivery = getStageDelivery();
        PostOffice badOffice = getPostOfficeThree();
        badStageDelivery.setRecipientOffice(badOffice);
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.of(postItem));
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));

        DataIntegrityViolationException dataIntegrityViolationException = assertThrows(DataIntegrityViolationException.class,
                () -> postService.registerReceivingPostDelivery(POST_IND_SENDER, postItem.getId()));

        assertEquals(dataIntegrityViolationException.getMessage(), "Посылка находится не в месте ее назначения");
        verify(stageDeliveryRepository, never()).save(any());
    }


    @Test
    void registerReceivePostDelivery_whenAllCorrect_thenReturnPostItem() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeThree()));
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.of(postItem));

        PostItem actualPostItem = postService
                .registerReceivePostDelivery(POST_IND_SENDER, postItem.getId());

        verify(stageDeliveryRepository).save(stageDeliveryArgumentCaptor.capture());
        StageDelivery savedStageDelivery = stageDeliveryArgumentCaptor.getValue();

        assertEquals(savedStageDelivery.getStatusDelivery().getStatus(), "Получено адресатом");
        assertEquals(savedStageDelivery.getItem().getSender(), actualPostItem.getSender());
        assertEquals(savedStageDelivery.getSenderOffice().getIndex(), POST_IND_RECIPIENT);
    }


    @Test
    void registerReceivePostDelivery_whenPostOfficeNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_RECIPIENT)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.registerReceivePostDelivery(POST_IND_RECIPIENT, postItem.getId()));

        assertEquals(entityNotFoundException.getMessage(), "Почтового отделения с индексом " + POST_IND_RECIPIENT +
                " не существует");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void registerReceivePostDelivery_whenPostItemIdeNotCorrect_thenReturnBadRequest() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.empty());
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> postService.registerReceivePostDelivery(POST_IND_SENDER, postItem.getId()));

        assertEquals(entityNotFoundException.getMessage(), "Почтового отправления с индексом " + postItem.getId() +
                " не существует");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void registerReceivePostDelivery_whenPostOfficeNotIsOfficeWhereIsTheDelivery_thenReturnBadRequest() {

        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        StageDelivery badStageDelivery = getStageDelivery();
        PostOffice badOffice = getPostOfficeThree();
        badStageDelivery.setRecipientOffice(badOffice);
        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.of(postItem));
        when(postOfficeRepository.getPostOfficeByIndex(POST_IND_SENDER)).thenReturn(Optional.of(getPostOfficeOne()));

        DataIntegrityViolationException dataIntegrityViolationException = assertThrows(DataIntegrityViolationException.class,
                () -> postService.registerReceivePostDelivery(POST_IND_SENDER, postItem.getId()));

        assertEquals(dataIntegrityViolationException.getMessage(), "Посылка находится не в месте где ее должны получить");
        verify(stageDeliveryRepository, never()).save(any());
    }

    @Test
    void getHistory_whenAllCorrect_thenReturnPostDeliveryHistory() {
        NewPostDelivery newPostDelivery = getNewPostItem();
        PostItem postItem = new PostItem(1, POST_IND_SENDER, getTypePostItemLetter(),
                getAddress(), newPostDelivery.getUser());
        List<StageDelivery> stageDelivery = List.of(getStageDelivery());

        when(postItemsRepository.findById(postItem.getId())).thenReturn(Optional.of(postItem));
        when(stageDeliveryRepository.findByItemId(postItem.getId())).thenReturn(Optional.of(stageDelivery));

        PostDeliveryHistory postDeliveryHistory = postService.getHistory(postItem.getId());

        assertEquals(postDeliveryHistory.getStageDeliveryList().get(0).getStatusDelivery().getStatus(),
                "Зарегистрировано в отделении");
        assertEquals(postDeliveryHistory.getStageDeliveryList().get(0).getItem().getTypePostItem().getAlias(),
                LETTER);
        assertEquals(postDeliveryHistory.getStageDeliveryList().get(0).getSenderOffice().getIndex(),
                POST_IND_SENDER);
    }
}
