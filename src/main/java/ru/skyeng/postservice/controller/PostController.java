package ru.skyeng.postservice.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PatchExchange;
import ru.skyeng.postservice.model.PostItem;
import ru.skyeng.postservice.model.dto.DeliveryId;
import ru.skyeng.postservice.model.dto.NewPostDelivery;
import ru.skyeng.postservice.model.dto.PostDeliveryHistory;
import ru.skyeng.postservice.service.PostService;

@RestController
@Slf4j
@RequestMapping("/e-posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/new/{typePostDelivery}")
    public PostItem createPostDelivery(@RequestHeader("X-Post-Ofice-Id") int ownIndex,
                                       @PathVariable("typePostDelivery") String typePostDelivery,
                                       @RequestBody NewPostDelivery postDelivery) {
        log.info("В отделении {} зарегистрировано и отправлено новое отправление - {} с параметрами:" +
                        "индекс - {}, адрес получателя - {}, имя получателя - {}",
                ownIndex, typePostDelivery, postDelivery.getIndex(), postDelivery.getAddress(), postDelivery.getAddress());
        return postService.createPostDelivery(ownIndex, typePostDelivery, postDelivery);
    }

    @PatchExchange("/register/arrival")
    public PostItem registerArrivalPostItem(@RequestHeader("X-Post-Ofice-Id") int ownIndex,
                                            @RequestBody DeliveryId deliveryId) {
        log.info("В отделение {} прибыла посылка с трек-номером {}", ownIndex, deliveryId.getId());
        return postService.registerArrivedPostDelivery(ownIndex, deliveryId.getId());
    }

    @PatchExchange("/register/departure")
    public PostItem registerDeparturePostItem(@RequestHeader("X-Post-Ofice-Id") int ownIndex,
                                              @RequestBody DeliveryId deliveryId) {
        log.info("Из отделения {} отправлена посылка с трек-номером {}", ownIndex, deliveryId.getId());
        return postService.registerDeparturePostDelivery(ownIndex, deliveryId.getId());
    }

    @PatchExchange()
    public PostItem receivePostItem(@RequestBody DeliveryId deliveryId) {
        log.info("Посылка с трек-номером {} получена",deliveryId.getId());
        return postService.registerReceivingPostDelivery(deliveryId.getId());
    }

    @GetMapping("/tracking/{postId}")
    public PostDeliveryHistory getHistory(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long postId) {
        log.info("Пользователем {} запрошена история по посылке {}", userId, postId);
        return postService.getHistory(userId, postId);
    }
}
