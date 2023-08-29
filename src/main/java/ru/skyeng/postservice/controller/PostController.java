package ru.skyeng.postservice.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.skyeng.postservice.model.PostItem;
import ru.skyeng.postservice.model.dto.DeliveryId;
import ru.skyeng.postservice.model.dto.NewPostDelivery;
import ru.skyeng.postservice.model.dto.PostDeliveryHistory;
import ru.skyeng.postservice.service.PostServiceImpl;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/e-posts")
public class PostController {

    private final PostServiceImpl postService;

    @PostMapping("/new/{typePostDelivery}")
    public PostItem createPostDelivery(@RequestHeader("X-Post-Office-Id") int ownIndex,
                                       @PathVariable("typePostDelivery") String typePostDelivery,
                                       @RequestBody NewPostDelivery postDelivery) {
        log.info("В отделении {} зарегистрировано и отправлено новое отправление - {} с параметрами:" +
                        "индекс - {}, адрес получателя - {}, имя получателя - {}",
                ownIndex, typePostDelivery, postDelivery.getIndex(), postDelivery.getAddress(), postDelivery.getUser());
        return postService.createPostItem(ownIndex, typePostDelivery, postDelivery);
    }

    @GetMapping("/register/departure")
    public PostItem registerDeparturePostItem(@RequestHeader("X-Post-Office-Id") int ownIndex,
                                              @RequestBody DeliveryId deliveryId,
                                              @RequestParam(name = "where", defaultValue = "0") int recipientOfficeId) {
        log.info("Из отделения {} в пункт {} отправлена посылка с трек-номером {}", ownIndex, recipientOfficeId,
                deliveryId.getId());
        return postService.registerDeparturePostDelivery(ownIndex, recipientOfficeId, deliveryId.getId());
    }

    @GetMapping("/register/arrival")
    public PostItem registerArrivalPostItem(@RequestHeader("X-Post-Office-Id") int ownIndex,
                                            @RequestBody DeliveryId deliveryId) {
        log.info("В отделение {} прибыла посылка с трек-номером {}", ownIndex, deliveryId.getId());
        return postService.registerArrivedPostDelivery(ownIndex, deliveryId.getId());
    }

    @GetMapping("/register/receiving")
    public PostItem registerReceivingPostItem(@RequestHeader("X-Post-Office-Id") int ownIndex,
                                              @RequestBody DeliveryId deliveryId) {
        log.info("Посылка с трек-номером {}, готова к получению в пункте {}", deliveryId.getId(), ownIndex);
        return postService.registerReceivingPostDelivery(ownIndex, deliveryId.getId());
    }

    @GetMapping("/register/receive")
    public PostItem registerReceivePostItem(@RequestHeader("X-Post-Office-Id") int ownIndex,
                                    @RequestBody DeliveryId deliveryId) {
        log.info("Посылка с трек-номером {} получена", deliveryId.getId());
        return postService.registerReceivePostDelivery(ownIndex, deliveryId.getId());
    }

    @GetMapping("/tracking/{postId}")
    public PostDeliveryHistory getHistory(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long postId) {
        log.info("Пользователем {} запрошена история по посылке {}", userId, postId);
        return postService.getHistory(userId, postId);
    }
}
