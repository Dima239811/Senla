package bookstore.model.mapper;

import bookstore.dto.BookRequest;
import bookstore.dto.BookResponse;
import bookstore.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    BookResponse toBookResponse(Book book);
    List<BookResponse> toBookResponseList(List<Book> books);

    @Mapping(target = "bookId", ignore = true)
    @Mapping(target = "status", expression = "java(StatusBook.AVAILABLE)")
    Book toEntity(BookRequest request);
}
