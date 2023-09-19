package org.inf5190.books.server;

import org.inf5190.books.Books.AddBookRequest;
import org.inf5190.books.Books.AddBookResponse;
import org.inf5190.books.Books.Book;
import org.inf5190.books.Books.BooksRequest;
import org.inf5190.books.Books.BooksResponse;
import org.inf5190.books.BooksServiceGrpc;
import org.inf5190.books.server.repository.BookRepository;

import io.grpc.stub.StreamObserver;

public class BookServerImpl extends BooksServiceGrpc.BooksServiceImplBase {

    private final BookRepository bookRepository = new BookRepository();

    @Override
    public void getBooks(BooksRequest req, StreamObserver<BooksResponse> responseObserver) {
        BooksResponse response = BooksResponse.newBuilder().addAllBooks(this.bookRepository.getBooks()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addBook(AddBookRequest req, StreamObserver<AddBookResponse> responseObserver) {
        Book book = this.bookRepository.addBook(req.getBook());
        AddBookResponse reponse = AddBookResponse.newBuilder().setBook(book).build();
        responseObserver.onNext(reponse);
        responseObserver.onCompleted();
    }
}