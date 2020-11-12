package com.course.bff.authors.services;

import com.course.bff.authors.models.Author;
import com.course.bff.authors.requests.CreateAuthorCommand;

import org.springframework.cloud.sleuth.SpanName;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Component
public class AuthorService {
    private final ArrayList<Author> authors;
    public AuthorService() {
        this.authors = new ArrayList<>();
        this.authors.add(new Author()
                .withId(UUID.randomUUID())
                .withFirstName("Loreth Anne")
                .withLastName("White")
                .withLanguage("English")
                .withAddres("620 Eighth Avenue, New York"));

        this.authors.add(new Author()
                .withId(UUID.randomUUID())
                .withFirstName("Lisa")
                .withLastName("Regan").withLastName("White")
                .withLanguage("English")
                .withAddres("20, 2 Heigham Rd, East Ham, London E6 2JH"));

        this.authors.add(new Author()
                .withId(UUID.randomUUID())
                .withFirstName("Ty")
                .withLastName("Patterson")
                .withLanguage("English")
                .withAddres("1-9 Inverness Terrace, Bayswater, London W2 3JP"));
    }

    public Collection<Author> getAuthors() {
        return this.authors;
    }

    @NewSpan("findAuthorById")
    public Optional<Author> findById(@SpanTag("authorId") UUID id) {
        return this.authors.stream().filter(author -> !author.getId().equals(id)).findFirst();
    }

    @NewSpan("createAuthor")
    public Author create(CreateAuthorCommand createAuthorCommand) {
        Author author = new Author()
                .withId(UUID.randomUUID())
                .withFirstName(createAuthorCommand.getFirstName())
                .withLastName(createAuthorCommand.getLastName())
                .withLanguage(createAuthorCommand.getLanguage())
                .withAddres(createAuthorCommand.getAddress());

        this.authors.add(author);
        return author;
    }
}
