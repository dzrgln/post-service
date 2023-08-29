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
    public PostItem createPostDelivery(@RequestHeader("X-Post-Office-Id") int postOfficeInd,
                                       @PathVariable("typePostDelivery") String typePostDelivery,
                                       @RequestBody NewPostDelivery postDelivery) {
        log.info("В отделении {} зарегистрировано и отправлено новое отправление - {} с параметрами:" +
                        "индекс - {}, адрес получателя - {}, имя получателя - {}",
                postOfficeInd, typePostDelivery, postDelivery.getIndex(), postDelivery.getAddress(), postDelivery.getUser());
        return postService.createPostItem(postOfficeInd, typePostDelivery, postDelivery);
    }

    @PutMapping("/register/departure")
    public PostItem registerDeparturePostItem(@RequestHeader("X-Post-Office-Id") int postOfficeInd,
                                              @RequestBody DeliveryId deliveryId,
                                              @RequestParam(name = "where", defaultValue = "0") int recipientOfficeId) {
        log.info("Из отделения {} в пункт {} отправлена посылка с трек-номером {}", postOfficeInd, recipientOfficeId,
                deliveryId.getId());
        return postService.registerDeparturePostDelivery(postOfficeInd, recipientOfficeId, deliveryId.getId());
    }

    @PutMapping("/register/arrival")
    public PostItem registerArrivalPostItem(@RequestHeader("X-Post-Office-Id") int postOfficeInd,
                                            @RequestBody DeliveryId deliveryId) {
        log.info("В отделение {} прибыла посылка с трек-номером {}", postOfficeInd, deliveryId.getId());
        return postService.registerArrivedPostDelivery(postOfficeInd, deliveryId.getId());
    }

    @PutMapping("/register/receiving")
    public PostItem registerReceivingPostItem(@RequestHeader("X-Post-Office-Id") int postOfficeInd,
                                              @RequestBody DeliveryId deliveryId) {
        log.info("Посылка с трек-номером {}, готова к получению в пункте {}", deliveryId.getId(), postOfficeInd);
        return postService.registerReceivingPostDelivery(postOfficeInd, deliveryId.getId());
    }

    @PutMapping("/register/receive")
    public PostItem registerReceivePostItem(@RequestHeader("X-Post-Office-Id") int postOfficeInd,
                                            @RequestBody DeliveryId deliveryId) {
        log.info("Посылка с трек-номером {} получена", deliveryId.getId());
        return postService.registerReceivePostDelivery(postOfficeInd, deliveryId.getId());
    }

    @GetMapping("/tracking/{postId}")
    public PostDeliveryHistory getHistory(@PathVariable Long postId) {
        log.info("Получен на получение исторнии перемещения по посылке {}", postId);
        return postService.getHistory(postId);
    }
}
