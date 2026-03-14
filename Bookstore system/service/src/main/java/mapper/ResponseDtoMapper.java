package mapper;

import domain.model.impl.*;
import dto.BookRequestSummary;
import dto.BookSummary;
import dto.OrderItemSummary;
import dto.OrderSummary;
import dto.request.ConsumerRequestDto;
import dto.response.BookDescriptionResponseDto;
import dto.response.BookRequestResponseDto;
import dto.response.BookResponseDto;
import dto.response.ConsumerResponseDto;
import dto.response.OrderItemResponseDto;
import dto.response.OrderResponseDto;
import enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования доменных объектов в DTO ответов API.
 */
public final class ResponseDtoMapper {

    private ResponseDtoMapper() {
    }

    public static BookResponseDto toBookResponseDto(Book book) {
        if (book == null) return null;
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getPublishDate(),
                book.getPrice(),
                book.getStatus()
        );
    }

    public static List<BookResponseDto> toBookResponseDtoList(List<Book> list) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                .map(ResponseDtoMapper::toBookResponseDto)
                .collect(Collectors.toList());
    }

    public static BookDescriptionResponseDto toBookDescriptionResponse(Long bookId, String description) {
        return new BookDescriptionResponseDto(bookId, description);
    }

    public static ConsumerResponseDto toConsumerResponseDto(Consumer consumer) {
        if (consumer == null) return null;
        return new ConsumerResponseDto(
                consumer.getId(),
                consumer.getName(),
                consumer.getPhone(),
                consumer.getEmail()
        );
    }

    public static Consumer toConsumer(ConsumerRequestDto consumerRequestDto) {
        if (consumerRequestDto == null) return null;
        return new Consumer(
                consumerRequestDto.getName(),
                consumerRequestDto.getPhone(),
                consumerRequestDto.getEmail()
        );
    }

    public static List<ConsumerResponseDto> toConsumerResponseDtoList(List<Consumer> list) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                .map(ResponseDtoMapper::toConsumerResponseDto)
                .collect(Collectors.toList());
    }

    public static OrderItemResponseDto toOrderItemResponseDto(OrderItem item) {
        if (item == null) return null;
        return new OrderItemResponseDto(
                item.getId(),
                item.getBook() != null ? item.getBook().getId() : null,
                item.getBook() != null ? item.getBook().getTitle() : null,
                item.getQuantity()
        );
    }

    public static OrderResponseDto toOrderResponseDto(Order order) {
        if (order == null) return null;
        ConsumerResponseDto consumerDto = order.getConsumer() != null
                ? toConsumerResponseDto(order.getConsumer())
                : null;
        List<OrderItemResponseDto> items = order.getOrderItemsList() != null
                ? order.getOrderItemsList().stream()
                .map(ResponseDtoMapper::toOrderItemResponseDto)
                .collect(Collectors.toList())
                : Collections.emptyList();
        return new OrderResponseDto(
                order.getId(),
                consumerDto,
                order.getCreatedAtDate(),
                order.getCompletedAtDate(),
                items,
                order.getTotalPrice(),
                order.getOrderStatus()
        );
    }

    public static List<OrderResponseDto> toOrderResponseDtoList(List<Order> list) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                .map(ResponseDtoMapper::toOrderResponseDto)
                .collect(Collectors.toList());
    }

    public static OrderResponseDto toOrderResponseDto(Order order, Consumer consumer, List<OrderItem> items) {
        if (order == null) return null;
        ConsumerResponseDto consumerDto = toConsumerResponseDto(consumer);
        List<OrderItemResponseDto> itemDtos = items != null
                ? items.stream().map(ResponseDtoMapper::toOrderItemResponseDto).collect(Collectors.toList())
                : Collections.emptyList();
        return new OrderResponseDto(
                order.getId(),
                consumerDto,
                order.getCreatedAtDate(),
                order.getCompletedAtDate(),
                itemDtos,
                order.getTotalPrice(),
                order.getOrderStatus()
        );
    }

    public static BookRequestResponseDto toBookRequestResponseDto(BookRequest summary) {
        if (summary == null) return null;
        Long orderId = summary.getRelatedOrder().getId() != null ? summary.getRelatedOrder().getId() : null;
        LocalDateTime requestDate = summary.getRequestDate();
        LocalDateTime deliveryDate = summary.getDeliveryDate();
        RequestStatus requestStatus = summary.getStatus();

        return new BookRequestResponseDto(orderId, requestDate, deliveryDate, requestStatus);
    }

    public static List<BookRequestResponseDto> toBookRequestResponseDtoList(List<BookRequest> list) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                .map(ResponseDtoMapper::toBookRequestResponseDto)
                .collect(Collectors.toList());
    }
}
