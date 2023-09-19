package org.inf5190.books.client;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.inf5190.books.BooksServiceGrpc;
import org.inf5190.books.Books.AddBookRequest;
import org.inf5190.books.Books.Book;
import org.inf5190.books.Books.BooksRequest;
import org.inf5190.books.Books.BooksResponse;

import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

public class BookClient {
    private final BooksServiceGrpc.BooksServiceBlockingStub blockingStub;

    public BookClient(Channel channel) {
        blockingStub = BooksServiceGrpc.newBlockingStub(channel);
    }

    public static void main(String[] args) throws Exception {
        String target = "127.0.0.1:50051";
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();
        final Scanner inputReader = new Scanner(System.in);

        try {
            BookClient client = new BookClient(channel);
            boolean quit = false;

            while (!quit) {
                System.out.println("Entrez la commande [ (n)ouveau, (l)iste, (q)uitter]: ");
                String command = inputReader.nextLine();

                switch (command) {
                    case "nouveau":
                    case "n": {
                        System.out.println("Entrez le titre: ");
                        final String title = inputReader.nextLine();
                        System.out.println("Entrez le nom de l'auteur: ");
                        final String author = inputReader.nextLine();
                        Book book = Book.newBuilder().setTitle(title).setAuthor(author).build();
                        client.blockingStub.addBook(AddBookRequest.newBuilder().setBook(book).build());
                        System.out.println("Livre ajouté.\n");
                        break;

                    }
                    case "liste":
                    case "l": {
                        BooksResponse response = client.blockingStub.getBooks(BooksRequest.getDefaultInstance());
                        for (Book book : response.getBooksList()) {
                            System.out.println(book.getId() + " : " + book.getTitle() + ", " + book.getAuthor());
                        }
                        System.out.println("\n");
                        break;

                    }
                    case "quitter":
                    case "q": {
                        quit = true;
                        break;
                    }
                    default: {
                        System.out.println("Commande invalide\n");
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            inputReader.close();
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
